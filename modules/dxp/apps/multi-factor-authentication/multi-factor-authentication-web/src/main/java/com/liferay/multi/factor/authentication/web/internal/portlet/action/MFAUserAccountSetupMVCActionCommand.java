/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.multi.factor.authentication.web.internal.portlet.action;

import com.liferay.multi.factor.authentication.spi.checker.setup.SetupMFAChecker;
import com.liferay.multi.factor.authentication.web.internal.policy.MFAPolicy;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.users.admin.constants.UsersAdminPortletKeys;

import java.util.Optional;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Tomas Polesovsky
 * @author Marta Medio
 */
@Component(
	property = {
		"javax.portlet.name=" + UsersAdminPortletKeys.MY_ACCOUNT,
		"mvc.command.name=/my_account/setup_mfa"
	},
	service = MVCActionCommand.class
)
public class MFAUserAccountSetupMVCActionCommand extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		Optional<SetupMFAChecker> optionalMfaSetupChecker =
			_mfaPolicy.getSetupMFAChecker(_portal.getCompanyId(actionRequest));

		if (!optionalMfaSetupChecker.isPresent()) {
			_log.error(
				"Unable to generate user account setup for Multi-Factor " +
					"Authentication: Setup verifier not present");

			return;
		}

		SetupMFAChecker mfaSetupChecker = optionalMfaSetupChecker.get();

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		boolean mfaRemoveTimeBasedOTP = ParamUtil.getBoolean(
			actionRequest, "mfaRemoveTimeBasedOTP");

		if (mfaRemoveTimeBasedOTP) {
			mfaSetupChecker.removeExistingSetup(themeDisplay.getUserId());
		}

		if (mfaSetupChecker.setUp(
				_portal.getHttpServletRequest(actionRequest),
				themeDisplay.getUserId())) {

			String redirect = _portal.escapeRedirect(
				ParamUtil.getString(actionRequest, "redirect"));

			if (Validator.isBlank(redirect)) {
				redirect = themeDisplay.getPortalURL();
			}

			actionResponse.sendRedirect(redirect);

			return;
		}

		if (!mfaRemoveTimeBasedOTP) {
			SessionErrors.add(actionRequest, "userAccountSetupFailed");
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		MFAUserAccountSetupMVCActionCommand.class);

	@Reference
	private MFAPolicy _mfaPolicy;

	@Reference
	private Portal _portal;

}
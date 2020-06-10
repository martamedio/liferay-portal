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
import com.liferay.petra.string.StringBundler;
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

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
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

	@Activate
	protected void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;
	}

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		SetupMFAChecker mfaSetupChecker = getSetupMFAChecker(actionRequest);

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

	protected SetupMFAChecker getSetupMFAChecker(ActionRequest actionRequest) {
		long setupMFACheckerServiceId = ParamUtil.getLong(
			actionRequest, "setupMFACheckerServiceId");

		ServiceReference<?> setupMFACheckerServiceReference =
			_bundleContext.getServiceReference(
				StringBundler.concat(
					"(service.id=", setupMFACheckerServiceId, ")"));

		if (setupMFACheckerServiceReference == null) {
			_log.error(
				"Unable to find SetupMFAChecker with service.id " +
					setupMFACheckerServiceId);

			return null;
		}

		Object setupMFAChecker = _bundleContext.getService(
			setupMFACheckerServiceReference);

		if (setupMFAChecker instanceof SetupMFAChecker) {
			return (SetupMFAChecker)setupMFAChecker;
		}

		_log.error(
			"Service with id " + setupMFACheckerServiceId +
				" is not a SetupMFAChecker or is errored");

		return null;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		MFAUserAccountSetupMVCActionCommand.class);

	private BundleContext _bundleContext;

	@Reference
	private Portal _portal;

}
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

package com.liferay.multi.factor.authentication.verifier.web.internal.portlet.action;

import com.liferay.multi.factor.authentication.verifier.spi.checker.MFABrowserChecker;
import com.liferay.multi.factor.authentication.verifier.web.internal.constants.MFAPortletKeys;
import com.liferay.multi.factor.authentication.verifier.web.internal.constants.MFAWebKeys;
import com.liferay.multi.factor.authentication.verifier.web.policy.MFAPolicy;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.List;

import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Tomas Polesovsky
 * @author Marta Medio
 */
@Component(
	property = {
		"javax.portlet.name=" + MFAPortletKeys.MFA_VERIFY_PORTLET_KEY,
		"mvc.command.name=/mfa_verify/view"
	},
	service = MVCRenderCommand.class
)
public class MFAVerifyMVCRenderCommand implements MVCRenderCommand {

	@Override
	public String render(
		RenderRequest renderRequest, RenderResponse renderResponse) {

		long mfaUserId = _getMFAUserId(renderRequest);

		if (mfaUserId == 0) {
			SessionErrors.add(renderRequest, "sessionExpired");

			return "/error.jsp";
		}

		List<MFABrowserChecker> availableBrowserCheckers =
			_mfaPolicy.getAvailableBrowserCheckers(
				_portal.getCompanyId(renderRequest), mfaUserId);

		int mfaCheckerIndex = ParamUtil.getInteger(
			renderRequest, "mfaCheckerIndex");

		MFABrowserChecker mfaBrowserChecker;

		if ((mfaCheckerIndex > -1) &&
			(mfaCheckerIndex < availableBrowserCheckers.size())) {

			mfaBrowserChecker = availableBrowserCheckers.get(mfaCheckerIndex);
		}
		else {
			mfaBrowserChecker = availableBrowserCheckers.get(0);
		}

		renderRequest.setAttribute(
			MFAWebKeys.MFA_AVAILABLE_CHECKERS, availableBrowserCheckers);
		renderRequest.setAttribute(MFAWebKeys.MFA_CHECKER, mfaBrowserChecker);
		renderRequest.setAttribute(MFAWebKeys.MFA_USER_ID, mfaUserId);

		return "/mfa_verify/verify.jsp";
	}

	private long _getMFAUserId(PortletRequest portletRequest) {
		ThemeDisplay themeDisplay = (ThemeDisplay)portletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		if (themeDisplay.isSignedIn()) {
			return themeDisplay.getUserId();
		}

		HttpServletRequest httpServletRequest =
			_portal.getOriginalServletRequest(
				_portal.getHttpServletRequest(portletRequest));

		HttpSession httpSession = httpServletRequest.getSession();

		return GetterUtil.getLong(
			httpSession.getAttribute(MFAWebKeys.MFA_USER_ID));
	}

	@Reference
	private MFAPolicy _mfaPolicy;

	@Reference
	private Portal _portal;

}
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

import com.liferay.multi.factor.authentication.spi.checker.browser.MFABrowserChecker;
import com.liferay.multi.factor.authentication.web.internal.constants.MFAPortletKeys;
import com.liferay.multi.factor.authentication.web.internal.constants.MFAWebKeys;
import com.liferay.multi.factor.authentication.web.policy.MFAPolicy;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.ResourceBundleLoader;
import com.liferay.portal.kernel.util.ResourceBundleLoaderUtil;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.List;
import java.util.Locale;

import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Tomas Polesovsky
 * @author Marta Medio
 */
@Component(
	property = {
		"javax.portlet.name=" + MFAPortletKeys.VERIFY,
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

		List<MFABrowserChecker> mfaBrowserCheckers =
			_mfaPolicy.getAvailableMFABrowserCheckers(
				_portal.getCompanyId(renderRequest), mfaUserId);

		int mfaCheckerIndex = ParamUtil.getInteger(
			renderRequest, "mfaCheckerIndex");

		MFABrowserChecker mfaBrowserChecker = null;

		if ((mfaCheckerIndex > -1) &&
			(mfaCheckerIndex < mfaBrowserCheckers.size())) {

			mfaBrowserChecker = mfaBrowserCheckers.get(mfaCheckerIndex);
		}
		else {
			mfaBrowserChecker = mfaBrowserCheckers.get(0);
		}

		ThemeDisplay themeDisplay = (ThemeDisplay)renderRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		renderRequest.setAttribute(
			MFAWebKeys.MFA_BROWSER_CHECKER, mfaBrowserChecker);
		renderRequest.setAttribute(
			MFAWebKeys.MFA_BROWSER_CHECKER_NAME,
			_getMFACheckerName(themeDisplay.getLocale(), mfaBrowserChecker));
		renderRequest.setAttribute(
			MFAWebKeys.MFA_BROWSER_CHECKERS, mfaBrowserCheckers);
		renderRequest.setAttribute(MFAWebKeys.MFA_USER_ID, mfaUserId);

		return "/mfa_verify/verify.jsp";
	}

	private String _getMFACheckerName(
		Locale locale, MFABrowserChecker mfaBrowserChecker) {

		Class<? extends MFABrowserChecker> clazz = mfaBrowserChecker.getClass();

		Bundle bundle = FrameworkUtil.getBundle(clazz);

		ResourceBundleLoader resourceBundleLoader =
			ResourceBundleLoaderUtil.
				getResourceBundleLoaderByBundleSymbolicName(
					bundle.getSymbolicName());

		return GetterUtil.getString(
			ResourceBundleUtil.getString(
				resourceBundleLoader.loadResourceBundle(locale),
				clazz.getName()),
			clazz.getName());
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
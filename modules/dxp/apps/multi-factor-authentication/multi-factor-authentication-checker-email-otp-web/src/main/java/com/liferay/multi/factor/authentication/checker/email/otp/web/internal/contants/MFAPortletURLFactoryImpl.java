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

package com.liferay.multi.factor.authentication.checker.email.otp.web.internal.contants;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletURL;
import com.liferay.portal.kernel.portlet.PortletURLFactory;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.PortletRequest;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Tomas Polesovsky
 */
@Component(service = MFAPortletURLFactory.class)
public class MFAPortletURLFactoryImpl implements MFAPortletURLFactory {

	@Override
	public LiferayPortletURL createVerifyURL(
		HttpServletRequest request, String redirectURL, long userId) {

		request = _portal.getOriginalServletRequest(request);

		HttpSession session = request.getSession();

		session.setAttribute(MFA_USER_ID, userId);

		long plid = 0;
		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		if (themeDisplay != null) {
			plid = themeDisplay.getPlid();
		}

		LiferayPortletURL liferayPortletURL = _portletURLFactory.create(
			request, MFAPortletKeys.MFA_VERIFY_PORTLET, plid,
			PortletRequest.RENDER_PHASE);

		liferayPortletURL.setParameter(
			"mvcRenderCommandName", "/mfa_verify/view");
		liferayPortletURL.setParameter("redirect", redirectURL);
		liferayPortletURL.setParameter(
			"saveLastPath", Boolean.FALSE.toString());

		return liferayPortletURL;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		MFAPortletURLFactoryImpl.class);

	@Reference
	private Portal _portal;

	@Reference
	private PortletURLFactory _portletURLFactory;

}
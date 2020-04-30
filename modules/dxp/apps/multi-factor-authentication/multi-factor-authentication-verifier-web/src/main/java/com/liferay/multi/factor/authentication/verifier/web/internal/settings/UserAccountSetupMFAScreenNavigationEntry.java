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

package com.liferay.multi.factor.authentication.verifier.web.internal.settings;

import com.liferay.frontend.taglib.servlet.taglib.ScreenNavigationEntry;
import com.liferay.multi.factor.authentication.verifier.spi.checker.MFASetupChecker;
import com.liferay.multi.factor.authentication.verifier.web.internal.constants.MFAPortletKeys;
import com.liferay.portal.kernel.model.User;
import com.liferay.users.admin.constants.UserScreenNavigationEntryConstants;

import java.io.IOException;

import java.util.Locale;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Marta Medio
 */
public class UserAccountSetupMFAScreenNavigationEntry
	implements ScreenNavigationEntry<User> {

	public UserAccountSetupMFAScreenNavigationEntry(
		MFASetupChecker mfaSetupChecker) {

		_mfaSetupChecker = mfaSetupChecker;
	}

	@Override
	public String getCategoryKey() {
		return MFAPortletKeys.CATEGORY_KEY_MFA;
	}

	@Override
	public String getEntryKey() {
		return _mfaSetupChecker.getName();
	}

	@Override
	public String getLabel(Locale locale) {
		return _mfaSetupChecker.getSetupLabelConfigurationKey(locale);
	}

	@Override
	public String getScreenNavigationKey() {
		return UserScreenNavigationEntryConstants.SCREEN_NAVIGATION_KEY_USERS;
	}

	@Override
	public boolean isVisible(User user, User context) {
		if (_mfaSetupChecker != null) {
			return true;
		}

		return false;
	}

	@Override
	public void render(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws IOException {

		httpServletRequest.setAttribute(
			MFASetupChecker.class.getName(), _mfaSetupChecker);

		httpServletRequest.setAttribute(
			"label", getLabel(httpServletRequest.getLocale()));
		httpServletRequest.setAttribute(
			"screenNavigationCategoryKey", getCategoryKey());
		httpServletRequest.setAttribute(
			"screenNavigationEntryKey", getEntryKey());

		RequestDispatcher requestDispatcher =
			_servletContext.getRequestDispatcher(
				"/my_account/user_account_setup.jsp");

		try {
			requestDispatcher.include(httpServletRequest, httpServletResponse);
		}
		catch (ServletException servletException) {
			throw new IOException(
				"Unable to render /my_account/user_account_setup.jsp",
				servletException);
		}
	}

	public void setServletContext(ServletContext servletContext) {
		_servletContext = servletContext;
	}

	private final MFASetupChecker _mfaSetupChecker;
	private ServletContext _servletContext;

}
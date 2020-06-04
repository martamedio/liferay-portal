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

package com.liferay.multi.factor.authentication.web.internal.frontend.taglib.servlet.taglib;

import com.liferay.frontend.taglib.servlet.taglib.ScreenNavigationEntry;
import com.liferay.multi.factor.authentication.spi.checker.setup.SetupMFAChecker;
import com.liferay.multi.factor.authentication.web.internal.constants.MFAUserAccountSetupScreenNavigationConstants;
import com.liferay.multi.factor.authentication.web.internal.constants.MFAWebKeys;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ResourceBundleLoader;
import com.liferay.portal.kernel.util.ResourceBundleLoaderUtil;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.users.admin.constants.UserScreenNavigationEntryConstants;

import java.io.IOException;

import java.util.Locale;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * @author Marta Medio
 */
public class MFAUserAccountSetupScreenNavigationEntry
	implements ScreenNavigationEntry<User> {

	public MFAUserAccountSetupScreenNavigationEntry(
		SetupMFAChecker mfaSetupChecker, ServletContext servletContext) {

		_mfaSetupChecker = mfaSetupChecker;
		_servletContext = servletContext;
	}

	@Override
	public String getCategoryKey() {
		return MFAUserAccountSetupScreenNavigationConstants.CATEGORY_KEY_MFA;
	}

	@Override
	public String getEntryKey() {
		Class<? extends SetupMFAChecker> clazz = _mfaSetupChecker.getClass();

		return clazz.getName();
	}

	@Override
	public String getLabel(Locale locale) {
		Class<? extends SetupMFAChecker> clazz = _mfaSetupChecker.getClass();

		Bundle bundle = FrameworkUtil.getBundle(clazz);

		ResourceBundleLoader resourceBundleLoader =
			ResourceBundleLoaderUtil.
				getResourceBundleLoaderByBundleSymbolicName(
					bundle.getSymbolicName());

		String name = clazz.getName();

		return GetterUtil.getString(
			ResourceBundleUtil.getString(
				resourceBundleLoader.loadResourceBundle(locale), name),
			name);
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
			SetupMFAChecker.class.getName(), _mfaSetupChecker);

		httpServletRequest.setAttribute(
			MFAWebKeys.MFA_USER_ACCOUNT_LABEL,
			getLabel(httpServletRequest.getLocale()));

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

	private final SetupMFAChecker _mfaSetupChecker;
	private final ServletContext _servletContext;

}
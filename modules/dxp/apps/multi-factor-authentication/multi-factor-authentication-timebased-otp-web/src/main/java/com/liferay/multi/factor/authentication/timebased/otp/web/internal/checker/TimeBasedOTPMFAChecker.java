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

package com.liferay.multi.factor.authentication.timebased.otp.web.internal.checker;

import com.liferay.multi.factor.authentication.spi.checker.browser.BrowserMFAChecker;
import com.liferay.multi.factor.authentication.spi.checker.setup.SetupMFAChecker;
import com.liferay.multi.factor.authentication.timebased.otp.model.MFATimeBasedOTPEntry;
import com.liferay.multi.factor.authentication.timebased.otp.service.MFATimeBasedOTPEntryLocalService;
import com.liferay.multi.factor.authentication.timebased.otp.web.internal.audit.MFATimeBasedOTPAuditMessageBuilder;
import com.liferay.multi.factor.authentication.timebased.otp.web.internal.configuration.MFATimeBasedOTPConfiguration;
import com.liferay.multi.factor.authentication.timebased.otp.web.internal.constants.MFATimeBasedOTPWebKeys;
import com.liferay.multi.factor.authentication.timebased.otp.web.internal.util.MFATimeBasedOTPUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.audit.AuditMessage;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.url.builder.AbsolutePortalURLBuilderFactory;
import com.liferay.portal.util.PropsValues;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

/**
 * @author Tomas Polesovsky
 * @author Marta Medio
 */
@Component(
	configurationPid = "com.liferay.multi.factor.authentication.timebased.otp.web.internal.configuration.MFATimeBasedOTPConfiguration.scoped",
	configurationPolicy = ConfigurationPolicy.REQUIRE, immediate = true,
	service = {}
)
public class TimeBasedOTPMFAChecker
	implements BrowserMFAChecker, SetupMFAChecker {

	@Override
	public void includeBrowserVerification(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, long userId)
		throws IOException, ServletException {

		RequestDispatcher requestDispatcher =
			_servletContext.getRequestDispatcher(
				"/mfa_timebased_otp_checker/verify_browser.jsp");

		requestDispatcher.include(httpServletRequest, httpServletResponse);
	}

	@Override
	public void includeSetup(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, long userId)
		throws Exception {

		MFATimeBasedOTPEntry mfaTimeBasedOTPEntry =
			_mfaTimeBasedOTPEntryLocalService.fetchMFATimeBasedOTPEntryByUserId(
				userId);

		if (mfaTimeBasedOTPEntry != null) {
			RequestDispatcher requestDispatcher =
				_servletContext.getRequestDispatcher(
					"/mfa_timebased_otp_checker/setup_completed.jsp");

			requestDispatcher.include(
				httpServletRequest, httpServletResponse);
		}
		else {
			Company company = _portal.getCompany(httpServletRequest);

			httpServletRequest.setAttribute(
				MFATimeBasedOTPWebKeys.MFA_TIME_BASED_OTP_ALGORITHM,
				MFATimeBasedOTPUtil.MFA_TIMEBASED_OTP_ALGORITHM);
			httpServletRequest.setAttribute(
				MFATimeBasedOTPWebKeys.MFA_TIME_BASED_OTP_COMPANY_NAME,
				company.getName());
			httpServletRequest.setAttribute(
				MFATimeBasedOTPWebKeys.MFA_TIME_BASED_OTP_DIGITS,
				MFATimeBasedOTPUtil.MFA_TIMEBASED_OTP_DIGITS);
			httpServletRequest.setAttribute(
				MFATimeBasedOTPWebKeys.MFA_TIME_BASED_OTP_TIME_COUNTER,
				MFATimeBasedOTPUtil.MFA_TIMEBASED_OTP_COUNTER);

			HttpServletRequest originalHttpServletRequest =
				_portal.getOriginalServletRequest(httpServletRequest);

			HttpSession session = originalHttpServletRequest.getSession();

			session.setAttribute(
				MFATimeBasedOTPWebKeys.MFA_TIME_BASED_OTP_SHARED_SECRET,
				MFATimeBasedOTPUtil.generateSharedSecret(_algorithmKeySize));

			RequestDispatcher requestDispatcher =
				_servletContext.getRequestDispatcher(
					"/mfa_timebased_otp_checker/setup.jsp");

			requestDispatcher.include(
				httpServletRequest, httpServletResponse);
		}
	}

	@Override
	public boolean isAvailable(long userId) {
		MFATimeBasedOTPEntry mfaTimeBasedOTPEntry =
			_mfaTimeBasedOTPEntryLocalService.fetchMFATimeBasedOTPEntryByUserId(
				userId);

		if (mfaTimeBasedOTPEntry != null) {
			return true;
		}

		return false;
	}

	@Override
	public boolean isBrowserVerified(
		HttpServletRequest httpServletRequest, long userId) {

		HttpServletRequest originalHttpServletRequest =
			_portal.getOriginalServletRequest(httpServletRequest);

		HttpSession httpSession = originalHttpServletRequest.getSession(false);

		if (isVerified(httpSession, userId)) {
			return true;
		}

		return false;
	}

	@Override
	public void removeExistingSetup(long userId) {
		MFATimeBasedOTPEntry mfaTimeBasedOTPEntry =
			_mfaTimeBasedOTPEntryLocalService.fetchMFATimeBasedOTPEntryByUserId(
				userId);

		if (mfaTimeBasedOTPEntry != null) {
			_mfaTimeBasedOTPEntryLocalService.deleteMFATimeBasedOTPEntry(
				mfaTimeBasedOTPEntry);
		}
	}

	@Override
	public boolean setUp(HttpServletRequest httpServletRequest, long userId) {
		HttpServletRequest originalHttpServletRequest =
			_portal.getOriginalServletRequest(httpServletRequest);

		HttpSession httpSession = originalHttpServletRequest.getSession();

		String mfaTimeBasedOTPSharedSecret = (String)httpSession.getAttribute(
			MFATimeBasedOTPWebKeys.MFA_TIME_BASED_OTP_SHARED_SECRET);
		String mfaTimeBasedOTP = ParamUtil.getString(
			httpServletRequest, "mfaTimeBasedOTP");

		try {
			if (MFATimeBasedOTPUtil.verifyTimeBasedOTP(
					_clockSkew, mfaTimeBasedOTPSharedSecret, mfaTimeBasedOTP)) {

				MFATimeBasedOTPEntry timeBasedOTPEntry =
					_mfaTimeBasedOTPEntryLocalService.addTimeBasedOTPEntry(
						mfaTimeBasedOTPSharedSecret, userId);

				if (timeBasedOTPEntry != null) {
					return true;
				}
			}
		}
		catch (PortalException portalException) {
			_log.error(
				StringBundler.concat(
					"Unable to generate time-based one-time password for user ",
					userId, ": ", portalException.getMessage()),
				portalException);
		}

		return false;
	}

	@Override
	public boolean verifyBrowserRequest(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, long userId)
		throws Exception {

		User user = _userLocalService.fetchUser(userId);

		if (user == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Requested one-time password time-based verification for " +
						"nonexistent user " + userId);
			}

			_routeAuditMessage(
				_mfaTimeBasedOTPAuditMessageBuilder.
					buildNonexistentUserVerificationFailureAuditMessage(
						CompanyThreadLocal.getCompanyId(), userId,
						_getClassName()));

			return false;
		}

		if (!isAvailable(user.getUserId())) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Requested time-based one time password for user" + userId +
						" with incomplete configuration");
			}

			_routeAuditMessage(
				_mfaTimeBasedOTPAuditMessageBuilder.
					buildUnconfiguredUserVerificationFailureAuditMessage(
						CompanyThreadLocal.getCompanyId(), user,
						_getClassName()));

			return false;
		}

		String mfaTimeBasedOTP = ParamUtil.getString(
			httpServletRequest, "mfaTimeBasedOTP");

		if (Validator.isBlank(mfaTimeBasedOTP)) {
			return false;
		}

		boolean verified = verifyTimeBasedOTP(
			mfaTimeBasedOTP, user.getUserId());

		HttpServletRequest originalHttpServletRequest =
			_portal.getOriginalServletRequest(httpServletRequest);

		String remoteAddress = originalHttpServletRequest.getRemoteAddr();

		if (verified) {
			long validatedAt = System.currentTimeMillis();

			HttpSession session = originalHttpServletRequest.getSession();

			Map<String, Object> validatedMap = _getValidatedMap(session);

			if (validatedMap == null) {
				validatedMap = new HashMap<>(2);

				session.setAttribute(_VALIDATED, validatedMap);
			}

			validatedMap.put("userId", userId);
			validatedMap.put("validatedAt", validatedAt);

			_mfaTimeBasedOTPEntryLocalService.updateAttempts(
				userId, remoteAddress, true);

			_routeAuditMessage(
				_mfaTimeBasedOTPAuditMessageBuilder.
					buildVerificationSuccessAuditMessage(
						user, _getClassName()));
		}
		else {
			_mfaTimeBasedOTPEntryLocalService.updateAttempts(
				user.getUserId(), remoteAddress, false);

			_routeAuditMessage(
				_mfaTimeBasedOTPAuditMessageBuilder.
					buildVerificationFailureAuditMessage(
						user, _getClassName(),
						"Incorrect time-based one-time password"));
		}

		return verified;
	}

	@Activate
	protected void activate(
		BundleContext bundleContext, Map<String, Object> properties) {

		MFATimeBasedOTPConfiguration mfaTimeBasedOTPConfiguration =
			ConfigurableUtil.createConfigurable(
				MFATimeBasedOTPConfiguration.class, properties);

		if (!mfaTimeBasedOTPConfiguration.enabled()) {
			return;
		}

		_algorithmKeySize = mfaTimeBasedOTPConfiguration.algorithmKeySize();
		_clockSkew = mfaTimeBasedOTPConfiguration.clockSkew();
		_validationExpirationTime =
			mfaTimeBasedOTPConfiguration.validationExpirationTime();

		if (PropsValues.SESSION_ENABLE_PHISHING_PROTECTION) {
			List<String> sessionPhishingProtectedAttributesList =
				new ArrayList<>(
					Arrays.asList(
						PropsValues.SESSION_PHISHING_PROTECTED_ATTRIBUTES));

			sessionPhishingProtectedAttributesList.add(_VALIDATED);

			PropsValues.SESSION_PHISHING_PROTECTED_ATTRIBUTES =
				sessionPhishingProtectedAttributesList.toArray(new String[0]);
		}

		_serviceRegistration = bundleContext.registerService(
			new String[] {
				BrowserMFAChecker.class.getName(),
				SetupMFAChecker.class.getName()
			},
			this, new HashMapDictionary<>(properties));
	}

	@Deactivate
	protected void deactivate() {
		if (_serviceRegistration == null) {
			return;
		}

		_serviceRegistration.unregister();

		if (PropsValues.SESSION_ENABLE_PHISHING_PROTECTION) {
			List<String> sessionPhishingProtectedAttributesList =
				new ArrayList<>(
					Arrays.asList(
						PropsValues.SESSION_PHISHING_PROTECTED_ATTRIBUTES));

			sessionPhishingProtectedAttributesList.remove(_VALIDATED);

			PropsValues.SESSION_PHISHING_PROTECTED_ATTRIBUTES =
				sessionPhishingProtectedAttributesList.toArray(new String[0]);
		}
	}

	protected boolean isVerified(HttpSession httpSession, long userId) {
		User user = _userLocalService.fetchUser(userId);

		if (user == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Requested one-time password email verification for " +
						"nonexistent user " + userId);
			}

			_routeAuditMessage(
				_mfaTimeBasedOTPAuditMessageBuilder.
					buildNonexistentUserVerificationFailureAuditMessage(
						CompanyThreadLocal.getCompanyId(), userId,
						_getClassName()));

			return false;
		}

		if (httpSession == null) {
			_routeAuditMessage(
				_mfaTimeBasedOTPAuditMessageBuilder.
					buildNotVerifiedAuditMessage(
						user, _getClassName(), "Empty session"));

			return false;
		}

		Map<String, Object> validatedMap = _getValidatedMap(httpSession);

		if (validatedMap != null) {
			if (userId != MapUtil.getLong(validatedMap, "userId")) {
				_routeAuditMessage(
					_mfaTimeBasedOTPAuditMessageBuilder.
						buildNotVerifiedAuditMessage(
							user, _getClassName(), "Not the same user"));

				return false;
			}

			if (_validationExpirationTime < 0) {
				_routeAuditMessage(
					_mfaTimeBasedOTPAuditMessageBuilder.
						buildVerifiedAuditMessage(user, _getClassName()));

				return true;
			}

			long validatedAt = MapUtil.getLong(validatedMap, "validatedAt");

			if ((validatedAt + _validationExpirationTime * 1000) >
					System.currentTimeMillis()) {

				_routeAuditMessage(
					_mfaTimeBasedOTPAuditMessageBuilder.
						buildVerifiedAuditMessage(user, _getClassName()));

				return true;
			}
		}

		_routeAuditMessage(
			_mfaTimeBasedOTPAuditMessageBuilder.buildNotVerifiedAuditMessage(
				user, _getClassName(), "Expired verification"));

		return false;
	}

	protected boolean verifyTimeBasedOTP(
		String timeBasedOtpValue, long userId) {

		MFATimeBasedOTPEntry mfaTimeBasedOTPEntry =
			_mfaTimeBasedOTPEntryLocalService.fetchMFATimeBasedOTPEntryByUserId(
				userId);

		if (mfaTimeBasedOTPEntry != null) {
			try {
				return MFATimeBasedOTPUtil.verifyTimeBasedOTP(
					_clockSkew, mfaTimeBasedOTPEntry.getSharedSecret(),
					timeBasedOtpValue);
			}
			catch (Exception exception) {
				_log.error(
					StringBundler.concat(
						"Unable to generate TimeBased One-Time password for",
						"user ", userId, ": ", exception.getMessage()),
					exception);

				return false;
			}
		}

		return false;
	}

	private String _getClassName() {
		Class<?> clazz = getClass();

		return clazz.getName();
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> _getValidatedMap(HttpSession httpSession) {
		return (Map<String, Object>)httpSession.getAttribute(_VALIDATED);
	}

	private void _routeAuditMessage(AuditMessage auditMessage) {
		if (_mfaTimeBasedOTPAuditMessageBuilder != null) {
			_mfaTimeBasedOTPAuditMessageBuilder.routeAuditMessage(auditMessage);
		}
	}

	private static final String _VALIDATED =
		TimeBasedOTPMFAChecker.class.getName() + "#VALIDATED";

	private static final Log _log = LogFactoryUtil.getLog(
		TimeBasedOTPMFAChecker.class);

	@Reference
	private AbsolutePortalURLBuilderFactory _absolutePortalURLBuilderFactory;

	private int _algorithmKeySize;
	private long _clockSkew;

	@Reference(cardinality = ReferenceCardinality.OPTIONAL)
	private MFATimeBasedOTPAuditMessageBuilder
		_mfaTimeBasedOTPAuditMessageBuilder;

	@Reference
	private MFATimeBasedOTPEntryLocalService _mfaTimeBasedOTPEntryLocalService;

	@Reference
	private Portal _portal;

	private ServiceRegistration<?> _serviceRegistration;

	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.multi.factor.authentication.timebased.otp.web)"
	)
	private ServletContext _servletContext;

	@Reference
	private UserLocalService _userLocalService;

	private long _validationExpirationTime;

}
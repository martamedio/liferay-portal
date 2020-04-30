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

import com.liferay.multi.factor.authentication.timebased.otp.model.MFATimebasedOTPEntry;
import com.liferay.multi.factor.authentication.timebased.otp.service.MFATimebasedOTPEntryLocalService;
import com.liferay.multi.factor.authentication.timebased.otp.web.internal.audit.MFATimebasedOTPAuditMessageBuilder;
import com.liferay.multi.factor.authentication.timebased.otp.web.internal.configuration.MFATimebasedOTPConfiguration;
import com.liferay.multi.factor.authentication.timebased.otp.web.internal.util.MFATimebasedOTPUtil;
import com.liferay.multi.factor.authentication.verifier.spi.checker.MFABrowserChecker;
import com.liferay.multi.factor.authentication.verifier.spi.checker.MFASetupChecker;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.audit.AuditMessage;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.io.BigEndianCodec;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.SecureRandomUtil;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.url.builder.AbsolutePortalURLBuilder;
import com.liferay.portal.url.builder.AbsolutePortalURLBuilderFactory;
import com.liferay.portal.url.builder.ModuleAbsolutePortalURLBuilder;
import com.liferay.portal.util.PropsValues;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jodd.util.Base32;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

/**
 * @author Tomas Polesovsky
 * @author Marta Medio
 */
@Component(
	configurationPid = "com.liferay.multi.factor.authentication.timebased.otp.web.internal.configuration.MFATimebasedOTPConfiguration.scoped",
	configurationPolicy = ConfigurationPolicy.REQUIRE, immediate = true,
	service = {MFABrowserChecker.class, MFASetupChecker.class}
)
public class MFATimebasedOTPChecker
	implements MFABrowserChecker, MFASetupChecker {

	@Override
	public String getName() {
		return MFATimebasedOTPConfiguration.class.getName();
	}

	@Override
	public String getSetupLabelConfigurationKey(Locale locale) {
		ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
			"content.Language", locale, getClass());

		return LanguageUtil.get(
			resourceBundle, "mfa-timebased-otp-configuration-name");
	}

	@Override
	public void includeBrowserVerification(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, long userId)
		throws IOException {

		RequestDispatcher requestDispatcher =
			_servletContext.getRequestDispatcher("/verify_timebased_otp.jsp");

		try {
			requestDispatcher.include(httpServletRequest, httpServletResponse);
		}
		catch (ServletException servletException) {
			throw new IOException(
				"Unable to include /verify_timebased_otp.jsp: " +
					servletException,
				servletException);
		}
	}

	@Override
	public void includeSetup(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, long userId)
		throws IOException {

		MFATimebasedOTPEntry mfaTimebasedOTPEntry =
			_mfaTimebasedOTPEntryLocalService.fetchMFATimebasedOTPEntryByUserId(
				userId);

		try {
			if (mfaTimebasedOTPEntry != null) {
				RequestDispatcher requestDispatcher =
					_servletContext.getRequestDispatcher(
						"/setup_timebased_otp_completed.jsp");

				requestDispatcher.include(
					httpServletRequest, httpServletResponse);
			}
			else {
				String sharedSecret = _generateSharedSecret();

				httpServletRequest.setAttribute("sharedSecret", sharedSecret);

				HttpServletRequest originalHttpServletRequest =
					_portal.getOriginalServletRequest(httpServletRequest);

				HttpSession session = originalHttpServletRequest.getSession();

				session.setAttribute("sharedSecret", sharedSecret);

				Company company = _portal.getCompany(httpServletRequest);

				httpServletRequest.setAttribute(
					"companyName", company.getName());

				httpServletRequest.setAttribute(
					"mfaTimebasedOTPAlgorithm",
					MFATimebasedOTPUtil.MFA_TIMEBASED_OTP_ALGORITHM);
				httpServletRequest.setAttribute(
					"mfaTimebasedOTPDigits", _digitsCount);
				httpServletRequest.setAttribute(
					"mfaTimeBasedOTPTimeWindow", _timeWindow);

				httpServletRequest.setAttribute(
					"mfaUser", _userLocalService.fetchUserById(userId));

				httpServletRequest.setAttribute(
					"qrCodeLibraryUrl",
					_getQRCodeLibraryUrl(httpServletRequest));

				RequestDispatcher requestDispatcher =
					_servletContext.getRequestDispatcher(
						"/setup_timebased_otp.jsp");

				requestDispatcher.include(
					httpServletRequest, httpServletResponse);
			}
		}
		catch (PortalException portalException) {
			throw new IOException(
				"Unable to get Company: " + portalException, portalException);
		}
		catch (ServletException servletException) {
			throw new IOException(
				"Unable to include /setup_timebased_otp.jsp: " +
					servletException,
				servletException);
		}
	}

	@Override
	public boolean isBrowserVerified(
		HttpServletRequest httpServletRequest, long userId) {

		HttpServletRequest originalHttpServletRequest =
			_portal.getOriginalServletRequest(httpServletRequest);

		HttpSession session = originalHttpServletRequest.getSession(false);

		if (isVerified(session, userId)) {
			return true;
		}

		return false;
	}

	@Override
	public boolean isEnabled() {
		return _enabled;
	}

	@Override
	public boolean isUserSetupComplete(long userId) {
		return isUserSetUp(userId);
	}

	@Override
	public void removeExistingSetup(long userId) {
		MFATimebasedOTPEntry mfaTimebasedOTPEntry =
			_mfaTimebasedOTPEntryLocalService.fetchMFATimebasedOTPEntryByUserId(
				userId);

		if (mfaTimebasedOTPEntry != null) {
			_mfaTimebasedOTPEntryLocalService.deleteMFATimebasedOTPEntry(
				mfaTimebasedOTPEntry);
		}
	}

	@Override
	public boolean setUp(HttpServletRequest httpServletRequest, long userId) {
		HttpServletRequest originalHttpServletRequest =
			_portal.getOriginalServletRequest(httpServletRequest);

		HttpSession session = originalHttpServletRequest.getSession();

		String sharedSecret = (String)session.getAttribute("sharedSecret");

		String timebasedOtpValue = ParamUtil.getString(
			httpServletRequest, "timebasedOtp");

		try {
			if (MFATimebasedOTPUtil.verifyTimebasedOTP(
					Base32.decode(sharedSecret), timebasedOtpValue, _clockSkew,
					_timeWindow, _digitsCount)) {

				MFATimebasedOTPEntry timebasedOTPEntry =
					_mfaTimebasedOTPEntryLocalService.addTimebasedOTPEntry(
						sharedSecret, userId);

				if (timebasedOTPEntry != null) {
					return true;
				}
			}
		}
		catch (Exception exception) {
			_log.error(
				StringBundler.concat(
					"Unable to generate timebased one-time password for user ",
					userId, ": ", exception.getMessage()),
				exception);
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
					"Requested one-time password email verification for " +
						"nonexistent user " + userId);
			}

			_routeAuditMessage(
				_mfaTimebasedOTPAuditMessageBuilder.
					buildNonexistentUserVerificationFailureAuditMessage(
						CompanyThreadLocal.getCompanyId(), userId,
						_getClassName()));

			return false;
		}

		if (!isUserSetUp(user.getUserId())) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Requested timebased one time password for user" + userId +
						" with incomplete configuration");
			}

			_routeAuditMessage(
				_mfaTimebasedOTPAuditMessageBuilder.
					buildMissingSetupUserVerificationFailureAuditMessage(
						CompanyThreadLocal.getCompanyId(), user,
						_getClassName()));

			return false;
		}

		String timebasedOtp = ParamUtil.getString(
			httpServletRequest, "timebasedOtp");

		if (Validator.isBlank(timebasedOtp)) {
			return false;
		}

		boolean verified = verifyTimebasedOTP(timebasedOtp, user.getUserId());

		HttpServletRequest originalHttpServletRequest =
			_portal.getOriginalServletRequest(httpServletRequest);

		String userIP = originalHttpServletRequest.getRemoteAddr();

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

			_mfaTimebasedOTPEntryLocalService.updateAttempts(
				userId, userIP, true);

			_routeAuditMessage(
				_mfaTimebasedOTPAuditMessageBuilder.
					buildVerificationSuccessAuditMessage(
						user, _getClassName()));
		}
		else {
			_mfaTimebasedOTPEntryLocalService.updateAttempts(
				user.getUserId(), userIP, false);

			_routeAuditMessage(
				_mfaTimebasedOTPAuditMessageBuilder.
					buildVerificationFailureAuditMessage(
						user, _getClassName(),
						"Incorrect timebased one-time password"));
		}

		return verified;
	}

	@Activate
	@Modified
	protected void activate(Map<String, Object> properties) {
		MFATimebasedOTPConfiguration mfaTimebasedOTPConfiguration =
			ConfigurableUtil.createConfigurable(
				MFATimebasedOTPConfiguration.class, properties);

		_algorithmKeySize = mfaTimebasedOTPConfiguration.algorithmKeySize();
		_enabled = mfaTimebasedOTPConfiguration.enabled();
		_clockSkew = mfaTimebasedOTPConfiguration.clockSkew();
		_digitsCount = mfaTimebasedOTPConfiguration.digitsCount();
		_timeWindow = mfaTimebasedOTPConfiguration.timeWindow();
		_validationExpirationTime =
			mfaTimebasedOTPConfiguration.validationExpirationTime();

		if (PropsValues.SESSION_ENABLE_PHISHING_PROTECTION) {
			List<String> sessionPhishingProtectedAttributesList =
				new ArrayList<>(
					Arrays.asList(
						PropsValues.SESSION_PHISHING_PROTECTED_ATTRIBUTES));

			sessionPhishingProtectedAttributesList.add(_VALIDATED);

			PropsValues.SESSION_PHISHING_PROTECTED_ATTRIBUTES =
				sessionPhishingProtectedAttributesList.toArray(new String[0]);
		}
	}

	@Deactivate
	protected void deactivate() {
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

	protected boolean isUserSetUp(long userId) {
		MFATimebasedOTPEntry mfaTimebasedOTPEntry =
			_mfaTimebasedOTPEntryLocalService.fetchMFATimebasedOTPEntryByUserId(
				userId);

		if (_enabled && (mfaTimebasedOTPEntry != null)) {
			return true;
		}

		return false;
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
				_mfaTimebasedOTPAuditMessageBuilder.
					buildNonexistentUserVerificationFailureAuditMessage(
						CompanyThreadLocal.getCompanyId(), userId,
						_getClassName()));

			return false;
		}

		if (httpSession == null) {
			_routeAuditMessage(
				_mfaTimebasedOTPAuditMessageBuilder.
					buildNotVerifiedAuditMessage(
						user, _getClassName(), "Empty session"));

			return false;
		}

		Map<String, Object> validatedMap = _getValidatedMap(httpSession);

		if (validatedMap != null) {
			if (userId != MapUtil.getLong(validatedMap, "userId")) {
				_routeAuditMessage(
					_mfaTimebasedOTPAuditMessageBuilder.
						buildNotVerifiedAuditMessage(
							user, _getClassName(), "Not the same user"));

				return false;
			}

			if (_validationExpirationTime < 0) {
				_routeAuditMessage(
					_mfaTimebasedOTPAuditMessageBuilder.
						buildVerifiedAuditMessage(user, _getClassName()));

				return true;
			}

			long validatedAt = MapUtil.getLong(validatedMap, "validatedAt");

			if ((validatedAt + _validationExpirationTime * 1000) >
					System.currentTimeMillis()) {

				_routeAuditMessage(
					_mfaTimebasedOTPAuditMessageBuilder.
						buildVerifiedAuditMessage(user, _getClassName()));

				return true;
			}
		}

		_routeAuditMessage(
			_mfaTimebasedOTPAuditMessageBuilder.buildNotVerifiedAuditMessage(
				user, _getClassName(), "Expired verification"));

		return false;
	}

	protected boolean verifyTimebasedOTP(
		String timebasedOtpValue, long userId) {

		MFATimebasedOTPEntry mfaTimebasedOTPEntry =
			_mfaTimebasedOTPEntryLocalService.fetchMFATimebasedOTPEntryByUserId(
				userId);

		if (mfaTimebasedOTPEntry != null) {
			try {
				return MFATimebasedOTPUtil.verifyTimebasedOTP(
					Base32.decode(mfaTimebasedOTPEntry.getSharedSecret()),
					timebasedOtpValue, _clockSkew, _timeWindow, _digitsCount);
			}
			catch (Exception exception) {
				_log.error(
					StringBundler.concat(
						"Unable to generate Timebased One-Time password for",
						"user ", userId, ": ", exception.getMessage()),
					exception);

				return false;
			}
		}

		return false;
	}

	private String _generateSharedSecret() {
		int count = (int)Math.ceil((double)_algorithmKeySize / 8);

		byte[] buffer = new byte[count * 8];

		for (int i = 0; i < count; i++) {
			BigEndianCodec.putLong(buffer, i * 8, SecureRandomUtil.nextLong());
		}

		byte[] secret = new byte[_algorithmKeySize];

		System.arraycopy(buffer, 0, secret, 0, _algorithmKeySize);

		return Base32.encode(secret);
	}

	private String _getClassName() {
		Class<?> clazz = getClass();

		return clazz.getName();
	}

	private String _getQRCodeLibraryUrl(HttpServletRequest httpServletRequest) {
		Bundle bundle = FrameworkUtil.getBundle(
			MFATimebasedOTPConfiguration.class);

		AbsolutePortalURLBuilder absolutePortalURLBuilder =
			_absolutePortalURLBuilderFactory.getAbsolutePortalURLBuilder(
				httpServletRequest);

		ModuleAbsolutePortalURLBuilder moduleAbsolutePortalURLBuilder =
			absolutePortalURLBuilder.forModule(bundle, "/js/qrcode.min.js");

		return _portal.getPortalURL(httpServletRequest) +
			moduleAbsolutePortalURLBuilder.build();
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> _getValidatedMap(HttpSession httpSession) {
		return (Map<String, Object>)httpSession.getAttribute(_VALIDATED);
	}

	private void _routeAuditMessage(AuditMessage auditMessage) {
		if (_mfaTimebasedOTPAuditMessageBuilder != null) {
			_mfaTimebasedOTPAuditMessageBuilder.routeAuditMessage(auditMessage);
		}
	}

	private static final String _VALIDATED =
		MFATimebasedOTPChecker.class.getName() + "#VALIDATED";

	private static final Log _log = LogFactoryUtil.getLog(
		MFATimebasedOTPChecker.class);

	@Reference
	private AbsolutePortalURLBuilderFactory _absolutePortalURLBuilderFactory;

	private int _algorithmKeySize;
	private long _clockSkew;
	private int _digitsCount;
	private boolean _enabled;

	@Reference(cardinality = ReferenceCardinality.OPTIONAL)
	private MFATimebasedOTPAuditMessageBuilder
		_mfaTimebasedOTPAuditMessageBuilder;

	@Reference
	private MFATimebasedOTPEntryLocalService _mfaTimebasedOTPEntryLocalService;

	@Reference
	private Portal _portal;

	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.multi.factor.authentication.timebased.otp.web)"
	)
	private ServletContext _servletContext;

	private long _timeWindow = 30 * 1000;

	@Reference
	private UserLocalService _userLocalService;

	private long _validationExpirationTime;

}
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

package com.liferay.multi.factor.authentication.ip.otp.web.internal.checker;

import com.liferay.multi.factor.authentication.ip.otp.web.internal.audit.MFAIpOTPAuditMessageBuilder;
import com.liferay.multi.factor.authentication.ip.otp.web.internal.configuration.MFAIpOTPConfiguration;
import com.liferay.multi.factor.authentication.verifier.spi.checker.MFAHeadlessChecker;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.audit.AuditMessage;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.access.control.AccessControlUtil;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.service.UserLocalService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

/**
 * @author Marta Medio
 */
@Component(
	configurationPid = "com.liferay.multi.factor.authentication.ip.otp.web.internal.configuration.MFAIpOTPConfiguration.scoped",
	configurationPolicy = ConfigurationPolicy.OPTIONAL,
	service = MFAHeadlessChecker.class
)
public class MFAIpOTPChecker implements MFAHeadlessChecker {

	@Override
	public boolean isEnabled() {
		return _enabled;
	}

	@Override
	public boolean verifyHeadlessRequest(
		HttpServletRequest httpServletRequest, long userId) {

		User user = _userLocalService.fetchUser(userId);

		if (user == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Requested ip verification for nonexistent user " + userId);
			}

			_routeAuditMessage(
				_mfaIpOTPAuditMessageBuilder.
					buildNonexistentUserVerificationFailureAuditMessage(
						CompanyThreadLocal.getCompanyId(), userId,
						_getClassName()));

			return false;
		}

		boolean accessAllowed = AccessControlUtil.isAccessAllowed(
			httpServletRequest, _allowedIPsWithMasks);

		if (accessAllowed) {
			_routeAuditMessage(
				_mfaIpOTPAuditMessageBuilder.
					buildVerificationSuccessAuditMessage(
						user, _getClassName()));
		}
		else {
			_routeAuditMessage(
				_mfaIpOTPAuditMessageBuilder.
					buildVerificationFailureAuditMessage(
						user, _getClassName(), "Ip not allowed"));
		}

		return accessAllowed;
	}

	@Activate
	@Modified
	protected void activate(Map<String, Object> properties) {
		MFAIpOTPConfiguration mfaIpOTPConfiguration =
			ConfigurableUtil.createConfigurable(
				MFAIpOTPConfiguration.class, properties);

		_enabled = mfaIpOTPConfiguration.enabled();

		_allowedIPsWithMasks = new HashSet<>(
			Arrays.asList(mfaIpOTPConfiguration.allowedIPsWithMasks()));
	}

	private String _getClassName() {
		Class<?> clazz = getClass();

		return clazz.getName();
	}

	private void _routeAuditMessage(AuditMessage auditMessage) {
		if (_mfaIpOTPAuditMessageBuilder != null) {
			_mfaIpOTPAuditMessageBuilder.routeAuditMessage(auditMessage);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		MFAIpOTPChecker.class);

	private Set<String> _allowedIPsWithMasks;
	private boolean _enabled;

	@Reference(cardinality = ReferenceCardinality.OPTIONAL)
	private MFAIpOTPAuditMessageBuilder _mfaIpOTPAuditMessageBuilder;

	@Reference
	private UserLocalService _userLocalService;

}
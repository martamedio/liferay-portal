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

package com.liferay.multi.factor.authentication.email.otp.configuration;

import com.liferay.dynamic.data.mapping.annotations.DDMForm;
import com.liferay.dynamic.data.mapping.annotations.DDMFormField;
import com.liferay.dynamic.data.mapping.annotations.DDMFormLayout;
import com.liferay.dynamic.data.mapping.annotations.DDMFormLayoutColumn;
import com.liferay.dynamic.data.mapping.annotations.DDMFormLayoutPage;
import com.liferay.dynamic.data.mapping.annotations.DDMFormLayoutRow;
import com.liferay.portal.kernel.settings.LocalizedValuesMap;

/**
 * @author Marta Medio
 */
@DDMForm
@DDMFormLayout(
	paginationMode = com.liferay.dynamic.data.mapping.model.DDMFormLayout.TABBED_MODE,
	value = {
		@DDMFormLayoutPage(
			title = "%mfa-configuration-tab-name",
			value = {
				@DDMFormLayoutRow(
					{@DDMFormLayoutColumn(size = 12, value = "enabled")}
				)
			}
		),
		@DDMFormLayoutPage(
			title = "%mfa-email-otp-tab-name",
			value = {
				@DDMFormLayoutRow(
					{
						@DDMFormLayoutColumn(
							size = 12,
							value = {
								"service.ranking", "otpSize",
								"resendEmailTimeout", "emailFromAddress",
								"emailFromName", "emailOTPSentSubject",
								"emailOTPSentBody", "failedAttemptsAllowed",
								"retryTimeout"
							}
						)
					}
				)
			}
		)
	}
)
public interface MFAEmailOTPConfigurationForm {

	@DDMFormField(
		label = "%email-from-address", tip = "%email-from-address-description"
	)
	public String emailFromAddress();

	@DDMFormField(label = "%email-from-name")
	public String emailFromName();

	@DDMFormField(label = "%email-otp-sent-body")
	public LocalizedValuesMap emailOTPSentBody();

	@DDMFormField(label = "%email-otp-sent-subject")
	public LocalizedValuesMap emailOTPSentSubject();

	@DDMFormField(
		label = "%enabled", tip = "%mfa-email-otp-enabled-description"
	)
	public boolean enabled();

	@DDMFormField(
		label = "%failed-attempts-allowed",
		tip = "%failed-attempts-allowed-description"
	)
	public int failedAttemptsAllowed();

	@DDMFormField(label = "%order", tip = "%order-description")
	public int order();

	@DDMFormField(label = "%otp-size", tip = "%otp-size-description")
	public int otpSize();

	@DDMFormField(
		label = "%resend-email-timeout",
		tip = "%resend-email-timeout-description"
	)
	public long resendEmailTimeout();

	@DDMFormField(label = "%retry-timeout", tip = "%retry-timeout-description")
	public long retryTimeout();

}
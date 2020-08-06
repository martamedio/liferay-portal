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

package com.liferay.multi.factor.authentication.web.internal.configuration.admin.display;

import com.liferay.configuration.admin.definition.ConfigurationDDMFormDeclaration;
import com.liferay.multi.factor.authentication.email.otp.configuration.MFAEmailOTPConfigurationForm;

import org.osgi.service.component.annotations.Component;

/**
 * @author Marta Medio
 */
@Component(
	immediate = true,
	property = "configurationPid=com.liferay.multi.factor.authentication.email.otp.configuration.MFAEmailOTPConfiguration",
	service = ConfigurationDDMFormDeclaration.class
)
public class MFAEmailOTPFormDeclaration
	implements ConfigurationDDMFormDeclaration {

	@Override
	public Class<?> getDDMFormClass() {
		return MFAEmailOTPConfigurationForm.class;
	}

}
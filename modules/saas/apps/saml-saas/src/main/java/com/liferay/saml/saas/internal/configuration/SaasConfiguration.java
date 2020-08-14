/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.saml.saas.internal.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Marta Medio
 */
@ExtendedObjectClassDefinition(
	generateUI = false, scope = ExtendedObjectClassDefinition.Scope.COMPANY
)
@Meta.OCD(id = "com.liferay.saas.configuration.SaasConfiguration")
public interface SaasConfiguration {

	@Meta.AD(
		deflt = "false", id = "saas.production.environment", required = false
	)
	public boolean productionEnvironment();

	@Meta.AD(id = "saas.pre.shared.key", required = false)
	public String preSharedKey();

	@Meta.AD(id = "saas.target.instance.import.url", required = false)
	public String targetInstanceImportURL();

}
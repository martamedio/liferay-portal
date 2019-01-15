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

package com.liferay.oauth2.provider.rest.internal.cors.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Tomas Polesovsky
 */
@ExtendedObjectClassDefinition(
	category = "oauth2", factoryInstanceLabelAttribute = "name",
	scope = ExtendedObjectClassDefinition.Scope.SYSTEM
)
@Meta.OCD(
	description = "oauth2-cors-configuration-description", factory = true,
	id = "com.liferay.oauth2.provider.rest.internal.cors.configuration.OAuth2CORSConfiguration",
	localization = "content/Language", name = "oauth2-cors-configuration"
)
public interface OAuth2CORSConfiguration {

	@Meta.AD(deflt = "true", name = "enabled", required = false)
	public boolean enabled();

	@Meta.AD(
		description = "oauth2-cors-configuration-name-description",
		name = "oauth2-cors-configuration-name", required = false
	)
	public String name();

	@Meta.AD(
		deflt = "/documents/*|/image/*|/api/jsonws/*|/o/api/*",
		description = "oauth2-cors-configuration-filter-mapping-url-pattern-description",
		name = "oauth2-cors-configuration-filter-mapping-url-pattern",
		required = false
	)
	public String[] filterMappingURLPatterns();

	@Meta.AD(
		deflt = "Access-Control-Allow-Credentials: true|Access-Control-Allow-Headers: *|Access-Control-Allow-Methods: *",
		description = "oauth2-cors-configuration-cors-headers-description",
		name = "oauth2-cors-configuration-cors-headers", required = false
	)
	public String[] corsHeaders();

}
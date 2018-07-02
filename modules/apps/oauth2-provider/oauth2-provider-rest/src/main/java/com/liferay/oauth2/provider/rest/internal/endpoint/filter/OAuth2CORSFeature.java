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

package com.liferay.oauth2.provider.rest.internal.endpoint.filter;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Marta Medio
 */
@Component(
	property = {
		"osgi.jaxrs.application.select=(!(liferay.cors=false))",
		"osgi.jaxrs.extension=true",
		"osgi.jaxrs.extension.select=(osgi.jaxrs.name=Liferay.OAuth2)",
		"osgi.jaxrs.name=OAuth2CORSFeature"
	},
	scope = ServiceScope.PROTOTYPE
)
@Provider
public class OAuth2CORSFeature implements Feature {

	@Override
	public boolean configure(FeatureContext context) {
		Map<Class<?>, Integer> contracts = new HashMap<>();

		contracts.put(
			ContainerResponseFilter.class, Priorities.HEADER_DECORATOR);

		context.register(new OAuth2CORSResponseFilter(), contracts);

		contracts = new HashMap<>();

		contracts.put(
			ContainerRequestFilter.class, Priorities.HEADER_DECORATOR);

		context.register(new OAuth2CORSRequestFilter(), contracts);

		return true;
	}

}
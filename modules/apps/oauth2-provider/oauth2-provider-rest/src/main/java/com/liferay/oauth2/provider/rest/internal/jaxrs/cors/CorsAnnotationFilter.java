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
 
package com.liferay.oauth2.provider.rest.internal.jaxrs.cors;

import com.liferay.oauth2.provider.rest.internal.cors.CORSSupport;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
* @author Carlos Sierra Andrés
*/
@Component(
	property = {
		"osgi.jaxrs.extension=true",
		"osgi.jaxrs.application.select=(liferay.cors.annotation=true)",
		"osgi.jaxrs.name=Liferay.CORS.Annotation.Extension"
	},
	service = ContainerResponseFilter.class,
	scope = ServiceScope.PROTOTYPE
)
@CORS
public class CorsAnnotationFilter implements
	ContainerRequestFilter, ContainerResponseFilter {

	public void filter(
		ContainerRequestContext requestContext,
		ContainerResponseContext responseContext) throws IOException {

		CORSSupport corsSupport = _getCorsSupport();

		if (corsSupport == null) {
			return;
		}

		MultivaluedMap<String, String> requestHeaders =
			requestContext.getHeaders();
		MultivaluedMap<String, String> responseHeaders =
			responseContext.getStringHeaders();

		if (corsSupport.isCORSRequest(requestHeaders::getFirst)) {
			if (corsSupport.isValidCORSRequest(
				requestContext.getMethod(), requestHeaders::getFirst)) {

				corsSupport.writeResponseHeaders(
					requestHeaders::getFirst, responseHeaders::addFirst);
			}
		}
	}

	private CORSSupport _getCorsSupport() {
		Method resourceMethod = _resourceInfo.getResourceMethod();

		if (resourceMethod == null) {
			return null;
		}

		CORS cors = resourceMethod.getAnnotation(CORS.class);

		Map<String, String> corsHeaders = new HashMap<>();

		corsHeaders.put(
			CORSSupport.ACCESS_CONTROL_ALLOW_ORIGIN, cors.allowOrigin());
		corsHeaders.put(
			CORSSupport.ACCESS_CONTROL_ALLOW_CREDENTIALS,
			String.valueOf(cors.allowCredentials()));
		corsHeaders.put(
			CORSSupport.ACCESS_CONTROL_ALLOW_HEADERS,
			StringUtil.merge(cors.allowHeaders(), StringPool.COMMA));
		corsHeaders.put(
			CORSSupport.ACCESS_CONTROL_ALLOW_METHODS,
			StringUtil.merge(cors.allowMethods(), StringPool.COMMA));

		CORSSupport corsSupport = new CORSSupport();
		corsSupport.setHeaders(corsHeaders);

		return corsSupport;
	}

	@Override
	public void filter(ContainerRequestContext requestContext)
		throws IOException {

		CORSSupport corsSupport = _getCorsSupport();

		if (corsSupport == null) {
			return;
		}

		MultivaluedMap<String, String> requestHeaders =
			requestContext.getHeaders();

		if (corsSupport.isCORSRequest(requestHeaders::getFirst)) {
			if (StringUtil.equals(requestContext.getMethod(), "OPTIONS")) {
				if (corsSupport.isValidCORSPreflightRequest(
					requestHeaders::getFirst)) {

					Response response = Response.ok().build();

					MultivaluedMap<String, String> responseHeaders =
						response.getStringHeaders();

					corsSupport.writeResponseHeaders(
						requestHeaders::getFirst, responseHeaders::addFirst);

					requestContext.abortWith(response);
				}
			}
		}
	}

	@Context
	private ResourceInfo _resourceInfo;
}
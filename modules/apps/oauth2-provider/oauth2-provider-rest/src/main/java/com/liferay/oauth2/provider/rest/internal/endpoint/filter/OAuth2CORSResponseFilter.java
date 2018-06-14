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

import com.liferay.oauth2.provider.model.OAuth2Application;
import com.liferay.oauth2.provider.rest.spi.bearer.token.provider.BearerTokenProvider;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.access.control.AccessControlUtil;
import com.liferay.portal.kernel.security.auth.AccessControlContext;
import com.liferay.portal.kernel.security.auth.verifier.AuthVerifierResult;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import org.osgi.service.component.annotations.Component;

/**
 * @author Marta Medio
 */
@Component(
	immediate = true,
	property = {
		"osgi.jaxrs.extension=true",
		"osgi.jaxrs.extension.select=(osgi.jaxrs.name=Liferay.OAuth2)",
		"osgi.jaxrs.name=OAuth2CORSResponseFilter"
	}
)
@Provider
public class OAuth2CORSResponseFilter implements ContainerResponseFilter {

	@Override
	public void filter(
		ContainerRequestContext requestContext,
		ContainerResponseContext responseContext) {

		String origin = requestContext.getHeaderString("Origin");

		if (Validator.isBlank(origin)) {
			return;
		}

		URI originURI = null;
		try {
			originURI = new URI(origin);
		}
		catch (URISyntaxException urise) {
			if (_log.isDebugEnabled()) {
				_log.debug("Invalid origin: " + origin, urise);
			}

			responseContext.setEntity(null);
			responseContext.setStatusInfo(Response.Status.FORBIDDEN);

			return;
		}

		OAuth2Application oAuth2Application = getOAuth2Application();

		if (oAuth2Application == null) {
			responseContext.setStatus(403);
		}

		List<String> redirectURIsList = oAuth2Application.getRedirectURIsList();

		boolean originAllowed = false;

		for (String redirectURI : redirectURIsList) {
			try {
				URI uri = new URI(redirectURI);

				if (originURI.equals(uri)){
					originAllowed = true;

					break;
				}
			}
			catch (URISyntaxException urise) {
				if (_log.isDebugEnabled()) {
					_log.debug("Invalid redirectURI: " + redirectURI, urise);
				}
			}
		}

		if (originAllowed) {
			MultivaluedMap<String, Object> headers =
				responseContext.getHeaders();

			headers.putSingle("Access-Control-Allow-Origin", origin);
			headers.putSingle("Access-Control-Allow-Headers", "*");
		}
		else {
			responseContext.setEntity(null);
			responseContext.setStatusInfo(Response.Status.FORBIDDEN);
		}
	}

	protected OAuth2Application getOAuth2Application() {
		AccessControlContext accessControlContext =
			AccessControlUtil.getAccessControlContext();

		AuthVerifierResult authVerifierResult =
			accessControlContext.getAuthVerifierResult();

		Map<String, Object> settings = authVerifierResult.getSettings();

		Object accessTokenObject = settings.get(
			BearerTokenProvider.AccessToken.class.getName());

		if (accessTokenObject == null) {
			return null;
		}

		BearerTokenProvider.AccessToken accessToken =
			(BearerTokenProvider.AccessToken)accessTokenObject;

		return accessToken.getOAuth2Application();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		OAuth2CORSResponseFilter.class);

}
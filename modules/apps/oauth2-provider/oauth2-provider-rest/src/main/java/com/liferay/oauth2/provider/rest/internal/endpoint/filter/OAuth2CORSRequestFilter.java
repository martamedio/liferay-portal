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
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.access.control.AccessControlUtil;
import com.liferay.portal.kernel.security.auth.AccessControlContext;
import com.liferay.portal.kernel.security.auth.verifier.AuthVerifierResult;
import com.liferay.portal.kernel.servlet.HttpMethods;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.IOException;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Map;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;

/**
 * @author Marta Medio
 */
public class OAuth2CORSRequestFilter implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext requestContext)
		throws IOException {

		String method = requestContext.getMethod();

		if (StringUtil.equals(method, HttpMethods.OPTIONS)) {
			if (!_isValidPreflight(requestContext)) {
				return;
			}
		}
		else {
			OAuth2Application oAuth2Application = getOAuth2Application();

			if (oAuth2Application == null) {
				if (_log.isDebugEnabled()) {
					_log.debug("Unable to find OAuth2 application");
				}

				return;
			}

			String origin = requestContext.getHeaderString("Origin");

			boolean originAllowed = false;

			List<String> redirectURIsList =
				oAuth2Application.getRedirectURIsList();

			for (String redirectURI : redirectURIsList) {
				try {
					URI originUri = new URI(origin);
					URI uri = new URI(redirectURI);

					String originHost = originUri.getHost();
					String uriHost = uri.getHost();

					if (originHost.equals(uriHost)) {
						originAllowed = true;

						break;
					}
				}
				catch (URISyntaxException urise) {
					if (_log.isDebugEnabled()) {
						_log.debug(
							StringBundler.concat(
								"Invalid client ",
								oAuth2Application.getClientId(),
								" redirectURI ", redirectURI),
							urise);
					}
				}
			}

			if (!originAllowed) {
				if (_log.isDebugEnabled()) {
					_log.debug(
						StringBundler.concat(
							"CORS was disallowed for client ",
							oAuth2Application.getClientId(), " and origin: ",
							origin));
				}

				return;
			}
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

	private boolean _isValidPreflight(ContainerRequestContext requestContext) {
		String accessControlRequestMethod = requestContext.getHeaderString(
			"Access-Control-Request-Method");

		String origin = requestContext.getHeaderString("Origin");

		if (Validator.isBlank(accessControlRequestMethod) ||
			Validator.isBlank(origin)) {

			if (_log.isDebugEnabled()) {
				_log.debug("Invalid preflight sent by browser");
			}

			return false;
		}

		return true;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		OAuth2CORSRequestFilter.class);

}
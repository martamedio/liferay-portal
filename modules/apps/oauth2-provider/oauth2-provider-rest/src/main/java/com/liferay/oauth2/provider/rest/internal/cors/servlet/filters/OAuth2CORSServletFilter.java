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

package com.liferay.oauth2.provider.rest.internal.cors.servlet.filters;

import com.liferay.oauth2.provider.model.OAuth2Application;
import com.liferay.oauth2.provider.rest.spi.bearer.token.provider.BearerTokenProvider;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.access.control.AccessControlUtil;
import com.liferay.portal.kernel.security.auth.AccessControlContext;
import com.liferay.portal.kernel.security.auth.verifier.AuthVerifierResult;

import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Tomas Polesovsky
 */
public class OAuth2CORSServletFilter extends OAuth2CORSServletBaseFilter {

	@Override
	protected Log getLog() {
		return _log;
	}

	protected OAuth2Application getOAuth2Application() {
		AccessControlContext accessControlContext =
			AccessControlUtil.getAccessControlContext();

		if (accessControlContext == null) {
			return null;
		}

		AuthVerifierResult authVerifierResult =
			accessControlContext.getAuthVerifierResult();

		if (authVerifierResult == null) {
			return null;
		}

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

	@Override
	protected void processFilter(
			HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain)
		throws Exception {

		OAuth2Application oAuth2Application = getOAuth2Application();

		if (oAuth2Application == null) {
			super.processFilter(request, response, filterChain);

			return;
		}

		if (!corsSupport.isValidCORSRequest(
				name -> request.getHeader(name),
				oAuth2Application.getRedirectURIsList())) {

			return;
		}

		corsSupport.writeResponseHeaders(
			name -> request.getHeader(name),
			(name, value) -> response.setHeader(name, value));

		super.processFilter(request, response, filterChain);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		OAuth2CORSServletFilter.class);

}
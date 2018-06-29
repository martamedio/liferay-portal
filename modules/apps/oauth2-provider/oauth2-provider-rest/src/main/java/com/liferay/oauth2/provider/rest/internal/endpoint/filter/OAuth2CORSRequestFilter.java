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

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.HttpMethods;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;

/**
 * @author Marta Medio
 */
public class OAuth2CORSRequestFilter implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext requestContext)
		throws IOException {

		String origin = requestContext.getHeaderString("Origin");

		if (Validator.isBlank(origin)) {
			if (_log.isDebugEnabled()) {
				_log.debug("Invalid origin sent by browser");
			}

			return;
		}

		String accessControlRequestMethod = requestContext.getHeaderString(
			"Access-Control-Request-Method");

		String method = requestContext.getMethod();

		if (StringUtil.equals(method, HttpMethods.OPTIONS) &&
			Validator.isBlank(accessControlRequestMethod)) {

			if (_log.isDebugEnabled()) {
				_log.debug("Invalid preflight sent by browser");
			}

			return;
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		OAuth2CORSRequestFilter.class);

}
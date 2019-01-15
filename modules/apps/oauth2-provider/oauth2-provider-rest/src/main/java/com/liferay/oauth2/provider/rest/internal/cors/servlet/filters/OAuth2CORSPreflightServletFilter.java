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

import com.liferay.oauth2.provider.rest.internal.cors.CORSSupport;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.BaseFilter;
import com.liferay.portal.kernel.servlet.HttpMethods;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Tomas Polesovsky
 */
public class OAuth2CORSPreflightServletFilter extends BaseFilter {

	@Override
	public boolean isFilterEnabled(
		HttpServletRequest request, HttpServletResponse response) {

		if (!StringUtil.equals(HttpMethods.OPTIONS, request.getMethod())) {
			return false;
		}

		if (Validator.isBlank(request.getHeader("Origin"))) {
			return false;
		}

		return super.isFilterEnabled(request, response);
	}

	public void setHeaders(Map<String, String> headers) {
		_corsSupport.setHeaders(headers);
	}

	@Override
	protected Log getLog() {
		return _log;
	}

	@Override
	protected void processFilter(
			HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain)
		throws Exception {

		if (_corsSupport.isValidCORSPreflightRequest(
				name -> request.getHeader(name))) {

			_corsSupport.writeResponseHeaders(
				name -> request.getHeader(name),
				(name, value) -> response.setHeader(name, value));

			return;
		}

		super.processFilter(request, response, filterChain);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		OAuth2CORSPreflightServletFilter.class);

	private final CORSSupport _corsSupport = new CORSSupport();

}
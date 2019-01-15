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

package com.liferay.oauth2.provider.rest.internal.cors;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author Tomas Polesovsky
 */
public class CORSSupport {

	public boolean isValidCORSPreflightRequest(
		Function<String, String> requestHeadersAccessor) {

		String origin = requestHeadersAccessor.apply("Origin");

		if (Validator.isBlank(origin)) {
			return false;
		}

		String accessControlRequestMethod = requestHeadersAccessor.apply(
			"Access-Control-Request-Method");

		if (Validator.isBlank(accessControlRequestMethod)) {
			return false;
		}

		String accessControlAllowedMethods = _headers.get(
			"Access-Control-Allow-Methods");

		if (!StringUtil.equals(accessControlAllowedMethods, StringPool.STAR)) {
			if (!ArrayUtil.contains(
					StringUtil.split(accessControlAllowedMethods),
					accessControlRequestMethod)) {

				return false;
			}
		}

		return true;
	}

	public boolean isValidCORSRequest(
		Function<String, String> requestHeadersAccessor,
		List<String> redirectURIs) {

		String origin = requestHeadersAccessor.apply("Origin");

		if (Validator.isBlank(origin)) {
			return false;
		}

		URI originURI = null;

		try {
			originURI = new URI(origin);
		}
		catch (URISyntaxException urise) {
			if (_log.isDebugEnabled()) {
				_log.debug("Invalid origin URI: " + origin);
			}

			return false;
		}

		String originHost = originURI.getHost();

		for (String redirect : redirectURIs) {
			try {
				URI redirectURI = new URI(redirect);

				String redirectHost = redirectURI.getHost();

				if (!StringUtil.equalsIgnoreCase(originHost, redirectHost)) {
					continue;
				}

				return true;
			}
			catch (URISyntaxException urise) {
				if (_log.isDebugEnabled()) {
					_log.debug("Invalid redirect uri " + redirect, urise);
				}
			}
		}

		return false;
	}

	public void setHeader(String key, String value) {
		_headers.put(key, value);
	}

	public void setHeaders(Map<String, String> headers) {
		_headers.putAll(headers);
	}

	public void writeResponseHeaders(
		Function<String, String> requestHeadersAccessor,
		BiConsumer<String, String> responseHeadersConsumer) {

		String origin = requestHeadersAccessor.apply("Origin");

		responseHeadersConsumer.accept("Access-Control-Allow-Origin", origin);

		for (Map.Entry<String, String> entry : _headers.entrySet()) {
			responseHeadersConsumer.accept(entry.getKey(), entry.getValue());
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(CORSSupport.class);

	private final Map<String, String> _headers = new HashMap<>();

}
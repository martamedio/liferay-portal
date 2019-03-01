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

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author Tomas Polesovsky
 */
public class CORSSupport {

	public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS =
		"Access-Control-Allow-Credentials";

	public static final String ACCESS_CONTROL_ALLOW_HEADERS =
		"Access-Control-Allow-Headers";

	public static final String ACCESS_CONTROL_ALLOW_METHODS =
		"Access-Control-Allow-Methods";

	public static final String ACCESS_CONTROL_ALLOW_ORIGIN =
		"Access-Control-Allow-Origin";

	public static final String ACCESS_CONTROL_REQUEST_HEADERS =
		"Access-Control-Request-Headers";

	public static final String ACCESS_CONTROL_REQUEST_METHOD =
		"Access-Control-Request-Method";

	public static final String ORIGIN = "Origin";

	public boolean isCORSRequest(
		Function<String, String> requestHeaderAccessorFunction) {

		String origin = requestHeaderAccessorFunction.apply(ORIGIN);

		if (Validator.isBlank(origin)) {
			return false;
		}

		return true;
	}

	public boolean isValidCORSPreflightRequest(
		Function<String, String> requestHeaderAccessorFunction) {

		String origin = requestHeaderAccessorFunction.apply(ORIGIN);

		if (!isValidOrigin(origin)) {
			return false;
		}

		String accessControlRequestMethod = requestHeaderAccessorFunction.apply(
			ACCESS_CONTROL_REQUEST_METHOD);

		if (Validator.isBlank(accessControlRequestMethod)) {
			return false;
		}

		String accessControlAllowedMethods = _headers.get(
			ACCESS_CONTROL_ALLOW_METHODS);

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
		String httpMethod,
		Function<String, String> requestHeaderAccessorFunction) {

		if (!isValidOrigin(requestHeaderAccessorFunction.apply(ORIGIN))) {
			return false;
		}

		String accessControlAllowedMethods = _headers.get(
			ACCESS_CONTROL_ALLOW_METHODS);

		if (!StringUtil.equals(accessControlAllowedMethods, StringPool.STAR)) {
			if (!ArrayUtil.contains(
					StringUtil.split(accessControlAllowedMethods),
					httpMethod)) {

				return false;
			}
		}

		return true;
	}

	public boolean isValidOrigin(String origin) {
		if (Validator.isBlank(origin)) {
			return false;
		}

		String accessControlAllowOrigin = _headers.get(
			ACCESS_CONTROL_ALLOW_ORIGIN);

		if (Validator.isBlank(accessControlAllowOrigin)) {
			return true;
		}

		if (StringUtil.equals(accessControlAllowOrigin, StringPool.STAR)) {
			return true;
		}

		if (ArrayUtil.contains(
				StringUtil.split(accessControlAllowOrigin), origin)) {

			return true;
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

		String origin = requestHeadersAccessor.apply(ORIGIN);

		responseHeadersConsumer.accept(ACCESS_CONTROL_ALLOW_ORIGIN, origin);

		for (Map.Entry<String, String> entry : _headers.entrySet()) {
			String key = entry.getKey();

			if (StringUtil.equals(key, ACCESS_CONTROL_ALLOW_ORIGIN)) {
				continue;
			}

			responseHeadersConsumer.accept(key, entry.getValue());
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(CORSSupport.class);

	private final Map<String, String> _headers = new HashMap<>();

}
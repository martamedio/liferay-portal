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

package com.liferay.portal.security.sso.opensso.internal;

import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.sso.OpenSSO;
import com.liferay.portal.kernel.util.CookieKeys;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.URLCodec;
import com.liferay.portal.util.HttpImpl;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;

/**
 * @author Marta Medio
 */
@Component(
	immediate = true, property = "version=openam-13", service = OpenSSO.class
)
public class OpenSSOV13Impl extends OpenSSOImpl {

	@Override
	public boolean isAuthenticated(
			HttpServletRequest request, String serviceUrl)
		throws IOException {

		boolean authenticated = false;

		boolean hasCookieNames = false;

		String[] cookieNames = getCookieNames(serviceUrl);

		for (String cookieName : cookieNames) {
			if (CookieKeys.getCookie(request, cookieName) != null) {
				hasCookieNames = true;

				break;
			}
		}

		if (!hasCookieNames) {
			if (_log.isInfoEnabled()) {
				_log.info(
					"User is not logged in because he has no OpenSSO cookies");
			}

			return false;
		}

		String subjectId = getSubjectId(request, serviceUrl);

		if (subjectId != null) {
			String validateTokenUrl = StringUtil.replace(
				_VALIDATE_TOKEN, "{#subjectId}", URLCodec.encodeURL(subjectId));

			String url = serviceUrl.concat(validateTokenUrl);

			HttpImpl httpImpl = new HttpImpl();

			String result = httpImpl.URLtoString(url, true);

			try {
				JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
					result);

				Boolean valid = (Boolean)jsonObject.get("valid");
				String uid = (String)jsonObject.get("uid");
				String realm = (String)jsonObject.get("realm");

				if ((realm != null) && (uid != null) && valid) {
					authenticated = true;
				}
				else if (_log.isDebugEnabled()) {
					_log.debug("Invalid authentication: " + result);
				}
			}
			catch (JSONException jsone) {
				throw new IOException(jsone);
			}
		}

		return authenticated;
	}

	private static final String _VALIDATE_TOKEN =
		"/json/sessions/{#subjectId}?_action=validate";

	private static final Log _log = LogFactoryUtil.getLog(OpenSSOV13Impl.class);

}
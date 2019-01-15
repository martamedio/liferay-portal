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

import com.liferay.oauth2.provider.rest.internal.cors.configuration.OAuth2CORSConfiguration;
import com.liferay.oauth2.provider.rest.internal.cors.servlet.filters.OAuth2CORSPreflightServletFilter;
import com.liferay.oauth2.provider.rest.internal.cors.servlet.filters.OAuth2CORSServletFilter;
import com.liferay.petra.string.CharPool;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.StringUtil;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;

/**
 * @author Tomas Polesovsky
 */
@Component(
	configurationPid = "com.liferay.oauth2.provider.rest.internal.cors.configuration.OAuth2CORSConfiguration",
	configurationPolicy = ConfigurationPolicy.OPTIONAL, immediate = true,
	service = {}
)
public class OAuth2CORSFiltersPublisher {

	@Activate
	protected void activate(
		BundleContext bundleContext, Map<String, Object> properties) {

		_bundleContext = bundleContext;

		OAuth2CORSConfiguration oAuth2CORSConfiguration =
			ConfigurableUtil.createConfigurable(
				OAuth2CORSConfiguration.class, properties);

		if (!oAuth2CORSConfiguration.enabled()) {
			return;
		}

		Map<String, String> headers = new HashMap<>();

		for (String header : oAuth2CORSConfiguration.corsHeaders()) {
			int pos = header.indexOf(CharPool.COLON);

			if (pos < 1) {
				if (_log.isWarnEnabled()) {
					_log.warn(
						"Incorrect syntax of OAuth2CORSConfiguration header: " +
							header);
				}

				continue;
			}

			headers.put(
				StringUtil.trim(header.substring(0, pos)),
				StringUtil.trim(header.substring(pos + 1)));
		}

		OAuth2CORSPreflightServletFilter oAuth2CORSPreflightServletFilter =
			new OAuth2CORSPreflightServletFilter();

		oAuth2CORSPreflightServletFilter.setHeaders(headers);

		Dictionary<String, Object> filterProperties = new HashMapDictionary<>();

		filterProperties.put("before-filter", "Auto Login Filter");
		filterProperties.put("dispatcher", new String[] {"FORWARD", "REQUEST"});
		filterProperties.put("servlet-context-name", "");
		filterProperties.put(
			"servlet-filter-name",
			"OAuth2 CORS Preflight Servlet Filter for " +
				oAuth2CORSConfiguration.name());
		filterProperties.put(
			"url-pattern", oAuth2CORSConfiguration.filterMappingURLPatterns());

		_preflightFilterServiceRegistration = _bundleContext.registerService(
			Filter.class, oAuth2CORSPreflightServletFilter, filterProperties);

		OAuth2CORSServletFilter oAuth2CORSServletFilter =
			new OAuth2CORSServletFilter();

		oAuth2CORSServletFilter.setHeaders(headers);

		filterProperties = new HashMapDictionary<>();

		filterProperties.put("after-filter", "Upload Servlet Request Filter");
		filterProperties.put("dispatcher", new String[] {"FORWARD", "REQUEST"});
		filterProperties.put("servlet-context-name", "");
		filterProperties.put(
			"servlet-filter-name",
			"OAuth2 CORS Servlet Filter for " + oAuth2CORSConfiguration.name());
		filterProperties.put(
			"url-pattern", oAuth2CORSConfiguration.filterMappingURLPatterns());

		_corsFilterServiceRegistration = _bundleContext.registerService(
			Filter.class, oAuth2CORSServletFilter, filterProperties);
	}

	@Deactivate
	protected void deactivate() {
		if (_corsFilterServiceRegistration != null) {
			_corsFilterServiceRegistration.unregister();
		}

		if (_preflightFilterServiceRegistration != null) {
			_preflightFilterServiceRegistration.unregister();
		}

		_corsFilterServiceRegistration = null;
		_preflightFilterServiceRegistration = null;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		OAuth2CORSFiltersPublisher.class);

	private BundleContext _bundleContext;
	private ServiceRegistration<Filter> _corsFilterServiceRegistration;
	private ServiceRegistration<Filter> _preflightFilterServiceRegistration;

}
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

import com.liferay.oauth2.provider.rest.internal.cors.servlet.filters.CORSServletFilter;
import com.liferay.osgi.util.ServiceTrackerFactory;
import com.liferay.osgi.util.StringPlus;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.petra.string.StringUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.http.context.ServletContextHelper;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * @author Carlos Sierra Andrés
 * @author Marta Medio
 */
@Component(
	immediate = true,
	property = "servlet.context.helper.select.filter=(&(!(liferay.cors=false))(osgi.jaxrs.name=*))",
	service = {}
)
public class CORSFilterServletContextHelperTracker {

	private HashMap<String, String> _corsHeaders;
	private List<String> _corsAllowURIs;

	@Activate
	protected void activate(
		BundleContext bundleContext, Map<String, Object> properties) {

		_bundleContext = bundleContext;
		_corsAllowURIs = StringPlus.asList(properties.get("cors.allow.uri"));
		_corsHeaders = _buildCorsHeaders(properties);

		String servletContextHelperSelectFilterString = MapUtil.getString(
			properties, "servlet.context.helper.select.filter");

		String filterString = StringBundler.concat(
			"(&" + servletContextHelperSelectFilterString + "(",
			HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_NAME, "=*)",
			"(objectClass=", ServletContextHelper.class.getName(), "))");

		_serviceTracker = ServiceTrackerFactory.open(
			_bundleContext, filterString,
			new ServletContextHelperServiceTrackerCustomizer());
	}

	@Deactivate
	protected void deactivate() {
		_serviceTracker.close();
	}

	private BundleContext _bundleContext;
	private ServiceTracker<ServletContextHelper, ServiceRegistration<?>>
		_serviceTracker;

	private static HashMap<String, String> _buildCorsHeaders(
		Map<String, Object> properties) {

		HashMap<String, String> corsHeaders = new HashMap<>();

		boolean corsAllowCredentials = MapUtil.getBoolean(
			properties, "cors.allow.credentials");

		if (corsAllowCredentials) {
			corsHeaders.put(
				CORSSupport.ACCESS_CONTROL_ALLOW_CREDENTIALS,
				Boolean.TRUE.toString());
		}

		String corsAllowOrigin = MapUtil.getString(
			properties, "cors.allow.origin");

		if (Validator.isNotNull(corsAllowOrigin)) {
			corsHeaders.put(
				CORSSupport.ACCESS_CONTROL_ALLOW_ORIGIN, corsAllowOrigin);
		}

		String corsAllowHeaders = MapUtil.getString(
			properties, "cors.allow.headers");

		if (Validator.isNotNull(corsAllowHeaders)) {
			corsHeaders.put(
				CORSSupport.ACCESS_CONTROL_ALLOW_HEADERS, corsAllowHeaders);
		}

		String corsAllowMethods = MapUtil.getString(
			properties, "cors.allow.methods");

		if (Validator.isNotNull(corsAllowMethods)) {
			corsHeaders.put(
				CORSSupport.ACCESS_CONTROL_ALLOW_METHODS, corsAllowMethods);
		}

		return corsHeaders;
	}

	private class ServletContextHelperServiceTrackerCustomizer
		implements ServiceTrackerCustomizer
			<ServletContextHelper, ServiceRegistration<?>> {

		@Override
		public ServiceRegistration<?> addingService(
			ServiceReference<ServletContextHelper> serviceReference) {

			CORSServletFilter corsServletFilter = new CORSServletFilter();

			corsServletFilter.setHeaders(_corsHeaders);

			return _bundleContext.registerService(
				Filter.class, corsServletFilter,
				_buildProperties(serviceReference));
		}

		@Override
		public void modifiedService(
			ServiceReference<ServletContextHelper>
				servletContextHelperServiceReference,
			ServiceRegistration<?> serviceRegistration) {

			serviceRegistration.setProperties(
				_buildProperties(servletContextHelperServiceReference));
		}

		@Override
		public void removedService(
			ServiceReference<ServletContextHelper> reference,
			ServiceRegistration<?> serviceRegistration) {

			try {
				serviceRegistration.unregister();
			}
			catch (Exception e) {
			}
		}

		private Dictionary<String, Object> _buildProperties(
			ServiceReference<ServletContextHelper> serviceReference) {

			String contextName = GetterUtil.getString(
				serviceReference.getProperty(
					HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_NAME));

			Dictionary<String, Object> serviceProperties =
				new HashMapDictionary<>();

			serviceProperties.put(Constants.SERVICE_RANKING, -1);
			serviceProperties.put(
				HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_SELECT,
				contextName);
			serviceProperties.put(
				HttpWhiteboardConstants.HTTP_WHITEBOARD_FILTER_NAME,
				CORSServletFilter.class.getName());

			if (_corsAllowURIs.contains(StringPool.STAR)) {
				serviceProperties.put(
					HttpWhiteboardConstants.HTTP_WHITEBOARD_FILTER_SERVLET,
					"cxf-servlet");
			}
			else {
				serviceProperties.put(
					HttpWhiteboardConstants.HTTP_WHITEBOARD_FILTER_PATTERN,
					_corsAllowURIs.toArray(new String[0]));
			}

			return serviceProperties;
		}

	}

}
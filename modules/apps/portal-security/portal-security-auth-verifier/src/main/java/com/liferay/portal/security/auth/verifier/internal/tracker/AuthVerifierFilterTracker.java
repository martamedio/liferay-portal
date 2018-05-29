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

package com.liferay.portal.security.auth.verifier.internal.tracker;

import com.liferay.osgi.util.ServiceTrackerFactory;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.servlet.filters.authverifier.AuthVerifierFilter;

import java.util.Hashtable;

import javax.servlet.Filter;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
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
 * @author Marta Medio
 */
@Component(immediate = true)
public class AuthVerifierFilterTracker {

	@Activate
	protected void activate(final BundleContext bundleContext)
		throws InvalidSyntaxException {

		String filterString = StringBundler.concat(
			"(&(objectClass=", ServletContextHelper.class.getName(), ")",
			"(com.liferay.auth.verifier.filter.enabled=true))");

		_serviceTracker = ServiceTrackerFactory.open(
			bundleContext, filterString,
			new ServletContextAuthVerifierServiceTrackerCustomizer(
				bundleContext));

		_serviceTracker.open();
	}

	@Deactivate
	protected void deactivate() {
		_serviceTracker.close();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		AuthVerifierFilterTracker.class);

	private ServiceTracker<?, ?> _serviceTracker;

	private class ServletContextAuthVerifierServiceTrackerCustomizer
		implements
			ServiceTrackerCustomizer
				<ServletContextHelper, ServiceRegistration<?>> {

		public ServletContextAuthVerifierServiceTrackerCustomizer(
			BundleContext bundleContext) {

			_bundleContext = bundleContext;
		}

		@Override
		public ServiceRegistration<?> addingService(
			ServiceReference<ServletContextHelper> serviceReference) {

			return _bundleContext.registerService(
				Filter.class, new AuthVerifierFilter(),
				_buildProperties(serviceReference));
		}

		@Override
		public void modifiedService(
			ServiceReference<ServletContextHelper> serviceReference,
			ServiceRegistration<?> serviceRegistration) {

			serviceRegistration.setProperties(
				_buildProperties(serviceReference));
		}

		@Override
		public void removedService(
			ServiceReference<ServletContextHelper> serviceReference,
			ServiceRegistration<?> serviceRegistration) {

			serviceRegistration.unregister();

			_bundleContext.ungetService(serviceReference);
		}

		private Hashtable<String, Object> _buildProperties(
			ServiceReference<ServletContextHelper> serviceReference) {

			Hashtable<String, Object> properties = new Hashtable<>();

			for (String key : serviceReference.getPropertyKeys()) {
				if (!key.startsWith("osgi.http.whiteboard")) {
					properties.put(key, serviceReference.getProperty(key));
				}
			}

			Object contextName = serviceReference.getProperty(
				HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_NAME);

			String value = StringBundler.concat(
				"(", HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_NAME, "=",
				contextName.toString(), ")");

			properties.put(
				HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_SELECT, value);

			properties.put(
				HttpWhiteboardConstants.HTTP_WHITEBOARD_FILTER_NAME,
				AuthVerifierFilter.class.getName());

			properties.put(
				HttpWhiteboardConstants.HTTP_WHITEBOARD_FILTER_PATTERN, "/*");

			return properties;
		}

		private final BundleContext _bundleContext;

	}

}
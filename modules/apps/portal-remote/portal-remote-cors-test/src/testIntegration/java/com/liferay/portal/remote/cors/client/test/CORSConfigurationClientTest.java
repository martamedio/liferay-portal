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

package com.liferay.portal.remote.cors.client.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.remote.cors.configuration.WebContextCORSConfiguration;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.Dictionary;

import javax.ws.rs.HttpMethod;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Marta Medio
 */
@RunWith(Arquillian.class)
public class CORSConfigurationClientTest extends BaseCORSClientTestCase {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() {
		Dictionary<String, Object> properties = new HashMapDictionary<>();

		properties.put("osgi.jaxrs.name", "test-cors");

		registerJaxRsApplication(new CORSTestApplication(), "cors", properties);

		properties = new HashMapDictionary<>();

		properties.put(
			"servlet.context.helper.select.filter",
			"(osgi.jaxrs.name=test-cors)");

		createFactoryConfiguration(
			WebContextCORSConfiguration.class.getName(), properties);

		properties = new HashMapDictionary<>();

		properties.put("osgi.jaxrs.name", "test-cors-url");

		registerJaxRsApplication(
			new CORSTestApplication(), "test-cors-url", properties);

		properties = new HashMapDictionary<>();

		properties.put("filter.mapping.url.patterns", "/cors-app");
		properties.put(
			"servlet.context.helper.select.filter",
			"(osgi.jaxrs.name=test-cors-url)");

		createFactoryConfiguration(
			WebContextCORSConfiguration.class.getName(), properties);

		registerJaxRsApplication(
			new CORSTestApplication(), "no-cors", new HashMapDictionary<>());

		properties = new HashMapDictionary<>();

		properties.put("osgi.jaxrs.name", "test-cors-wrong-url");

		registerJaxRsApplication(
			new CORSTestApplication(), "test-cors-wrong-url", properties);

		properties = new HashMapDictionary<>();

		properties.put("filter.mapping.url.patterns", "/wrong");
		properties.put(
			"servlet.context.helper.select.filter",
			"(osgi.jaxrs.name=test-cors-wrong-url)");

		createFactoryConfiguration(
			WebContextCORSConfiguration.class.getName(), properties);
	}

	@Test
	public void testCORSApplication() throws Exception {
		assertJaxRSUrl("/cors/cors-app", HttpMethod.GET, true);
		assertJaxRSUrl("/no-cors/cors-app", HttpMethod.GET, false);
		assertJaxRSUrl("/test-cors-url/cors-app", HttpMethod.GET, true);
		assertJaxRSUrl("/test-cors-wrong-url/cors-app", HttpMethod.GET, false);
	}

	@Test
	public void testCORSPreflightApplication() throws Exception {
		assertJaxRSUrl("/cors/cors-app", HttpMethod.OPTIONS, true);
		assertJaxRSUrl("/no-cors/cors-app", HttpMethod.OPTIONS, false);
		assertJaxRSUrl("/test-cors-url/cors-app", HttpMethod.OPTIONS, true);
		assertJaxRSUrl(
			"/test-cors-wrong-url/cors-app", HttpMethod.OPTIONS, false);
	}

}
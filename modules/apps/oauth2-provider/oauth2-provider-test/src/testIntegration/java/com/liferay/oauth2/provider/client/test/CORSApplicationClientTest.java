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

package com.liferay.oauth2.provider.client.test;

import com.liferay.oauth2.provider.constants.GrantType;
import com.liferay.oauth2.provider.test.internal.TestApplication;
import com.liferay.oauth2.provider.test.internal.activator.BaseTestPreparatorBundleActivator;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.PortalUtil;

import java.util.Arrays;
import java.util.Dictionary;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Marta Medio
 */
@RunAsClient
@RunWith(Arquillian.class)
public class CORSApplicationClientTest extends BaseClientTestCase {

	@Deployment
	public static Archive<?> getDeployment() throws Exception {
		return BaseClientTestCase.getDeployment(
			MethodApplicationTestPreparatorBundleActivator.class);
	}

	@Test
	public void testApplicationWithoutCORS() throws Exception {
		WebTarget webTarget = getWebTarget("/no-cors");

		Invocation.Builder invocationBuilder = authorize(
			webTarget.request(), getToken("oauthTestApplicationNoCORS"));

		invocationBuilder.header("Origin", _DUMMY_URI);
		invocationBuilder.header(
			"Access-Control-Request-Method", "authentication");

		Response response = invocationBuilder.options();

		Assert.assertNull(
			response.getHeaderString("Access-Control-Allow-Origin"));
	}

	@Test
	public void testCORSApplicationUri() throws Exception {
		WebTarget webTarget = getWebTarget("/cors");

		Invocation.Builder invocationBuilder = authorize(
			webTarget.request(), getToken("oauthTestApplicationCORSWithUri"));

		invocationBuilder.header("Origin", _DUMMY_URI);

		Response response = invocationBuilder.options();

		Assert.assertEquals(
			_DUMMY_URI,
			response.getHeaderString("Access-Control-Allow-Origin"));

		response = invocationBuilder.get();

		Assert.assertEquals(
			_DUMMY_URI,
			response.getHeaderString("Access-Control-Allow-Origin"));
		Assert.assertEquals(200, response.getStatus());
		Assert.assertNotEquals(
			StringPool.BLANK, response.readEntity(String.class));
	}

	@Test
	public void testCORSPreflight() throws Exception {
		WebTarget webTarget = getWebTarget("/cors");

		Invocation.Builder invocationBuilder = authorize(
			webTarget.request(), getToken("oauthTestApplicationCORS"));

		invocationBuilder.header("Origin", _DUMMY_URI);
		invocationBuilder.header(
			"Access-Control-Request-Method", "authentication");

		Response response = invocationBuilder.options();

		Assert.assertEquals(
			_DUMMY_URI,
			response.getHeaderString("Access-Control-Allow-Origin"));

		response = invocationBuilder.get();

		Assert.assertEquals(
			StringPool.BLANK, response.readEntity(String.class));
	}

	public static class MethodApplicationTestPreparatorBundleActivator
		extends BaseTestPreparatorBundleActivator {

		@Override
		protected void prepareTest() throws Exception {
			Dictionary<String, Object> properties = new HashMapDictionary<>();

			properties.put("auth.verifier.cors.allowed", true);

			registerJaxRsApplication(new TestApplication(), "cors", properties);

			long defaultCompanyId = PortalUtil.getDefaultCompanyId();

			User user = UserTestUtil.getAdminUser(defaultCompanyId);

			createOAuth2Application(
				defaultCompanyId, user, "oauthTestApplicationCORS");

			createOAuth2Application(
				defaultCompanyId, user, "oauthTestApplicationCORSWithUri",
				"oauthTestApplicationSecret",
				Arrays.asList(
					GrantType.CLIENT_CREDENTIALS,
					GrantType.RESOURCE_OWNER_PASSWORD),
				Arrays.asList("everything", "everything.readonly"),
				Arrays.asList(_DUMMY_URI));

			properties = new HashMapDictionary<>();

			registerJaxRsApplication(
				new TestApplication(), "no-cors", properties);

			createOAuth2Application(
				defaultCompanyId, user, "oauthTestApplicationNoCORS");
		}

	}

	private static final String _DUMMY_URI = "http://test-cors.com";

}
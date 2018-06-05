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

package com.liferay.portal.security.auth.verifier.test;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Marta Medio
 */
@RunAsClient
@RunWith(Arquillian.class)
public class AuthVerifierTrackerTest {

	@Test
	public void testGetUser() throws Exception {
		_provider.setCredentials(AuthScope.ANY, _credentials);

		HttpClient client =
			HttpClientBuilder.create().setDefaultCredentialsProvider(
				_provider).build();

		HttpResponse httpResponse = client.execute(
			new HttpGet(
				_LOCALHOST_URL + "/auth-verifier-filter-test/getUserName"));

		String response = EntityUtils.toString(httpResponse.getEntity());

		Assert.assertNotEquals("", response);

		httpResponse = client.execute(
			new HttpGet(
				_LOCALHOST_URL + "/no-auth-verifier-filter-test/getUserName"));

		response = EntityUtils.toString(httpResponse.getEntity());

		Assert.assertEquals("", response);
	}

	private static final String _LOCALHOST_URL = "http://localhost:8080/o";

	private final UsernamePasswordCredentials _credentials =
		new UsernamePasswordCredentials("test@liferay.com", "test");
	private final CredentialsProvider _provider =
		new BasicCredentialsProvider();

}
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

import com.liferay.arquillian.deploymentscenario.annotations.BndFile;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Marta Medio
 */
@BndFile("bnd-guest.bnd")
@RunAsClient
@RunWith(Arquillian.class)
public class AuthVerifierAllowGuestTest {

	@Test
	public void testAllowGuest() throws Exception {
		HttpGet request = new HttpGet(
			_LOCALHOST_URL + "/auth-verifier-no-allow-guest-test/getAccess");

		HttpResponse response = _client.execute(request);

		Assert.assertEquals("", EntityUtils.toString(response.getEntity()));

		_client = HttpClients.createDefault();

		request = new HttpGet(
			_LOCALHOST_URL + "/auth-verifier-allow-guest-test/getAccess");

		response = _client.execute(request);

		Assert.assertNotEquals("", EntityUtils.toString(response.getEntity()));
	}

	private static final String _LOCALHOST_URL = "http://localhost:8080/o";

	private HttpClient _client = HttpClients.createDefault();

}
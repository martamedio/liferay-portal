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
import com.liferay.portal.kernel.util.StringUtil;

import java.io.InputStream;

import java.net.URL;

import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Marta Medio
 */
@BndFile("test-bnd.bnd")
@RunAsClient
@RunWith(Arquillian.class)
public class TrackerAuthVerifierTest {

	@Test
	public void testRemoteAccess() throws Exception {
		URL url = new URL(
			"http://localhost:8080/o/auth-verifier-filter-tracker-remote-" +
				"access-test/remoteAccess");

		try (InputStream inputStream = url.openStream()) {
			Assert.assertEquals("true", StringUtil.read(inputStream));
		}
	}

	@Test
	public void testRemoteUser() throws Exception {
		URL url = new URL(
			"http://localhost:8080/o/auth-verifier-filter-tracker-enabled-" +
				"test/remoteUser");

		try (InputStream inputStream = url.openStream()) {
			Assert.assertEquals(
				"remote-user-set", StringUtil.read(inputStream));
		}

		url = new URL(
			"http://localhost:8080/o/auth-verifier-filter-tracker-disabled-" +
				"test/remoteUser");

		try (InputStream inputStream = url.openStream()) {
			Assert.assertEquals("no-remote-user", StringUtil.read(inputStream));
		}

		url = new URL(
			"http://localhost:8080/o/auth-verifier-filter-tracker-default-" +
				"test/remoteUser");

		try (InputStream inputStream = url.openStream()) {
			Assert.assertEquals("no-remote-user", StringUtil.read(inputStream));
		}
	}

}
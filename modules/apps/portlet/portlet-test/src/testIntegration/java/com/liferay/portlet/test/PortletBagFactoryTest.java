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

package com.liferay.portlet.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.model.impl.PortletAppImpl;
import com.liferay.portal.model.impl.PortletImpl;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portlet.PortletBagFactory;

import javax.servlet.ServletContext;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.mock.web.MockServletContext;

/**
 * @author Raymond Augé
 */
@RunWith(Arquillian.class)
public class PortletBagFactoryTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void testConcreteInstance() throws Exception {
		PortletImpl portletImpl = new PortletImpl();

		portletImpl.setPortletApp(new PortletAppImpl(StringPool.BLANK));

		Class<?> clazz = getClass();

		PortletBagFactory portletBagFactory = _createPortletBagFactory(
			clazz.getClassLoader(), new MockServletContext(), false);

		portletBagFactory.create(portletImpl, new MVCPortlet(), false);
	}

	@Test
	public void testInitializedInstance() throws Exception {
		PortletImpl portletImpl = new PortletImpl();

		portletImpl.setPortletApp(new PortletAppImpl(StringPool.BLANK));
		portletImpl.setPortletClass(MVCPortlet.class.getName());

		Class<?> clazz = getClass();

		PortletBagFactory portletBagFactory = _createPortletBagFactory(
			clazz.getClassLoader(), new MockServletContext(), false);

		portletBagFactory.create(portletImpl);
	}

	@Test
	public void testValidate() throws Exception {
		_testValidate(null, null, null);

		Class<?> clazz = getClass();

		_testValidate(clazz.getClassLoader(), null, null);

		_testValidate(clazz.getClassLoader(), new MockServletContext(), null);
	}

	private PortletBagFactory _createPortletBagFactory(
		ClassLoader classLoader, ServletContext servletContext,
		Boolean warFile) {

		PortletBagFactory portletBagFactory = new PortletBagFactory();

		portletBagFactory.setClassLoader(classLoader);
		portletBagFactory.setServletContext(servletContext);

		if (warFile != null) {
			portletBagFactory.setWARFile(warFile);
		}

		return portletBagFactory;
	}

	private void _testValidate(
			ClassLoader classLoader, ServletContext servletContext,
			Boolean warFile)
		throws Exception {

		try {
			PortletBagFactory portletBagFactory = _createPortletBagFactory(
				classLoader, servletContext, warFile);

			portletBagFactory.create(new PortletImpl());

			Assert.fail();
		}
		catch (IllegalStateException ise) {
		}
	}

}
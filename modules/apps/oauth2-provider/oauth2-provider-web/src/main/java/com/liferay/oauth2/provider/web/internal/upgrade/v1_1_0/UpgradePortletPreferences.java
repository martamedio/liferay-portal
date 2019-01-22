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

package com.liferay.oauth2.provider.web.internal.upgrade.v1_1_0;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.upgrade.BaseUpgradePortletPreferences;
import com.liferay.portal.kernel.util.Validator;

import javax.portlet.PortletPreferences;

/**
 * @author Tomas Polesovsky
 */
public class UpgradePortletPreferences extends BaseUpgradePortletPreferences {

	@Override
	protected String[] getPortletIds() {
		return _PORTLET_IDS;
	}

	@Override
	protected String upgradePreferences(
			long companyId, long ownerId, int ownerType, long plid,
			String portletId, String xml)
		throws Exception {

		PortletPreferences portletPreferences =
			PortletPreferencesFactoryUtil.fromXML(
				companyId, ownerId, ownerType, plid, portletId, xml);

		String oAuth2Features = portletPreferences.getValue(
			_OAUTH2_FEATURES, StringPool.BLANK);

		if (Validator.isBlank(oAuth2Features)) {
			oAuth2Features = "cors";
		}
		else {
			oAuth2Features = oAuth2Features + ",cors";
		}

		portletPreferences.setValue(_OAUTH2_FEATURES, oAuth2Features);

		return PortletPreferencesFactoryUtil.toXML(portletPreferences);
	}

	private static final String _OAUTH2_FEATURES = "oAuth2Features";

	private static final String[] _PORTLET_IDS = {
		"com_liferay_oauth2_provider_web_internal_portlet_OAuth2AdminPortlet"
	};

}
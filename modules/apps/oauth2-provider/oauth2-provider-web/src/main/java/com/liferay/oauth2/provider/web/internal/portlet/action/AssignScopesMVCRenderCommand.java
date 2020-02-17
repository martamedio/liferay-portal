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

package com.liferay.oauth2.provider.web.internal.portlet.action;

import com.liferay.document.library.util.DLURLHelper;
import com.liferay.oauth2.provider.configuration.OAuth2ProviderConfiguration;
import com.liferay.oauth2.provider.scope.liferay.ScopeLocator;
import com.liferay.oauth2.provider.scope.liferay.spi.ApplicationDescriptorLocator;
import com.liferay.oauth2.provider.scope.liferay.spi.ScopeDescriptorLocator;
import com.liferay.oauth2.provider.scope.spi.scope.matcher.ScopeMatcherFactory;
import com.liferay.oauth2.provider.service.OAuth2ApplicationScopeAliasesLocalService;
import com.liferay.oauth2.provider.service.OAuth2ApplicationService;
import com.liferay.oauth2.provider.service.OAuth2ScopeGrantLocalService;
import com.liferay.oauth2.provider.web.internal.constants.OAuth2ProviderPortletKeys;
import com.liferay.oauth2.provider.web.internal.constants.OAuth2ProviderWebKeys;
import com.liferay.oauth2.provider.web.internal.display.context.AssignScopesDisplayContext;
import com.liferay.oauth2.provider.web.internal.display.context.AssignScopesTreeDisplayContext;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Tomas Polesovsky
 * @author Stian Sigvartsen
 */
@Component(
	configurationPid = "com.liferay.oauth2.provider.configuration.OAuth2ProviderConfiguration",
	configurationPolicy = ConfigurationPolicy.OPTIONAL,
	property = {
		"javax.portlet.name=" + OAuth2ProviderPortletKeys.OAUTH2_ADMIN,
		"mvc.command.name=/admin/assign_scopes"
	},
	service = MVCRenderCommand.class
)
public class AssignScopesMVCRenderCommand implements MVCRenderCommand {

	@Override
	public String render(
		RenderRequest renderRequest, RenderResponse renderResponse) {

		ThemeDisplay themeDisplay = getThemeDisplay(renderRequest);

		try {
			AssignScopesDisplayContext assignScopesDisplayContext =
				new AssignScopesDisplayContext(
					_oAuth2ApplicationService,
					_oAuth2ApplicationScopeAliasesLocalService,
					_oAuth2ScopeGrantLocalService, _oAuth2ProviderConfiguration,
					renderRequest, themeDisplay, _applicationDescriptorLocator,
					_scopeDescriptorLocator, _scopeLocator, _dlURLHelper);

			renderRequest.setAttribute(
				OAuth2ProviderWebKeys.OAUTH2_ADMIN_PORTLET_DISPLAY_CONTEXT,
				assignScopesDisplayContext);

			PermissionChecker permissionChecker =
				themeDisplay.getPermissionChecker();

			if (!permissionChecker.isOmniadmin()) {
				ScopeMatcherFactory scopeMatcherFactory =
					getScopeMatcherFactory(themeDisplay.getCompanyId());

				AssignScopesTreeDisplayContext assignScopesTreeDisplayContext =
					new AssignScopesTreeDisplayContext(
						_oAuth2ApplicationService,
						_oAuth2ApplicationScopeAliasesLocalService,
						_oAuth2ScopeGrantLocalService,
						_oAuth2ProviderConfiguration, renderRequest,
						themeDisplay, _scopeDescriptorLocator, _scopeLocator,
						scopeMatcherFactory, _dlURLHelper);

				renderRequest.setAttribute(
					OAuth2ProviderWebKeys.
						OAUTH2_ADMIN_PORTLET_TREE_DISPLAY_CONTEXT,
					assignScopesTreeDisplayContext);
			}
		}
		catch (PortalException portalException) {
			if (_log.isDebugEnabled()) {
				_log.debug(portalException.getMessage(), portalException);
			}
		}

		return "/admin/edit_application.jsp";
	}

	@Activate
	protected void activate(
		BundleContext bundleContext, Map<String, Object> properties) {

		_oAuth2ProviderConfiguration = ConfigurableUtil.createConfigurable(
			OAuth2ProviderConfiguration.class, properties);

		_scopeMatcherFactoriesServiceTrackerMap =
			ServiceTrackerMapFactory.openSingleValueMap(
				bundleContext, ScopeMatcherFactory.class, "company.id");
	}

	protected ScopeMatcherFactory getScopeMatcherFactory(long companyId) {
		ScopeMatcherFactory scopeMatcherFactory =
			_scopeMatcherFactoriesServiceTrackerMap.getService(
				String.valueOf(companyId));

		if (scopeMatcherFactory == null) {
			return _defaultScopeMatcherFactory;
		}

		return scopeMatcherFactory;
	}

	protected ThemeDisplay getThemeDisplay(PortletRequest portletRequest) {
		return (ThemeDisplay)portletRequest.getAttribute(WebKeys.THEME_DISPLAY);
	}

	@Reference(name = "default", unbind = "-")
	protected void setDefaultScopeMatcherFactory(
		ScopeMatcherFactory defaultScopeMatcherFactory) {

		_defaultScopeMatcherFactory = defaultScopeMatcherFactory;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		AssignScopesMVCRenderCommand.class);

	@Reference
	private ApplicationDescriptorLocator _applicationDescriptorLocator;

	private ScopeMatcherFactory _defaultScopeMatcherFactory;

	@Reference
	private DLURLHelper _dlURLHelper;

	@Reference
	private OAuth2ApplicationScopeAliasesLocalService
		_oAuth2ApplicationScopeAliasesLocalService;

	@Reference
	private OAuth2ApplicationService _oAuth2ApplicationService;

	private OAuth2ProviderConfiguration _oAuth2ProviderConfiguration;

	@Reference
	private OAuth2ScopeGrantLocalService _oAuth2ScopeGrantLocalService;

	@Reference
	private ScopeDescriptorLocator _scopeDescriptorLocator;

	@Reference
	private ScopeLocator _scopeLocator;

	private ServiceTrackerMap<String, ScopeMatcherFactory>
		_scopeMatcherFactoriesServiceTrackerMap;

}
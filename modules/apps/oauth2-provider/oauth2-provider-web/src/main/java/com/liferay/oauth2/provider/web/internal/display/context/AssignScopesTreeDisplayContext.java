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

package com.liferay.oauth2.provider.web.internal.display.context;

import com.liferay.document.library.util.DLURLHelper;
import com.liferay.oauth2.provider.configuration.OAuth2ProviderConfiguration;
import com.liferay.oauth2.provider.model.OAuth2Application;
import com.liferay.oauth2.provider.model.OAuth2ScopeGrant;
import com.liferay.oauth2.provider.scope.liferay.ScopeLocator;
import com.liferay.oauth2.provider.scope.liferay.spi.ScopeDescriptorLocator;
import com.liferay.oauth2.provider.scope.spi.scope.matcher.ScopeMatcherFactory;
import com.liferay.oauth2.provider.service.OAuth2ApplicationScopeAliasesLocalService;
import com.liferay.oauth2.provider.service.OAuth2ApplicationService;
import com.liferay.oauth2.provider.service.OAuth2ScopeGrantLocalService;
import com.liferay.oauth2.provider.web.internal.ScopeAliasScopeDescriptor;
import com.liferay.oauth2.provider.web.internal.taglib.Tree;
import com.liferay.oauth2.provider.web.internal.util.GenerateScopesTreeUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.theme.ThemeDisplay;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import javax.portlet.PortletRequest;

/**
 * @author Marta Medio
 */
public class AssignScopesTreeDisplayContext
	extends OAuth2AdminPortletDisplayContext {

	public AssignScopesTreeDisplayContext(
			OAuth2ApplicationService oAuth2ApplicationService,
			OAuth2ApplicationScopeAliasesLocalService
				oAuth2ApplicationScopeAliasesLocalService,
			OAuth2ScopeGrantLocalService oAuth2ScopeGrantLocalService,
			OAuth2ProviderConfiguration oAuth2ProviderConfiguration,
			PortletRequest portletRequest, ThemeDisplay themeDisplay,
			ScopeDescriptorLocator scopeDescriptorLocator,
			ScopeLocator scopeLocator, ScopeMatcherFactory scopeMatcherFactory,
			DLURLHelper dlURLHelper)
		throws PortalException {

		super(
			oAuth2ApplicationService, oAuth2ApplicationScopeAliasesLocalService,
			oAuth2ProviderConfiguration, portletRequest, themeDisplay,
			dlURLHelper);

		_companyId = themeDisplay.getCompanyId();
		_locale = themeDisplay.getLocale();

		_scopeAliasScopeDescriptor = new ScopeAliasScopeDescriptor(
			_companyId, scopeDescriptorLocator, scopeLocator);

		OAuth2Application oAuth2Application = getOAuth2Application();

		_assignedScopeAliases = getAssignedScopeAliases(
			oAuth2Application.getOAuth2ApplicationScopeAliasesId(),
			oAuth2ScopeGrantLocalService);

		Set<String> scopeAliases = new LinkedHashSet<>(
			scopeLocator.getScopeAliases(_companyId));

		_assignedDeletedScopeAliases = _generateAssignedDeletedScopeAliases(
			scopeAliases);

		scopeAliases.addAll(_assignedScopeAliases);

		_scopeAliasesDescriptions = _generateScopeAliasesDescriptions(
			scopeAliases);

		_availableScopeAliases = GenerateScopesTreeUtil.generateScopesTree(
			scopeAliases, scopeMatcherFactory);
	}

	public Set<String> getAssignedDeletedScopeAliases() {
		return _assignedDeletedScopeAliases;
	}

	public Set<String> getAssignedScopeAliases() {
		return _assignedScopeAliases;
	}

	public Tree.Node<String> getAvailableScopeAliases() {
		return _availableScopeAliases;
	}

	public Map<String, String> getScopeAliasesDescriptions() {
		return _scopeAliasesDescriptions;
	}

	protected Set<String> getAssignedScopeAliases(
		long oAuth2ApplicationScopeAliasesId,
		OAuth2ScopeGrantLocalService oAuth2ScopeGrantLocalService) {

		Collection<OAuth2ScopeGrant> oAuth2ScopeGrants =
			oAuth2ScopeGrantLocalService.getOAuth2ScopeGrants(
				oAuth2ApplicationScopeAliasesId, QueryUtil.ALL_POS,
				QueryUtil.ALL_POS, null);

		Set<String> assignedScopeAliases = new TreeSet<>();

		Stream<OAuth2ScopeGrant> scopeGrantStream = oAuth2ScopeGrants.stream();

		scopeGrantStream.forEach(
			oAuth2ScopeGrant -> assignedScopeAliases.addAll(
				oAuth2ScopeGrant.getScopeAliasesList()));

		return assignedScopeAliases;
	}

	private Set<String> _generateAssignedDeletedScopeAliases(
		Set<String> scopeAliases) {

		Set<String> asignedDeletedScopeAliases = new TreeSet<>();

		for (String assignedScope : _assignedScopeAliases) {
			if (!scopeAliases.contains(assignedScope)) {
				asignedDeletedScopeAliases.add(assignedScope);
			}
		}

		return asignedDeletedScopeAliases;
	}

	private Map<String, String> _generateScopeAliasesDescriptions(
		Set<String> scopeAliases) {

		Map<String, String> descriptionsMap = new HashMap<>();

		for (String scope : scopeAliases) {
			String description = _scopeAliasScopeDescriptor.getDescription(
				scope, _locale, StringPool.COMMA_AND_SPACE);

			descriptionsMap.put(scope, description);
		}

		return descriptionsMap;
	}

	private final Set<String> _assignedDeletedScopeAliases;
	private final Set<String> _assignedScopeAliases;
	private final Tree.Node<String> _availableScopeAliases;
	private final long _companyId;
	private final Locale _locale;
	private final Map<String, String> _scopeAliasesDescriptions;
	private final ScopeAliasScopeDescriptor _scopeAliasScopeDescriptor;

}
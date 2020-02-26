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
import java.util.stream.Collectors;
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

		_locale = themeDisplay.getLocale();

		long companyId = themeDisplay.getCompanyId();

		_scopeAliasScopeDescriptor = new ScopeAliasScopeDescriptor(
			companyId, scopeDescriptorLocator, scopeLocator);

		OAuth2Application oAuth2Application = getOAuth2Application();

		_assignedScopeAliases = getAssignedScopeAliases(
			oAuth2Application.getOAuth2ApplicationScopeAliasesId(),
			oAuth2ScopeGrantLocalService);

		Set<String> scopeAliases = new LinkedHashSet<>(
			scopeLocator.getScopeAliases(companyId));

		_assignedDeletedScopeAliases = _getAssignedDeletedScopeAliases(
			scopeAliases);

		scopeAliases.addAll(_assignedScopeAliases);

		_scopeAliasesDescriptionMap = _getScopeAliasDescriptionMap(
			scopeAliases);

		_scopeAliasesTreeNode = GenerateScopesTreeUtil.getScopesTreeNode(
			scopeAliases, scopeMatcherFactory);

		List<Tree<String>> children = _scopeAliasesTreeNode.getChildren();

		children.sort(
			Comparator.comparing(
				Tree::getValue, String.CASE_INSENSITIVE_ORDER));
	}

	public Set<String> getAssignedDeletedScopeAliases() {
		return _assignedDeletedScopeAliases;
	}

	public Set<String> getAssignedScopeAliases() {
		return _assignedScopeAliases;
	}

	public Map<String, String> getScopeAliasDescriptionMap() {
		return _scopeAliasesDescriptionMap;
	}

	public Tree.Node<String> getScopeAliasTreeNode() {
		return _scopeAliasesTreeNode;
	}

	protected Set<String> getAssignedScopeAliases(
		long oAuth2ApplicationScopeAliasesId,
		OAuth2ScopeGrantLocalService oAuth2ScopeGrantLocalService) {

		Collection<OAuth2ScopeGrant> oAuth2ScopeGrants =
			oAuth2ScopeGrantLocalService.getOAuth2ScopeGrants(
				oAuth2ApplicationScopeAliasesId, QueryUtil.ALL_POS,
				QueryUtil.ALL_POS, null);

		Stream<OAuth2ScopeGrant> stream = oAuth2ScopeGrants.stream();

		return stream.map(
			OAuth2ScopeGrant::getScopeAliasesList
		).flatMap(
			Collection::stream
		).collect(
			Collectors.toCollection(TreeSet::new)
		);
	}

	private Set<String> _getAssignedDeletedScopeAliases(
		Set<String> scopeAliases) {

		Stream<String> stream = _assignedScopeAliases.stream();

		return stream.filter(
			scopeAlias -> !scopeAliases.contains(scopeAlias)
		).collect(
			Collectors.toCollection(TreeSet::new)
		);
	}

	private Map<String, String> _getScopeAliasDescriptionMap(
		Set<String> scopeAliases) {

		Map<String, String> map = new HashMap<>();

		for (String scopeAlias : scopeAliases) {
			map.put(
				scopeAlias,
				_scopeAliasScopeDescriptor.getDescription(
					scopeAlias, _locale, StringPool.COMMA_AND_SPACE));
		}

		return map;
	}

	private final Set<String> _assignedDeletedScopeAliases;
	private final Set<String> _assignedScopeAliases;
	private final Locale _locale;
	private final Map<String, String> _scopeAliasesDescriptionMap;
	private final Tree.Node<String> _scopeAliasesTreeNode;
	private final ScopeAliasScopeDescriptor _scopeAliasScopeDescriptor;

}
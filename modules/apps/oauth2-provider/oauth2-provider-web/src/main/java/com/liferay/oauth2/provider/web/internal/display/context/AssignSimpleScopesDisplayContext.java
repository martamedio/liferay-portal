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

import static com.liferay.portal.kernel.util.PropertiesUtil.toMap;

import static java.util.Map.Entry.comparingByKey;

import com.liferay.document.library.util.DLURLHelper;
import com.liferay.oauth2.provider.configuration.OAuth2ProviderConfiguration;
import com.liferay.oauth2.provider.model.OAuth2Application;
import com.liferay.oauth2.provider.model.OAuth2ScopeGrant;
import com.liferay.oauth2.provider.scope.liferay.ScopeLocator;
import com.liferay.oauth2.provider.scope.liferay.spi.ScopeDescriptorLocator;
import com.liferay.oauth2.provider.scope.spi.scope.matcher.ScopeMatcher;
import com.liferay.oauth2.provider.scope.spi.scope.matcher.ScopeMatcherFactory;
import com.liferay.oauth2.provider.service.OAuth2ApplicationScopeAliasesLocalService;
import com.liferay.oauth2.provider.service.OAuth2ApplicationService;
import com.liferay.oauth2.provider.service.OAuth2ScopeGrantLocalService;
import com.liferay.oauth2.provider.web.internal.ScopeAliasScopeDescriptor;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.theme.ThemeDisplay;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.portlet.PortletRequest;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.DirectedAcyclicGraph;

/**
 * @author Marta Medio
 */
public class AssignSimpleScopesDisplayContext
	extends OAuth2AdminPortletDisplayContext {

	public AssignSimpleScopesDisplayContext(
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

		_availableScopeAliasesMap = _generateScopesTree(
			scopeAliases, scopeMatcherFactory);
	}

	public Set<String> getAssignedDeletedScopeAliases() {
		return _assignedDeletedScopeAliases;
	}

	public Set<String> getAssignedScopeAliases() {
		return _assignedScopeAliases;
	}

	public Map<String, HashMap<String, Map>> getAvailableScopeAliasesMap() {
		return _availableScopeAliasesMap;
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

	private Map<String, HashMap<String, Map>> _generateScopesTree(
		Set<String> scopeAliases, ScopeMatcherFactory scopeMatcherFactory) {

		Map<String, HashMap<String, Map>> scopeAliasesMap = new HashMap<>();

		DirectedAcyclicGraph<String, String> directedAcyclicGraph =
			new DirectedAcyclicGraph<>(String.class);

		for (String vertex : scopeAliases) {
			directedAcyclicGraph.addVertex(vertex);
		}

		Set<String> initialScopes = new HashSet<>();
		Set<String> endingScopes = new HashSet<>();

		for (String scope : scopeAliases) {
			Stream<String> streamScopeAliases = scopeAliases.stream();

			streamScopeAliases.forEach(
				s -> {
					ScopeMatcher scopeMatcher = scopeMatcherFactory.create(
						scope);

					if ((s != scope) && scopeMatcher.match(s)) {
						directedAcyclicGraph.addEdge(
							scope, s, scope + " -> " + s);
					}
				});
		}

		for (String scope : scopeAliases) {
			if (directedAcyclicGraph.inDegreeOf(scope) == 0) {
				initialScopes.add(scope);
			}

			if (directedAcyclicGraph.outDegreeOf(scope) == 0) {
				endingScopes.add(scope);
			}
		}

		for (String initialScope : initialScopes) {
			scopeAliasesMap.put(initialScope, new HashMap<>());
		}

		AllDirectedPaths<String, String> allDirectedPaths =
			new AllDirectedPaths<>(directedAcyclicGraph);

		List<GraphPath<String, String>> allPaths = allDirectedPaths.getAllPaths(
			initialScopes, endingScopes, true, null);

		Comparator<GraphPath<?, ?>> ci = Comparator.comparingInt(
			GraphPath::getLength);

		allPaths.sort(ci.reversed());

		HashMap<String, Set<String>> visitedMap = new HashMap<>();

		for (GraphPath<String, String> path : allPaths) {
			List<String> vertexList = path.getVertexList();

			Set<String> visited = visitedMap.computeIfAbsent(
				path.getStartVertex() + " -> " + path.getEndVertex(),
				__ -> new HashSet<>());

			if (visited.containsAll(vertexList)) {
				continue;
			}

			visited.addAll(vertexList);

			Map<String, Map> parent = null;
			Iterator<String> iterator = vertexList.iterator();

			while (iterator.hasNext()) {
				String scope = iterator.next();

				if (parent == null) {
					parent = scopeAliasesMap.get(scope);

					continue;
				}

				parent.computeIfAbsent(scope, __ -> new HashMap());

				if (iterator.hasNext()) {
					parent = parent.get(scope);
				}
			}
		}

		Set<Map.Entry<String, HashMap<String, Map>>> scopeAliasesEntrySet =
			scopeAliasesMap.entrySet();

		Stream<Map.Entry<String, HashMap<String, Map>>>
			scopeAliasesEntrySetSteam = scopeAliasesEntrySet.stream();

		Comparator<Map.Entry<String, HashMap<String, Map>>> comparator =
			Map.Entry.<String, HashMap<String, Map>>comparingByKey(
				String.CASE_INSENSITIVE_ORDER);

		scopeAliasesMap = scopeAliasesEntrySetSteam.sorted(
			comparator.reversed()
		).collect(
			Collectors.toMap(
				Map.Entry::getKey, Map.Entry::getValue,
				(oldValue, newValue) -> oldValue, LinkedHashMap::new)
		);

		return scopeAliasesMap;
	}

	private final Set<String> _assignedDeletedScopeAliases;
	private final Set<String> _assignedScopeAliases;
	private final Map<String, HashMap<String, Map>> _availableScopeAliasesMap;
	private final long _companyId;
	private final Locale _locale;
	private final Map<String, String> _scopeAliasesDescriptions;
	private final ScopeAliasScopeDescriptor _scopeAliasScopeDescriptor;

}
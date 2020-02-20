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

package com.liferay.oauth2.provider.web.internal.util;

import com.liferay.oauth2.provider.scope.spi.scope.matcher.ScopeMatcher;
import com.liferay.oauth2.provider.scope.spi.scope.matcher.ScopeMatcherFactory;
import com.liferay.oauth2.provider.web.internal.taglib.Tree;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.liferay.oauth2.provider.web.internal.taglib.Tree.Leaf;
import com.liferay.oauth2.provider.web.internal.taglib.Tree.Node;
import com.liferay.petra.string.StringPool;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.DirectedAcyclicGraph;

/**
 * @author Marta Medio
 */
public class GenerateScopesTreeUtil {

	public static Tree<String> generateScopesTree(
		Set<String> scopeAliases, ScopeMatcherFactory scopeMatcherFactory) {

		DirectedAcyclicGraph<String, String> directedAcyclicGraph =
			new DirectedAcyclicGraph<>(String.class);

		for (String vertex : scopeAliases) {
			directedAcyclicGraph.addVertex(vertex);
		}

		for (String scopeAlias1 : scopeAliases) {
			ScopeMatcher scopeMatcher = scopeMatcherFactory.create(scopeAlias1);

			for (String scopeAlias2 : scopeAliases) {
				if (Objects.equals(scopeAlias1, scopeAlias2)) {
					continue;
				}

				if (scopeMatcher.match(scopeAlias2)) {
					directedAcyclicGraph.addEdge(
						scopeAlias1, scopeAlias2,
						scopeAlias1 + "#" + scopeAlias2);
				}
			}
		}

		Set<String> endingScopes = new HashSet<>();
		Set<String> initialScopes = new HashSet<>();

		for (String scope : scopeAliases) {
			if (directedAcyclicGraph.outDegreeOf(scope) == 0) {
				endingScopes.add(scope);
			}

			if (directedAcyclicGraph.inDegreeOf(scope) == 0) {
				initialScopes.add(scope);
			}
		}

		AllDirectedPaths<String, String> allDirectedPaths =
			new AllDirectedPaths<>(directedAcyclicGraph);

		List<GraphPath<String, String>> allPaths = allDirectedPaths.getAllPaths(
			initialScopes, endingScopes, true, null);

		Comparator<GraphPath<?, ?>> comparator = Comparator.comparingInt(
			GraphPath::getLength);

		allPaths.sort(comparator.reversed());

		HashMap<String, Set<String>> visitedMap = new HashMap<>();

		for (GraphPath<String, String> path : allPaths) {
			List<String> vertexList = path.getVertexList();

			Set<String> visited = visitedMap.computeIfAbsent(
				path.getStartVertex() + "#" + path.getEndVertex(),
				__ -> new HashSet<>());

			if (visited.containsAll(vertexList)) {
				directedAcyclicGraph.removeAllEdges(path.getEdgeList());

				continue;
			}

			visited.addAll(vertexList);
		}

		final Stream<String> stream = initialScopes.stream();

		return new Node<>(
			StringPool.BLANK, stream.map(
				scope -> _createTree(directedAcyclicGraph, scope)
			).collect(
				Collectors.toList()
			)
		);
	}

	private static <T, E> Tree<T> _createTree(Graph<T, E> graph, T t) {
		if (graph.outDegreeOf(t) == 0) {
			return new Leaf<>(t);
		}
		else {
			final Set<E> outgoingEdgesOf = graph.outgoingEdgesOf(t);

			final Stream<E> stream = outgoingEdgesOf.stream();

			return new Node<>(
				t,
				stream.map(
					edge -> _createTree(graph, graph.getEdgeTarget(edge))
				).collect(
					Collectors.toList()
				)
			);
		}
	}

}
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
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.DirectedAcyclicGraph;

/**
 * @author Carlos Sierra
 * @author Marta Medio
 */
public class GenerateScopesTreeUtil {

	public static Tree.Node<String> getScopesTreeNode(
		Set<String> scopeAliases, ScopeMatcherFactory scopeMatcherFactory) {

		DirectedAcyclicGraph<String, String> directedAcyclicGraph =
			new DirectedAcyclicGraph<>(String.class);

		for (String scopeAlias1 : scopeAliases) {
			directedAcyclicGraph.addVertex(scopeAlias1);

			ScopeMatcher scopeMatcher = scopeMatcherFactory.create(scopeAlias1);

			for (String scopeAlias2 : scopeAliases) {
				if (Objects.equals(scopeAlias1, scopeAlias2)) {
					continue;
				}

				if (scopeMatcher.match(scopeAlias2)) {
					directedAcyclicGraph.addVertex(scopeAlias2);

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

		_filterRedundantPaths(
			directedAcyclicGraph, initialScopes, endingScopes);

		return _createTreeNode(
			directedAcyclicGraph, StringPool.BLANK, initialScopes);
	}

	private static <T, E> Tree<T> _createTree(Graph<T, E> graph, T t) {
		if (graph.outDegreeOf(t) == 0) {
			return new Tree.Leaf<>(t);
		}

		Set<T> set = new HashSet<>();

		for (E edge : graph.outgoingEdgesOf(t)) {
			set.add(graph.getEdgeTarget(edge));
		}

		return _createTreeNode(graph, t, set);
	}

	private static <T> Tree.Node<T> _createTreeNode(
		Graph<T, ?> graph, T value, Set<T> children) {

		List<Tree<T>> trees = new ArrayList<>();

		for (T child : children) {
			trees.add(_createTree(graph, child));
		}

		return new Tree.Node<>(value, trees);
	}

	private static void _filterRedundantPaths(
		DirectedAcyclicGraph<String, String> directedAcyclicGraph,
		Set<String> initialScopes, Set<String> endingScopes) {

		AllDirectedPaths<String, String> allDirectedPaths =
			new AllDirectedPaths<>(directedAcyclicGraph);

		List<GraphPath<String, String>> allPaths = allDirectedPaths.getAllPaths(
			initialScopes, endingScopes, true, null);

		Comparator<GraphPath<?, ?>> comparator = Comparator.comparingInt(
			GraphPath::getLength);

		allPaths.sort(comparator.reversed());

		HashMap<String, Set<String>> visitedEdgesMap = new HashMap<>();
		HashMap<String, Set<String>> visitedVerticesMap = new HashMap<>();

		for (GraphPath<String, String> path : allPaths) {
			String pathKey = StringBundler.concat(
				path.getStartVertex(), "#", path.getEndVertex());

			Set<String> visitedVerticesSet = visitedVerticesMap.computeIfAbsent(
				pathKey, key -> new HashSet<>());

			List<String> vertexList = path.getVertexList();

			Set<String> visitedEdgesSet = visitedEdgesMap.computeIfAbsent(
				pathKey, key -> new HashSet<>());

			List<String> edgeList = path.getEdgeList();

			if (visitedVerticesSet.containsAll(vertexList)) {
				for (String edge : edgeList) {
					if (!visitedEdgesSet.contains(edge)) {
						directedAcyclicGraph.removeEdge(edge);
					}
				}

				continue;
			}

			visitedEdgesSet.addAll(edgeList);
			visitedVerticesSet.addAll(vertexList);
		}
	}

}
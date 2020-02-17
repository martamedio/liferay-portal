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
import com.liferay.oauth2.provider.web.internal.taglib.Node;
import com.liferay.petra.string.StringPool;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.stream.Stream;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.DirectedAcyclicGraph;

/**
 * @author Marta Medio
 */
public class GenerateScopesTreeUtil {

	public static Node generateScopesTree(
		Set<String> scopeAliases, ScopeMatcherFactory scopeMatcherFactory) {

		Node root = new Node(StringPool.BLANK);

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
			Node node = new Node(initialScope);

			root.addChildren(node);
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

			Node parent = null;
			Iterator<String> iterator = vertexList.iterator();

			while (iterator.hasNext()) {
				String scope = iterator.next();

				if (parent == null) {
					parent = root.findChildrenByValue(scope);

					continue;
				}

				SortedSet<Node> nodes = parent.getNodes();

				nodes.add(new Node(scope));

				if (iterator.hasNext()) {
					parent = parent.findChildrenByValue(scope);
				}
			}
		}

		return root;
	}

}
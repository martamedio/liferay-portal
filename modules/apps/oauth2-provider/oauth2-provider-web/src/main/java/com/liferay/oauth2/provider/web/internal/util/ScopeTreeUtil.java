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

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.DirectedAcyclicGraph;

/**
 * @author Carlos Sierra
 * @author Marta Medio
 */
public class ScopeTreeUtil {

	public static Tree.Node<String> getScopeTreeNode(
		Set<String> scopeAliases, ScopeMatcherFactory scopeMatcherFactory) {

		final HashMap<String, ScopeMatcher> scopeMatcherMap = new HashMap<>();

		return getTreeNode(
			scopeAliases, StringPool.BLANK,
			(scopeAlias1, scopeAlias2) -> {
				final ScopeMatcher scopeMatcher =
					scopeMatcherMap.computeIfAbsent(
						scopeAlias1, scopeMatcherFactory::create);

				return scopeMatcher.match(scopeAlias2);
			},
			() -> new TreeSet<Tree<String>>(
				Comparator.comparing(
					Tree::getValue, String.CASE_INSENSITIVE_ORDER)));
	}

	public static <T> Tree.Node<T> getTreeNode(
		Set<T> set, T rootValue, BiPredicate<T, T> biPredicate,
		Supplier<Collection<Tree<T>>> collectionSupplier) {

		DirectedAcyclicGraph<T, String> directedAcyclicGraph =
			new DirectedAcyclicGraph<>(String.class);

		for (T element1 : set) {
			directedAcyclicGraph.addVertex(element1);

			for (T element2 : set) {
				if (Objects.equals(element1, element2)) {
					continue;
				}

				if (biPredicate.test(element1, element2)) {
					directedAcyclicGraph.addVertex(element2);

					directedAcyclicGraph.addEdge(
						element1, element2,
						StringBundler.concat(element1, "#", element2));
				}
			}
		}

		Set<T> endingVertices = new HashSet<>();
		Set<T> initialVertices = new HashSet<>();

		for (T element : set) {
			if (directedAcyclicGraph.outDegreeOf(element) == 0) {
				endingVertices.add(element);
			}

			if (directedAcyclicGraph.inDegreeOf(element) == 0) {
				initialVertices.add(element);
			}
		}

		_filterRedundantPaths(
			directedAcyclicGraph, initialVertices, endingVertices);

		return _createTreeNode(
			directedAcyclicGraph, rootValue, initialVertices,
			collectionSupplier);
	}

	private static <T, E> Tree<T> _createTree(
		Graph<T, E> graph, T t,
		Supplier<Collection<Tree<T>>> collectionSupplier) {

		if (graph.outDegreeOf(t) == 0) {
			return new Tree.Leaf<>(t);
		}

		Set<T> set = new HashSet<>();

		for (E edge : graph.outgoingEdgesOf(t)) {
			set.add(graph.getEdgeTarget(edge));
		}

		return _createTreeNode(graph, t, set, collectionSupplier);
	}

	private static <T> Tree.Node<T> _createTreeNode(
		Graph<T, ?> graph, T value, Set<T> children,
		Supplier<Collection<Tree<T>>> collectionSupplier) {

		Collection<Tree<T>> trees = collectionSupplier.get();

		for (T child : children) {
			trees.add(_createTree(graph, child, collectionSupplier));
		}

		return new Tree.Node<>(value, trees);
	}

	private static <T> void _filterRedundantPaths(
		DirectedAcyclicGraph<T, String> directedAcyclicGraph,
		Set<T> initialVertices, Set<T> endingVertices) {

		AllDirectedPaths<T, String> allDirectedPaths = new AllDirectedPaths<>(
			directedAcyclicGraph);

		List<GraphPath<T, String>> allPaths = allDirectedPaths.getAllPaths(
			initialVertices, endingVertices, true, null);

		Comparator<GraphPath<?, ?>> comparator = Comparator.comparingInt(
			GraphPath::getLength);

		allPaths.sort(comparator.reversed());

		HashMap<String, Set<String>> visitedEdgesMap = new HashMap<>();
		HashMap<String, Set<T>> visitedVerticesMap = new HashMap<>();

		for (GraphPath<T, String> path : allPaths) {
			String pathKey = StringBundler.concat(
				path.getStartVertex(), "#", path.getEndVertex());

			Set<T> visitedVerticesSet = visitedVerticesMap.computeIfAbsent(
				pathKey, key -> new HashSet<>());

			List<T> vertexList = path.getVertexList();

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
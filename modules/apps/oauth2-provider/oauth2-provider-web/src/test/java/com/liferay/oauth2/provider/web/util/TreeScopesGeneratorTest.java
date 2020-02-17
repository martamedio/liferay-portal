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

package com.liferay.oauth2.provider.web.util;

import com.liferay.oauth2.provider.scope.internal.spi.scope.matcher.ChunkScopeMatcherFactory;
import com.liferay.oauth2.provider.scope.spi.scope.matcher.ScopeMatcherFactory;
import com.liferay.oauth2.provider.web.internal.taglib.Node;
import com.liferay.oauth2.provider.web.internal.util.GenerateScopesTreeUtil;
import com.liferay.petra.string.StringPool;

import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Marta Medio
 */
public class TreeScopesGeneratorTest {

	@Before
	public void setUp() {
		_scopeMatcherFactory = new ChunkScopeMatcherFactory();
	}

	@Test
	public void testMultipleLevelScopeTree() throws Exception {
		List<String> scopesList = Arrays.asList(
			"everything.read", "everything.write", "everything",
			"everything.read.user", "everything.read.user.documents");

		Node root = GenerateScopesTreeUtil.generateScopesTree(
			new TreeSet(scopesList), _scopeMatcherFactory);

		Assert.assertTrue(_checkValue(root, StringPool.BLANK));

		Node node = _getFirstChild(root);

		Assert.assertTrue(_checkValue(node, "everything"));
		Assert.assertFalse(node.isLeaf());

		Node firstChild = _getFirstChild(node);

		Assert.assertTrue(_checkValue(firstChild, "everything.read"));
		Assert.assertFalse(firstChild.isLeaf());

		Node firstGrandChild = _getFirstChild(firstChild);

		Assert.assertTrue(_checkValue(firstGrandChild, "everything.read.user"));
		Assert.assertFalse(firstGrandChild.isLeaf());
		Node greatGrandChild = _getFirstChild(firstGrandChild);

		Assert.assertTrue(
			_checkValue(greatGrandChild, "everything.read.user.documents"));

		Node lastChild = _getLastChild(node);

		Assert.assertTrue(_checkValue(lastChild, "everything.write"));
		Assert.assertTrue(lastChild.isLeaf());
	}

	@Test
	public void testMultipleParentsScopeTree() throws Exception {
		List<String> scopesList = Arrays.asList(
			"everything.read", "everything.write", "everything",
			"everything.read.user", "analytics.read", "analytics");

		Node root = GenerateScopesTreeUtil.generateScopesTree(
			new TreeSet(scopesList), _scopeMatcherFactory);

		Assert.assertTrue(_checkValue(root, StringPool.BLANK));

		Node firstNode = _getFirstChild(root);
		Node lastNode = _getLastChild(root);

		Assert.assertTrue(_checkValue(firstNode, "analytics"));
		Assert.assertFalse(firstNode.isLeaf());

		Node childFirstNode = _getFirstChild(firstNode);

		Assert.assertTrue(_checkValue(childFirstNode, "analytics.read"));

		Assert.assertTrue(childFirstNode.isLeaf());

		Assert.assertTrue(_checkValue(lastNode, "everything"));
		Assert.assertFalse(lastNode.isLeaf());

		Node childLastNode = _getFirstChild(lastNode);

		Assert.assertTrue(_checkValue(childLastNode, "everything.read"));
		Assert.assertFalse(childLastNode.isLeaf());
	}

	@Test
	public void testOneLevelScopeTree() throws Exception {
		List<String> scopesList = Arrays.asList(
			"everything.read", "everything.write", "everything");

		Node root = GenerateScopesTreeUtil.generateScopesTree(
			new TreeSet(scopesList), _scopeMatcherFactory);

		Assert.assertTrue(_checkValue(root, StringPool.BLANK));

		SortedSet<Node> nodes = root.getNodes();

		Node node = nodes.first();

		Assert.assertTrue(_checkValue(node, "everything"));
		Assert.assertFalse(node.isLeaf());

		Node firstChild = _getFirstChild(node);

		Assert.assertTrue(_checkValue(firstChild, "everything.read"));
		Assert.assertTrue(firstChild.isLeaf());

		Node lastChild = _getLastChild(node);

		Assert.assertTrue(_checkValue(lastChild, "everything.write"));
		Assert.assertTrue(lastChild.isLeaf());
	}

	private boolean _checkValue(Node node, String expectedValue) {
		String nodeValue = node.getValue();

		return nodeValue.equals(expectedValue);
	}

	private Node _getFirstChild(Node node) {
		SortedSet<Node> nodes = node.getNodes();

		return nodes.first();
	}

	private Node _getLastChild(Node node) {
		SortedSet<Node> nodes = node.getNodes();

		return nodes.last();
	}

	private ScopeMatcherFactory _scopeMatcherFactory;

}
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
import com.liferay.oauth2.provider.web.internal.taglib.Tree;
import com.liferay.oauth2.provider.web.internal.util.GenerateScopesTreeUtil;
import com.liferay.petra.string.StringPool;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
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
	public void testMultipleLevelScopeTree() {
		List<String> scopesList = Arrays.asList(
			"everything.read", "everything.write", "everything",
			"everything.read.user", "everything.read.user.documents");

		Tree.Node<String> root = GenerateScopesTreeUtil.generateScopesTree(
			new TreeSet<>(scopesList), _scopeMatcherFactory);

		Assert.assertEquals(StringPool.BLANK, root.getValue());

		Tree<String> node = _getFirstChild(root);

		Assert.assertEquals("everything", node.getValue());
		Assert.assertFalse(node instanceof Tree.Leaf);

		Tree<String> firstChild = _getFirstChild((Tree.Node<String>)node);

		Assert.assertEquals("everything.read", firstChild.getValue());
		Assert.assertFalse(firstChild instanceof Tree.Leaf);

		Tree<String> firstGrandChild = _getFirstChild(
			(Tree.Node<String>)firstChild);

		Assert.assertEquals("everything.read.user", firstGrandChild.getValue());
		Assert.assertFalse(firstGrandChild instanceof Tree.Leaf);
		Tree<String> greatGrandChild = _getFirstChild(
			(Tree.Node<String>)firstGrandChild);

		Assert.assertEquals(
			"everything.read.user.documents", greatGrandChild.getValue());

		Tree<String> lastChild = _getLastChild((Tree.Node<String>)node);

		Assert.assertEquals("everything.write", lastChild.getValue());
		Assert.assertTrue(lastChild instanceof Tree.Leaf);
	}

	@Test
	public void testMultipleParentsScopeTree() {
		List<String> scopesList = Arrays.asList(
			"everything.read", "everything.write", "everything",
			"everything.read.user", "analytics.read", "analytics");

		Tree.Node<String> root = GenerateScopesTreeUtil.generateScopesTree(
			new TreeSet<>(scopesList), _scopeMatcherFactory);

		Assert.assertEquals(StringPool.BLANK, root.getValue());

		Tree<String> firstNode = _getFirstChild(root);
		Tree<String> lastNode = _getLastChild(root);

		Assert.assertEquals("analytics", firstNode.getValue());
		Assert.assertFalse(firstNode instanceof Tree.Leaf);

		Tree<String> childFirstNode = _getFirstChild(
			(Tree.Node<String>)firstNode);

		Assert.assertEquals("analytics.read", childFirstNode.getValue());

		Assert.assertTrue(childFirstNode instanceof Tree.Leaf);

		Assert.assertEquals("everything", lastNode.getValue());
		Assert.assertFalse(lastNode instanceof Tree.Leaf);

		Tree<String> childLastNode = _getFirstChild(
			(Tree.Node<String>)lastNode);

		Assert.assertEquals("everything.read", childLastNode.getValue());
		Assert.assertFalse(childLastNode instanceof Tree.Leaf);
	}

	@Test
	public void testOneLevelScopeTree() {
		List<String> scopesList = Arrays.asList(
			"everything.read", "everything.write", "everything");

		Tree.Node<String> root = GenerateScopesTreeUtil.generateScopesTree(
			new TreeSet<>(scopesList), _scopeMatcherFactory);

		Assert.assertEquals(StringPool.BLANK, root.getValue());

		final Tree<String> child = _getFirstChild(root);

		Assert.assertEquals("everything", child.getValue());
		Assert.assertFalse(child instanceof Tree.Leaf);

		Tree<String> firstChild = _getFirstChild((Tree.Node<String>)child);

		Assert.assertEquals("everything.read", firstChild.getValue());
		Assert.assertTrue(firstChild instanceof Tree.Leaf);

		Tree<String> lastChild = _getLastChild((Tree.Node<String>)child);

		Assert.assertEquals("everything.write", lastChild.getValue());
		Assert.assertTrue(lastChild instanceof Tree.Leaf);
	}

	private Tree<String> _getFirstChild(Tree.Node<String> node) {
		final List<Tree<String>> children = node.getChildren();

		children.sort(Comparator.comparing(Tree::getValue));

		return children.get(0);
	}

	private Tree<String> _getLastChild(Tree.Node<String> node) {
		final List<Tree<String>> children = node.getChildren();

		children.sort(Comparator.comparing(Tree::getValue));

		return children.get(children.size() - 1);
	}

	private ScopeMatcherFactory _scopeMatcherFactory;

}
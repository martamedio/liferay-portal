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
import com.liferay.oauth2.provider.web.internal.util.ScopeTreeUtil;
import com.liferay.petra.string.StringPool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
	public void testMultipleLevelsScopeTree() {
		List<String> scopesList = Arrays.asList(
			"everything.read", "everything.write", "everything",
			"everything.read.user", "everything.read.user.documents");

		Tree.Node<String> root = ScopeTreeUtil.getScopeTreeNode(
			new TreeSet<>(scopesList), _scopeMatcherFactory);

		Assert.assertEquals(StringPool.BLANK, root.getValue());

		Tree<String> node = _getChild(root, 0);

		Assert.assertEquals("everything", node.getValue());
		Assert.assertFalse(node instanceof Tree.Leaf);

		Tree<String> firstChild = _getChild((Tree.Node<String>)node, 0);

		Assert.assertEquals("everything.read", firstChild.getValue());
		Assert.assertFalse(firstChild instanceof Tree.Leaf);

		Tree<String> firstGrandChild = _getChild(
			(Tree.Node<String>)firstChild, 0);

		Assert.assertEquals("everything.read.user", firstGrandChild.getValue());
		Assert.assertFalse(firstGrandChild instanceof Tree.Leaf);
		Tree<String> greatGrandChild = _getChild(
			(Tree.Node<String>)firstGrandChild, 0);

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

		Tree.Node<String> root = ScopeTreeUtil.getScopeTreeNode(
			new TreeSet<>(scopesList), _scopeMatcherFactory);

		Assert.assertEquals(StringPool.BLANK, root.getValue());

		Tree<String> firstNode = _getChild(root, 0);
		Tree<String> lastNode = _getLastChild(root);

		Assert.assertEquals("analytics", firstNode.getValue());
		Assert.assertFalse(firstNode instanceof Tree.Leaf);

		Tree<String> childFirstNode = _getChild(
			(Tree.Node<String>)firstNode, 0);

		Assert.assertEquals("analytics.read", childFirstNode.getValue());

		Assert.assertTrue(childFirstNode instanceof Tree.Leaf);

		Assert.assertEquals("everything", lastNode.getValue());
		Assert.assertFalse(lastNode instanceof Tree.Leaf);

		Tree<String> childLastNode = _getChild((Tree.Node<String>)lastNode, 0);

		Assert.assertEquals("everything.read", childLastNode.getValue());
		Assert.assertFalse(childLastNode instanceof Tree.Leaf);
	}

	@Test
	public void testOneLevelScopeTree() {
		List<String> scopesList = Arrays.asList(
			"everything.read", "everything.write", "everything");

		Tree.Node<String> root = ScopeTreeUtil.getScopeTreeNode(
			new TreeSet<>(scopesList), _scopeMatcherFactory);

		Assert.assertEquals(StringPool.BLANK, root.getValue());

		final Tree<String> child = _getChild(root, 0);

		Assert.assertEquals("everything", child.getValue());
		Assert.assertFalse(child instanceof Tree.Leaf);

		Tree<String> firstChild = _getChild((Tree.Node<String>)child, 0);

		Assert.assertEquals("everything.read", firstChild.getValue());
		Assert.assertTrue(firstChild instanceof Tree.Leaf);

		Tree<String> lastChild = _getLastChild((Tree.Node<String>)child);

		Assert.assertEquals("everything.write", lastChild.getValue());
		Assert.assertTrue(lastChild instanceof Tree.Leaf);
	}

	private Tree<String> _getChild(Tree.Node<String> node, int indexItem) {
		final List<Tree<String>> children = new ArrayList<>(node.getTrees());

		return children.get(indexItem);
	}

	private Tree<String> _getLastChild(Tree.Node<String> node) {
		final Collection<Tree<String>> children = node.getTrees();

		return _getChild(node, children.size() - 1);
	}

	private ScopeMatcherFactory _scopeMatcherFactory;

}
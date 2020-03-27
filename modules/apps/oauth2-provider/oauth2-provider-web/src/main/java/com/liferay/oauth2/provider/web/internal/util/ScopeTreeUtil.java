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
import com.liferay.oauth2.provider.web.internal.tree.Tree;
import com.liferay.oauth2.provider.web.internal.tree.util.TreeUtil;
import com.liferay.oauth2.provider.web.internal.tree.visitor.TreeVisitor;
import com.liferay.petra.string.StringPool;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;

/**
 * @author Carlos Sierra
 * @author Marta Medio
 */
public class ScopeTreeUtil {

	public static Tree.Node<String> getScopeTreeNode(
		Set<String> scopeAliases, ScopeMatcherFactory scopeMatcherFactory) {

		final HashMap<String, ScopeMatcher> scopeMatcherMap = new HashMap<>();

		final Tree.Node<String> treeNode = TreeUtil.getTreeNode(
			scopeAliases, StringPool.BLANK,
			(scopeAlias1, scopeAlias2) -> {
				final ScopeMatcher scopeMatcher =
					scopeMatcherMap.computeIfAbsent(
						scopeAlias1, scopeMatcherFactory::create);

				return scopeMatcher.match(scopeAlias2);
			});

		treeNode.accept(new SortTreeVisitor());

		return treeNode;
	}

	private static class SortTreeVisitor implements TreeVisitor<String> {

		@Override
		public void visitLeaf(Tree.Leaf<String> leaf) {
		}

		@Override
		public void visitNode(Tree.Node<String> node) {
			for (Tree<String> tree : node.getTrees()) {
				tree.accept(this);
			}

			Collections.sort(
				node.getTrees(),
				Comparator.comparing(
					Tree::getValue, String.CASE_INSENSITIVE_ORDER));
		}

	}

}
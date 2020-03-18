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

package com.liferay.oauth2.provider.web.internal.taglib;

import java.io.IOException;

import java.util.Collection;
import java.util.Deque;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.JspTag;

/**
 * @author Marta Medio
 */
public class RenderChildrenTag extends TreeTag {

	@Override
	public void doTag() throws IOException, JspException {
		final JspContext jspContext = getJspContext();

		Object parentsNodesObject = jspContext.getAttribute("parentNodes");

		if (!(parentsNodesObject instanceof Deque)) {
			throw new IllegalStateException(
				"Render children has to be used inside the body of a tree");
		}

		Deque<Tree.Node<?>> parentNodes =
			(Deque<Tree.Node<?>>)parentsNodesObject;

		try {
			parentNodes.push((Tree.Node<?>)jspContext.getAttribute("node"));

			for (Tree<?> tree : _trees) {
				renderTree(tree);
			}
		}
		finally {
			parentNodes.pop();
		}
	}

	@Override
	public JspFragment getLeafJspFragment() {
		if (leafJspFragment != null) {
			return leafJspFragment;
		}

		final JspTag jspTag = findAncestorWithClass(this, TreeTag.class);

		if (jspTag instanceof TreeTag) {
			TreeTag treeTag = (TreeTag)jspTag;

			return treeTag.getLeafJspFragment();
		}

		throw new IllegalStateException("Can not find leaf fragment");
	}

	@Override
	public JspFragment getNodeJspFragment() {
		if (nodeJspFragment != null) {
			return nodeJspFragment;
		}

		final JspTag jspTag = findAncestorWithClass(this, TreeTag.class);

		if (jspTag instanceof TreeTag) {
			TreeTag treeTag = (TreeTag)jspTag;

			return treeTag.getNodeJspFragment();
		}

		throw new IllegalStateException("Can not find node fragment");
	}

	public Collection<Tree<?>> getTrees() {
		return _trees;
	}

	public void setLeafJspFragment(JspFragment leafJspFragment) {
		this.leafJspFragment = leafJspFragment;
	}

	public void setNodeJspFragment(JspFragment nodeJspFragment) {
		this.nodeJspFragment = nodeJspFragment;
	}

	public void setTrees(Collection trees) {
		_trees = (Collection) trees;
	}

	protected JspFragment leafJspFragment;
	protected JspFragment nodeJspFragment;

	private Collection<Tree<?>> _trees;

}
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
import java.util.LinkedList;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * @author Marta Medio
 */
public class TreeTag extends SimpleTagSupport {

	@Override
	public void doTag() throws IOException, JspException {
		final JspContext jspContext = getJspContext();

		Object parentsObject = jspContext.getAttribute("parents");

		if (!(parentsObject instanceof Deque)) {
			parentsObject = new LinkedList<>();

			jspContext.setAttribute("parents", parentsObject);
		}
		else {
			Deque<Tree.Node<?>> parents = (Deque<Tree.Node<?>>)parentsObject;

			parents.push((Tree.Node<?>)jspContext.getAttribute("node"));
		}

		for (Tree<?> tree : _trees) {
			_renderTree(tree);
		}

		Deque<Tree.Node<?>> parents = (Deque<Tree.Node<?>>)parentsObject;

		if (parents.isEmpty()) {
			jspContext.removeAttribute("parents");
		}
		else {
			parents.pop();
		}
	}

	public JspFragment getLeafJspFragment() {
		return _leafJspFragment;
	}

	public JspFragment getNodeJspFragment() {
		return _nodeJspFragment;
	}

	public Collection<Tree<?>> getTrees() {
		return _trees;
	}

	public void setLeafJspFragment(JspFragment leafJspFragment) {
		_leafJspFragment = leafJspFragment;
	}

	public void setNodeJspFragment(JspFragment nodeJspFragment) {
		_nodeJspFragment = nodeJspFragment;
	}

	public void setTrees(Collection<Tree<?>> trees) {
		_trees = trees;
	}

	private JspFragment _getLeafJspFragment() {
		if (_leafJspFragment != null) {
			return _leafJspFragment;
		}

		JspTag currentTag = this;

		while (currentTag != null) {
			currentTag = findAncestorWithClass(currentTag, TreeTag.class);

			if (currentTag instanceof TreeTag) {
				TreeTag treeTag = (TreeTag)currentTag;

				if (treeTag._leafJspFragment != null) {
					return treeTag._leafJspFragment;
				}
			}
		}

		throw new IllegalStateException("Can not find leaf fragment");
	}

	private JspFragment _getNodeJspFragment() {
		if (_nodeJspFragment != null) {
			return _nodeJspFragment;
		}

		JspTag currentTag = this;

		while (currentTag != null) {
			currentTag = findAncestorWithClass(currentTag, TreeTag.class);

			if (currentTag instanceof TreeTag) {
				TreeTag treeTag = (TreeTag)currentTag;

				if (treeTag._nodeJspFragment != null) {
					return treeTag._nodeJspFragment;
				}
			}
		}

		throw new IllegalStateException("Can not find node fragment");
	}

	private void _renderTree(Tree<?> tree) throws IOException, JspException {
		final JspContext jspContext = getJspContext();

		final Object currentNode = jspContext.getAttribute("node");

		jspContext.setAttribute("node", tree);

		if (tree instanceof Tree.Leaf) {
			_getLeafJspFragment().invoke(jspContext.getOut());
		}
		else {
			_getNodeJspFragment().invoke(jspContext.getOut());
		}

		jspContext.setAttribute("node", currentNode);
	}

	private JspFragment _leafJspFragment;
	private JspFragment _nodeJspFragment;
	private Collection<Tree<?>> _trees;

}
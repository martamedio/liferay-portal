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
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * @author Marta Medio
 */
public class TreeTag extends SimpleTagSupport {

	@Override
	public void doTag() throws IOException, JspException {
		for (Tree<?> tree : _nodes) {
			_renderTree(tree, new LinkedList<>());
		}
	}

	public JspFragment getAfterParent() {
		return _afterParent;
	}

	public JspFragment getBeforeParent() {
		return _beforeParent;
	}

	public JspFragment getLeaf() {
		return _leaf;
	}

	public Collection<Tree.Node<?>> getNodes() {
		return _nodes;
	}

	public void setAfterParent(JspFragment afterParent) {
		_afterParent = afterParent;
	}

	public void setBeforeParent(JspFragment beforeParent) {
		_beforeParent = beforeParent;
	}

	public void setLeaf(JspFragment leaf) {
		_leaf = leaf;
	}

	public void setNodes(Collection<Tree.Node<?>> nodes) {
		_nodes = nodes;
	}

	private void _invokeAfterParent() throws IOException, JspException {
		if (_afterParent != null) {
			_afterParent.invoke(getJspContext().getOut());
		}
	}

	private void _invokeBeforeParent() throws IOException, JspException {
		if (_beforeParent != null) {
			_beforeParent.invoke(getJspContext().getOut());
		}
	}

	private void _renderTree(Tree<?> tree, Deque<Tree.Node<?>> parents)
		throws IOException, JspException {

		final JspContext jspContext = getJspContext();

		jspContext.setAttribute("node", tree);
		jspContext.setAttribute("parents", parents);

		if (tree instanceof Tree.Leaf) {
			_leaf.invoke(jspContext.getOut());
		}
		else {
			Tree.Node<?> node = (Tree.Node<?>)tree;

			_invokeBeforeParent();

			parents.push(node);

			for (Tree<?> children : node.getChildren()) {
				_renderTree(children, parents);
			}

			parents.pop();

			_invokeAfterParent();
		}
	}

	private JspFragment _afterParent;
	private JspFragment _beforeParent;
	private JspFragment _leaf;
	private Collection<Tree.Node<?>> _nodes;

}
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

	public JspFragment getAfterParentJspFragment() {
		return _afterParentJspFragment;
	}

	public JspFragment getBeforeParentJspFragment() {
		return _beforeParentJspFragment;
	}

	public JspFragment getLeafJspFragment() {
		return _leafJspFragment;
	}

	public Collection<Tree.Node<?>> getNodes() {
		return _nodes;
	}

	public void setAfterParentJspFragment(JspFragment afterParentJspFragment) {
		_afterParentJspFragment = afterParentJspFragment;
	}

	public void setBeforeParentJspFragment(
		JspFragment beforeParentJspFragment) {

		_beforeParentJspFragment = beforeParentJspFragment;
	}

	public void setLeafJspFragment(JspFragment leafJspFragment) {
		_leafJspFragment = leafJspFragment;
	}

	public void setNodes(Collection<Tree.Node<?>> nodes) {
		_nodes = nodes;
	}

	private void _invokeAfterParent() throws IOException, JspException {
		if (_afterParentJspFragment != null) {
			_afterParentJspFragment.invoke(getJspContext().getOut());
		}
	}

	private void _invokeBeforeParent() throws IOException, JspException {
		if (_beforeParentJspFragment != null) {
			_beforeParentJspFragment.invoke(getJspContext().getOut());
		}
	}

	private void _renderTree(Tree<?> tree, Deque<Tree.Node<?>> parents)
		throws IOException, JspException {

		final JspContext jspContext = getJspContext();

		jspContext.setAttribute("node", tree);
		jspContext.setAttribute("parents", parents);

		if (tree instanceof Tree.Leaf) {
			_leafJspFragment.invoke(jspContext.getOut());
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

	private JspFragment _afterParentJspFragment;
	private JspFragment _beforeParentJspFragment;
	private JspFragment _leafJspFragment;
	private Collection<Tree.Node<?>> _nodes;

}
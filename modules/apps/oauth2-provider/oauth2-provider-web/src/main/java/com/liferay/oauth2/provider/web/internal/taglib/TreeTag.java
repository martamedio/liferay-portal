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
		for (Tree<String> tree : root.getChildren()) {
			_renderTree(tree, new LinkedList<>());
		}
	}

	public JspFragment getAfterParent() {
		return afterParent;
	}

	public JspFragment getBeforeParent() {
		return beforeParent;
	}

	public JspFragment getLeaf() {
		return leaf;
	}

	public Tree.Node getRoot() {
		return root;
	}

	public void setAfterParent(JspFragment afterParent) {
		this.afterParent = afterParent;
	}

	public void setBeforeParent(JspFragment beforeParent) {
		this.beforeParent = beforeParent;
	}

	public void setLeaf(JspFragment leaf) {
		this.leaf = leaf;
	}

	public void setRoot(Tree.Node root) {
		this.root = root;
	}

	public JspFragment afterParent;
	public JspFragment beforeParent;
	public JspFragment leaf;
	public Tree.Node<String> root;

	private void _invokeAfterParent() throws IOException, JspException {
		if (afterParent != null) {
			afterParent.invoke(getJspContext().getOut());
		}
	}

	private void _invokeBeforeParent() throws IOException, JspException {
		if (beforeParent != null) {
			beforeParent.invoke(getJspContext().getOut());
		}
	}

	private void _renderTree(
			Tree<String> tree, Deque<Tree.Node<String>> parents)
		throws IOException, JspException {

		final JspContext jspContext = getJspContext();

		jspContext.setAttribute("node", tree);
		jspContext.setAttribute("parents", parents);

		if (tree instanceof Tree.Leaf) {
			leaf.invoke(jspContext.getOut());
		}
		else {
			Tree.Node<String> node = (Tree.Node<String>)tree;

			_invokeBeforeParent();

			parents.push(node);

			for (Tree<String> children : node.getChildren()) {
				_renderTree(children, parents);
			}

			parents.pop();

			_invokeAfterParent();
		}
	}

}
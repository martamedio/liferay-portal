/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 * <p>
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */

package com.liferay.oauth2.provider.web.internal.tree.visitor;

import com.liferay.oauth2.provider.web.internal.tree.Tree;

/**
 * @author Carlos Sierra Andrés
 */
public interface TreeVisitor<T> {

	public void visitLeaf(Tree.Leaf<T> leaf);

	public void visitNode(Tree.Node<T> node);

}
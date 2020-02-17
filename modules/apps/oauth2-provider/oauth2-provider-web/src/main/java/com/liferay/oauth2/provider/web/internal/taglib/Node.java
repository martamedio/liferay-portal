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

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author Marta Medio
 */
public class Node implements Comparable<Node> {

	public Node(String value) {
		_value = value;
		_nodes = new TreeSet<>();
	}

	public void addChildren(Node node) {
		_nodes.add(node);
	}

	@Override
	public int compareTo(Node o) {
		return _value.compareToIgnoreCase(o.getValue());
	}

	public Node findChildrenByValue(String value) {
		for (Node node : _nodes) {
			String nodeValue = node.getValue();

			if (nodeValue.equals(value)) {
				return node;
			}
		}

		return null;
	}

	public SortedSet<Node> getNodes() {
		return _nodes;
	}

	public String getValue() {
		return _value;
	}

	public boolean isLeaf() {
		return _nodes.isEmpty();
	}

	public void setNodes(TreeSet<Node> nodes) {
		_nodes = nodes;
	}

	private SortedSet<Node> _nodes;
	private final String _value;

}
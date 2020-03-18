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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Marta Medio
 */
public interface Tree<T> {

	public T getValue();

	public static final class Leaf<T> implements Tree<T> {

		public Leaf(T value) {
			_value = value;
		}

		@Override
		public T getValue() {
			return _value;
		}

		private T _value;

	}

	public static final class Node<T> implements Tree<T> {

		public Node(T value, List<Tree<T>> trees) {
			_value = value;
			_trees = Collections.unmodifiableList(trees);
		}

		public Node(T value, Tree<T>... trees) {
			this(value, Arrays.asList(trees));
		}

		public Collection<Tree<T>> getTrees() {
			return _trees;
		}

		@Override
		public T getValue() {
			return _value;
		}

		private final List<Tree<T>> _trees;
		private T _value;

	}

}
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

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * @author Marta Medio
 */
public class ScopesTreeTag extends SimpleTagSupport {

	@Override
	public void doTag() throws IOException, JspException {
		Deque<Event<Map.Entry<String, Map<String, Map>>>> deque =
			new LinkedList<>();

		List<Event<Map.Entry<String, Map<String, Map>>>> eventList =
			new ArrayList<>();

		Set<Map.Entry<String, HashMap<String, Map>>> entries =
			scopesMap.entrySet();

		Stream<Map.Entry<String, HashMap<String, Map>>> stream =
			entries.stream();

		stream.map(
			Node::new
		).forEach(
			((Deque)deque)::addFirst
		);

		while (true) {
			Event<Map.Entry<String, Map<String, Map>>> eventPoll = deque.poll();

			if (eventPoll == null)

				break;

			if (eventPoll instanceof BeforeParent) {
				eventList.add(eventPoll);
			}
			else if (eventPoll instanceof Node) {
				Map.Entry<String, Map<String, Map>> event = eventPoll.event;

				Map<String, Map> value = event.getValue();

				if (value.isEmpty()) {
					eventList.add(eventPoll);
				}
				else {
					deque.addFirst(new AfterParent<>(event));

					Set<Map.Entry<String, Map>> entrySet = value.entrySet();

					Stream<Map.Entry<String, Map>> streamEntry =
						entrySet.stream();

					streamEntry.map(
						Node::new
					).forEach(
						((Deque)deque)::addFirst
					);

					deque.addFirst(new BeforeParent(event));
				}
			}
			else if (eventPoll instanceof AfterParent) {
				eventList.add(eventPoll);
			}
		}

		LinkedList<Object> parents = new LinkedList<>();

		getJspContext().setAttribute("parents", parents);

		for (Event event : eventList) {
			getJspContext().setAttribute("node", event.event);

			if (event instanceof BeforeParent) {
				beforeParent.invoke(getJspContext().getOut());
				parents.push(event.event);
			}
			else if (event instanceof Node) {
				leaf.invoke(getJspContext().getOut());
			}
			else if (event instanceof AfterParent) {
				parents.pop();
			}
		}
	}

	public JspFragment getBeforeParent() {
		return beforeParent;
	}

	public JspFragment getLeaf() {
		return leaf;
	}

	public Map<String, HashMap<String, Map>> getScopesMap() {
		return scopesMap;
	}

	public void setBeforeParent(JspFragment beforeParent) {
		this.beforeParent = beforeParent;
	}

	public void setLeaf(JspFragment leaf) {
		this.leaf = leaf;
	}

	public void setScopesMap(Map<String, HashMap<String, Map>> scopesMap) {
		this.scopesMap = scopesMap;
	}

	public JspFragment beforeParent;
	public JspFragment leaf;
	public Map<String, HashMap<String, Map>> scopesMap;

	private class AfterParent<T> extends Event<T> {

		public AfterParent(T event) {
			super(event);
		}

	}

	private class BeforeParent<T> extends Event<T> {

		public BeforeParent(T event) {
			super(event);
		}

	}

	private abstract class Event<T> {

		public Event(T event) {
			this.event = event;
		}

		public T event;

	}

	private class Node<T> extends Event<T> {

		public Node(T event) {
			super(event);
		}

	}

}
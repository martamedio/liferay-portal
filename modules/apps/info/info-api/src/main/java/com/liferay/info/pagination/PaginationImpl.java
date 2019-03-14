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

package com.liferay.info.pagination;

/**
 * @author Jorge Ferrer
 */
public class PaginationImpl implements Pagination {

	public PaginationImpl(int start, int end) {
		_start = start;
		_end = end;
	}

	@Override
	public int getEnd() {
		return _end;
	}

	@Override
	public int getDelta() {
		return _end - _start;
	}

	@Override
	public int getStart() {
		return _start;
	}

	private final int _end;
	private final int _start;

}
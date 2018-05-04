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

package com.liferay.document.library.uad.display;

import com.liferay.document.library.kernel.model.DLFolder;
import com.liferay.document.library.kernel.service.DLFolderLocalService;
import com.liferay.document.library.uad.constants.DLUADConstants;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.user.associated.data.display.BaseModelUADDisplay;

import java.io.Serializable;

import java.util.List;
import java.util.Locale;

import org.osgi.service.component.annotations.Reference;

/**
 * Provides the base implementation for the Document Library Folder UAD display.
 *
 * <p>
 * This implementation exists only as a container for the default methods
 * generated by ServiceBuilder. All custom methods should be put in
 * {@link DLFolderUADDisplay}.
 * </p>
 * @author William Newbury
 */
public abstract class BaseDLFolderUADDisplay
	extends BaseModelUADDisplay<DLFolder> {

	@Override
	public DLFolder get(Serializable primaryKey) throws PortalException {
		return dlFolderLocalService.getDLFolder(
			Long.valueOf(primaryKey.toString()));
	}

	public String getApplicationName() {
		return DLUADConstants.APPLICATION_NAME;
	}

	public String[] getDisplayFieldNames() {
		return new String[] {"name", "description"};
	}

	@Override
	public String getKey() {
		return DLUADConstants.CLASS_NAME_DL_FOLDER;
	}

	@Override
	public String getTypeName(Locale locale) {
		return "DLFolder";
	}

	@Override
	protected long doCount(DynamicQuery dynamicQuery) {
		return dlFolderLocalService.dynamicQueryCount(dynamicQuery);
	}

	@Override
	protected DynamicQuery doGetDynamicQuery() {
		return dlFolderLocalService.dynamicQuery();
	}

	@Override
	protected List<DLFolder> doGetRange(
		DynamicQuery dynamicQuery, int start, int end) {

		return dlFolderLocalService.dynamicQuery(dynamicQuery, start, end);
	}

	@Override
	protected String[] doGetUserIdFieldNames() {
		return DLUADConstants.USER_ID_FIELD_NAMES_DL_FOLDER;
	}

	@Reference
	protected DLFolderLocalService dlFolderLocalService;

}
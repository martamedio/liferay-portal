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

package com.liferay.change.tracking.model;

import aQute.bnd.annotation.ProviderType;

import com.liferay.expando.kernel.model.ExpandoBridge;

import com.liferay.portal.kernel.bean.AutoEscape;
import com.liferay.portal.kernel.model.AttachedModel;
import com.liferay.portal.kernel.model.AuditedModel;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.ResourcedModel;
import com.liferay.portal.kernel.model.ShardedModel;
import com.liferay.portal.kernel.service.ServiceContext;

import java.io.Serializable;

import java.util.Date;

/**
 * The base model interface for the CTEntry service. Represents a row in the &quot;CTEntry&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation {@link com.liferay.change.tracking.model.impl.CTEntryModelImpl} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link com.liferay.change.tracking.model.impl.CTEntryImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see CTEntry
 * @see com.liferay.change.tracking.model.impl.CTEntryImpl
 * @see com.liferay.change.tracking.model.impl.CTEntryModelImpl
 * @generated
 */
@ProviderType
public interface CTEntryModel extends AttachedModel, AuditedModel,
	BaseModel<CTEntry>, ResourcedModel, ShardedModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a ct entry model instance should use the {@link CTEntry} interface instead.
	 */

	/**
	 * Returns the primary key of this ct entry.
	 *
	 * @return the primary key of this ct entry
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this ct entry.
	 *
	 * @param primaryKey the primary key of this ct entry
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the ct entry ID of this ct entry.
	 *
	 * @return the ct entry ID of this ct entry
	 */
	public long getCtEntryId();

	/**
	 * Sets the ct entry ID of this ct entry.
	 *
	 * @param ctEntryId the ct entry ID of this ct entry
	 */
	public void setCtEntryId(long ctEntryId);

	/**
	 * Returns the company ID of this ct entry.
	 *
	 * @return the company ID of this ct entry
	 */
	@Override
	public long getCompanyId();

	/**
	 * Sets the company ID of this ct entry.
	 *
	 * @param companyId the company ID of this ct entry
	 */
	@Override
	public void setCompanyId(long companyId);

	/**
	 * Returns the user ID of this ct entry.
	 *
	 * @return the user ID of this ct entry
	 */
	@Override
	public long getUserId();

	/**
	 * Sets the user ID of this ct entry.
	 *
	 * @param userId the user ID of this ct entry
	 */
	@Override
	public void setUserId(long userId);

	/**
	 * Returns the user uuid of this ct entry.
	 *
	 * @return the user uuid of this ct entry
	 */
	@Override
	public String getUserUuid();

	/**
	 * Sets the user uuid of this ct entry.
	 *
	 * @param userUuid the user uuid of this ct entry
	 */
	@Override
	public void setUserUuid(String userUuid);

	/**
	 * Returns the user name of this ct entry.
	 *
	 * @return the user name of this ct entry
	 */
	@AutoEscape
	@Override
	public String getUserName();

	/**
	 * Sets the user name of this ct entry.
	 *
	 * @param userName the user name of this ct entry
	 */
	@Override
	public void setUserName(String userName);

	/**
	 * Returns the create date of this ct entry.
	 *
	 * @return the create date of this ct entry
	 */
	@Override
	public Date getCreateDate();

	/**
	 * Sets the create date of this ct entry.
	 *
	 * @param createDate the create date of this ct entry
	 */
	@Override
	public void setCreateDate(Date createDate);

	/**
	 * Returns the modified date of this ct entry.
	 *
	 * @return the modified date of this ct entry
	 */
	@Override
	public Date getModifiedDate();

	/**
	 * Sets the modified date of this ct entry.
	 *
	 * @param modifiedDate the modified date of this ct entry
	 */
	@Override
	public void setModifiedDate(Date modifiedDate);

	/**
	 * Returns the fully qualified class name of this ct entry.
	 *
	 * @return the fully qualified class name of this ct entry
	 */
	@Override
	public String getClassName();

	public void setClassName(String className);

	/**
	 * Returns the class name ID of this ct entry.
	 *
	 * @return the class name ID of this ct entry
	 */
	@Override
	public long getClassNameId();

	/**
	 * Sets the class name ID of this ct entry.
	 *
	 * @param classNameId the class name ID of this ct entry
	 */
	@Override
	public void setClassNameId(long classNameId);

	/**
	 * Returns the class pk of this ct entry.
	 *
	 * @return the class pk of this ct entry
	 */
	@Override
	public long getClassPK();

	/**
	 * Sets the class pk of this ct entry.
	 *
	 * @param classPK the class pk of this ct entry
	 */
	@Override
	public void setClassPK(long classPK);

	/**
	 * Returns the resource prim key of this ct entry.
	 *
	 * @return the resource prim key of this ct entry
	 */
	@Override
	public long getResourcePrimKey();

	/**
	 * Sets the resource prim key of this ct entry.
	 *
	 * @param resourcePrimKey the resource prim key of this ct entry
	 */
	@Override
	public void setResourcePrimKey(long resourcePrimKey);

	@Override
	public boolean isResourceMain();

	@Override
	public boolean isNew();

	@Override
	public void setNew(boolean n);

	@Override
	public boolean isCachedModel();

	@Override
	public void setCachedModel(boolean cachedModel);

	@Override
	public boolean isEscapedModel();

	@Override
	public Serializable getPrimaryKeyObj();

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj);

	@Override
	public ExpandoBridge getExpandoBridge();

	@Override
	public void setExpandoBridgeAttributes(BaseModel<?> baseModel);

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge);

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext);

	@Override
	public Object clone();

	@Override
	public int compareTo(CTEntry ctEntry);

	@Override
	public int hashCode();

	@Override
	public CacheModel<CTEntry> toCacheModel();

	@Override
	public CTEntry toEscapedModel();

	@Override
	public CTEntry toUnescapedModel();

	@Override
	public String toString();

	@Override
	public String toXmlString();
}
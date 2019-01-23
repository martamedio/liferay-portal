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

package com.bemis.portal.twofa.device.manager.model;

import aQute.bnd.annotation.ProviderType;

import com.liferay.expando.kernel.model.ExpandoBridge;

import com.liferay.portal.kernel.bean.AutoEscape;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.GroupedModel;
import com.liferay.portal.kernel.model.ShardedModel;
import com.liferay.portal.kernel.service.ServiceContext;

import java.io.Serializable;

import java.util.Date;

/**
 * The base model interface for the DeviceCode service. Represents a row in the &quot;Bemis_DeviceCode&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation {@link com.bemis.portal.twofa.device.manager.model.impl.DeviceCodeModelImpl} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link com.bemis.portal.twofa.device.manager.model.impl.DeviceCodeImpl}.
 * </p>
 *
 * @author Prathima Shreenath
 * @see DeviceCode
 * @see com.bemis.portal.twofa.device.manager.model.impl.DeviceCodeImpl
 * @see com.bemis.portal.twofa.device.manager.model.impl.DeviceCodeModelImpl
 * @generated
 */
@ProviderType
public interface DeviceCodeModel extends BaseModel<DeviceCode>, GroupedModel,
	ShardedModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a device code model instance should use the {@link DeviceCode} interface instead.
	 */

	/**
	 * Returns the primary key of this device code.
	 *
	 * @return the primary key of this device code
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this device code.
	 *
	 * @param primaryKey the primary key of this device code
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the device code ID of this device code.
	 *
	 * @return the device code ID of this device code
	 */
	public long getDeviceCodeId();

	/**
	 * Sets the device code ID of this device code.
	 *
	 * @param deviceCodeId the device code ID of this device code
	 */
	public void setDeviceCodeId(long deviceCodeId);

	/**
	 * Returns the group ID of this device code.
	 *
	 * @return the group ID of this device code
	 */
	@Override
	public long getGroupId();

	/**
	 * Sets the group ID of this device code.
	 *
	 * @param groupId the group ID of this device code
	 */
	@Override
	public void setGroupId(long groupId);

	/**
	 * Returns the company ID of this device code.
	 *
	 * @return the company ID of this device code
	 */
	@Override
	public long getCompanyId();

	/**
	 * Sets the company ID of this device code.
	 *
	 * @param companyId the company ID of this device code
	 */
	@Override
	public void setCompanyId(long companyId);

	/**
	 * Returns the user ID of this device code.
	 *
	 * @return the user ID of this device code
	 */
	@Override
	public long getUserId();

	/**
	 * Sets the user ID of this device code.
	 *
	 * @param userId the user ID of this device code
	 */
	@Override
	public void setUserId(long userId);

	/**
	 * Returns the user uuid of this device code.
	 *
	 * @return the user uuid of this device code
	 */
	@Override
	public String getUserUuid();

	/**
	 * Sets the user uuid of this device code.
	 *
	 * @param userUuid the user uuid of this device code
	 */
	@Override
	public void setUserUuid(String userUuid);

	/**
	 * Returns the user name of this device code.
	 *
	 * @return the user name of this device code
	 */
	@AutoEscape
	@Override
	public String getUserName();

	/**
	 * Sets the user name of this device code.
	 *
	 * @param userName the user name of this device code
	 */
	@Override
	public void setUserName(String userName);

	/**
	 * Returns the create date of this device code.
	 *
	 * @return the create date of this device code
	 */
	@Override
	public Date getCreateDate();

	/**
	 * Sets the create date of this device code.
	 *
	 * @param createDate the create date of this device code
	 */
	@Override
	public void setCreateDate(Date createDate);

	/**
	 * Returns the modified date of this device code.
	 *
	 * @return the modified date of this device code
	 */
	@Override
	public Date getModifiedDate();

	/**
	 * Sets the modified date of this device code.
	 *
	 * @param modifiedDate the modified date of this device code
	 */
	@Override
	public void setModifiedDate(Date modifiedDate);

	/**
	 * Returns the portal user ID of this device code.
	 *
	 * @return the portal user ID of this device code
	 */
	public long getPortalUserId();

	/**
	 * Sets the portal user ID of this device code.
	 *
	 * @param portalUserId the portal user ID of this device code
	 */
	public void setPortalUserId(long portalUserId);

	/**
	 * Returns the portal user uuid of this device code.
	 *
	 * @return the portal user uuid of this device code
	 */
	public String getPortalUserUuid();

	/**
	 * Sets the portal user uuid of this device code.
	 *
	 * @param portalUserUuid the portal user uuid of this device code
	 */
	public void setPortalUserUuid(String portalUserUuid);

	/**
	 * Returns the portal user name of this device code.
	 *
	 * @return the portal user name of this device code
	 */
	@AutoEscape
	public String getPortalUserName();

	/**
	 * Sets the portal user name of this device code.
	 *
	 * @param portalUserName the portal user name of this device code
	 */
	public void setPortalUserName(String portalUserName);

	/**
	 * Returns the email address of this device code.
	 *
	 * @return the email address of this device code
	 */
	@AutoEscape
	public String getEmailAddress();

	/**
	 * Sets the email address of this device code.
	 *
	 * @param emailAddress the email address of this device code
	 */
	public void setEmailAddress(String emailAddress);

	/**
	 * Returns the device code of this device code.
	 *
	 * @return the device code of this device code
	 */
	@AutoEscape
	public String getDeviceCode();

	/**
	 * Sets the device code of this device code.
	 *
	 * @param deviceCode the device code of this device code
	 */
	public void setDeviceCode(String deviceCode);

	/**
	 * Returns the device i p of this device code.
	 *
	 * @return the device i p of this device code
	 */
	@AutoEscape
	public String getDeviceIP();

	/**
	 * Sets the device i p of this device code.
	 *
	 * @param deviceIP the device i p of this device code
	 */
	public void setDeviceIP(String deviceIP);

	/**
	 * Returns the validation code of this device code.
	 *
	 * @return the validation code of this device code
	 */
	public int getValidationCode();

	/**
	 * Sets the validation code of this device code.
	 *
	 * @param validationCode the validation code of this device code
	 */
	public void setValidationCode(int validationCode);

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
	public int compareTo(DeviceCode deviceCode);

	@Override
	public int hashCode();

	@Override
	public CacheModel<DeviceCode> toCacheModel();

	@Override
	public DeviceCode toEscapedModel();

	@Override
	public DeviceCode toUnescapedModel();

	@Override
	public String toString();

	@Override
	public String toXmlString();
}
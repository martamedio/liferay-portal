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

package com.bemis.portal.twofa.device.manager.service;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link DeviceLocalService}.
 *
 * @author Prathima Shreenath
 * @see DeviceLocalService
 * @generated
 */
@ProviderType
public class DeviceLocalServiceWrapper implements DeviceLocalService,
	ServiceWrapper<DeviceLocalService> {
	public DeviceLocalServiceWrapper(DeviceLocalService deviceLocalService) {
		_deviceLocalService = deviceLocalService;
	}

	/**
	* Adds the device to the database. Also notifies the appropriate model listeners.
	*
	* @param device the device
	* @return the device that was added
	*/
	@Override
	public com.bemis.portal.twofa.device.manager.model.Device addDevice(
		com.bemis.portal.twofa.device.manager.model.Device device) {
		return _deviceLocalService.addDevice(device);
	}

	@Override
	public boolean checkDeviceExistsForThisUser(long userId, String deviceIP) {
		return _deviceLocalService.checkDeviceExistsForThisUser(userId, deviceIP);
	}

	/**
	* Creates a new device with the primary key. Does not add the device to the database.
	*
	* @param deviceId the primary key for the new device
	* @return the new device
	*/
	@Override
	public com.bemis.portal.twofa.device.manager.model.Device createDevice(
		long deviceId) {
		return _deviceLocalService.createDevice(deviceId);
	}

	/**
	* Deletes the device from the database. Also notifies the appropriate model listeners.
	*
	* @param device the device
	* @return the device that was removed
	*/
	@Override
	public com.bemis.portal.twofa.device.manager.model.Device deleteDevice(
		com.bemis.portal.twofa.device.manager.model.Device device) {
		return _deviceLocalService.deleteDevice(device);
	}

	/**
	* Deletes the device with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param deviceId the primary key of the device
	* @return the device that was removed
	* @throws PortalException if a device with the primary key could not be found
	*/
	@Override
	public com.bemis.portal.twofa.device.manager.model.Device deleteDevice(
		long deviceId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _deviceLocalService.deleteDevice(deviceId);
	}

	/**
	* @throws PortalException
	*/
	@Override
	public com.liferay.portal.kernel.model.PersistedModel deletePersistedModel(
		com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _deviceLocalService.deletePersistedModel(persistedModel);
	}

	@Override
	public void deleteUnauthorizedDevice(long userId) {
		_deviceLocalService.deleteUnauthorizedDevice(userId);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return _deviceLocalService.dynamicQuery();
	}

	/**
	* Performs a dynamic query on the database and returns the matching rows.
	*
	* @param dynamicQuery the dynamic query
	* @return the matching rows
	*/
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {
		return _deviceLocalService.dynamicQuery(dynamicQuery);
	}

	/**
	* Performs a dynamic query on the database and returns a range of the matching rows.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.bemis.portal.twofa.device.manager.model.impl.DeviceModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param dynamicQuery the dynamic query
	* @param start the lower bound of the range of model instances
	* @param end the upper bound of the range of model instances (not inclusive)
	* @return the range of matching rows
	*/
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) {
		return _deviceLocalService.dynamicQuery(dynamicQuery, start, end);
	}

	/**
	* Performs a dynamic query on the database and returns an ordered range of the matching rows.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.bemis.portal.twofa.device.manager.model.impl.DeviceModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param dynamicQuery the dynamic query
	* @param start the lower bound of the range of model instances
	* @param end the upper bound of the range of model instances (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching rows
	*/
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<T> orderByComparator) {
		return _deviceLocalService.dynamicQuery(dynamicQuery, start, end,
			orderByComparator);
	}

	/**
	* Returns the number of rows matching the dynamic query.
	*
	* @param dynamicQuery the dynamic query
	* @return the number of rows matching the dynamic query
	*/
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {
		return _deviceLocalService.dynamicQueryCount(dynamicQuery);
	}

	/**
	* Returns the number of rows matching the dynamic query.
	*
	* @param dynamicQuery the dynamic query
	* @param projection the projection to apply to the query
	* @return the number of rows matching the dynamic query
	*/
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {
		return _deviceLocalService.dynamicQueryCount(dynamicQuery, projection);
	}

	@Override
	public com.bemis.portal.twofa.device.manager.model.Device fetchDevice(
		long deviceId) {
		return _deviceLocalService.fetchDevice(deviceId);
	}

	@Override
	public com.bemis.portal.twofa.device.manager.model.Device fetchDeviceByUserAndDeviceIP(
		long userId, String deviceIP) {
		return _deviceLocalService.fetchDeviceByUserAndDeviceIP(userId, deviceIP);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery getActionableDynamicQuery() {
		return _deviceLocalService.getActionableDynamicQuery();
	}

	/**
	* Returns the device with the primary key.
	*
	* @param deviceId the primary key of the device
	* @return the device
	* @throws PortalException if a device with the primary key could not be found
	*/
	@Override
	public com.bemis.portal.twofa.device.manager.model.Device getDevice(
		long deviceId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _deviceLocalService.getDevice(deviceId);
	}

	/**
	* Returns a range of all the devices.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.bemis.portal.twofa.device.manager.model.impl.DeviceModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of devices
	* @param end the upper bound of the range of devices (not inclusive)
	* @return the range of devices
	*/
	@Override
	public java.util.List<com.bemis.portal.twofa.device.manager.model.Device> getDevices(
		int start, int end) {
		return _deviceLocalService.getDevices(start, end);
	}

	/**
	* Returns the number of devices.
	*
	* @return the number of devices
	*/
	@Override
	public int getDevicesCount() {
		return _deviceLocalService.getDevicesCount();
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery getIndexableActionableDynamicQuery() {
		return _deviceLocalService.getIndexableActionableDynamicQuery();
	}

	/**
	* Returns the OSGi service identifier.
	*
	* @return the OSGi service identifier
	*/
	@Override
	public String getOSGiServiceIdentifier() {
		return _deviceLocalService.getOSGiServiceIdentifier();
	}

	@Override
	public com.liferay.portal.kernel.model.PersistedModel getPersistedModel(
		java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _deviceLocalService.getPersistedModel(primaryKeyObj);
	}

	/**
	* Sets registered device as verified
	*
	* @param userId
	*/
	@Override
	public void registerAsTempDevice(long userId) {
		_deviceLocalService.registerAsTempDevice(userId);
	}

	/**
	* Sets registered device as verified
	*
	* @param userId
	*/
	@Override
	public void registerAsVerifiedDevice(long userId) {
		_deviceLocalService.registerAsVerifiedDevice(userId);
	}

	@Override
	public com.bemis.portal.twofa.device.manager.model.Device registerDevice(
		com.bemis.portal.twofa.device.manager.model.DeviceInfo deviceInfo)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _deviceLocalService.registerDevice(deviceInfo);
	}

	/**
	* Updates the device in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	*
	* @param device the device
	* @return the device that was updated
	*/
	@Override
	public com.bemis.portal.twofa.device.manager.model.Device updateDevice(
		com.bemis.portal.twofa.device.manager.model.Device device) {
		return _deviceLocalService.updateDevice(device);
	}

	@Override
	public DeviceLocalService getWrappedService() {
		return _deviceLocalService;
	}

	@Override
	public void setWrappedService(DeviceLocalService deviceLocalService) {
		_deviceLocalService = deviceLocalService;
	}

	private DeviceLocalService _deviceLocalService;
}
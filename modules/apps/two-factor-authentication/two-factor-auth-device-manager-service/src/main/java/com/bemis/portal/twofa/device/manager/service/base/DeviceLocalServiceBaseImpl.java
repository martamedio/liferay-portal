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

package com.bemis.portal.twofa.device.manager.service.base;

import aQute.bnd.annotation.ProviderType;

import com.bemis.portal.twofa.device.manager.model.Device;
import com.bemis.portal.twofa.device.manager.service.DeviceLocalService;
import com.bemis.portal.twofa.device.manager.service.persistence.DeviceCodePersistence;
import com.bemis.portal.twofa.device.manager.service.persistence.DevicePersistence;

import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdate;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdateFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DefaultActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Projection;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.module.framework.service.IdentifiableOSGiService;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.service.BaseLocalServiceImpl;
import com.liferay.portal.kernel.service.PersistedModelLocalServiceRegistry;
import com.liferay.portal.kernel.service.persistence.ClassNamePersistence;
import com.liferay.portal.kernel.service.persistence.UserPersistence;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.spring.extender.service.ServiceReference;

import java.io.Serializable;

import java.util.List;

import javax.sql.DataSource;

/**
 * Provides the base implementation for the device local service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.bemis.portal.twofa.device.manager.service.impl.DeviceLocalServiceImpl}.
 * </p>
 *
 * @author Prathima Shreenath
 * @see com.bemis.portal.twofa.device.manager.service.impl.DeviceLocalServiceImpl
 * @see com.bemis.portal.twofa.device.manager.service.DeviceLocalServiceUtil
 * @generated
 */
@ProviderType
public abstract class DeviceLocalServiceBaseImpl extends BaseLocalServiceImpl
	implements DeviceLocalService, IdentifiableOSGiService {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link com.bemis.portal.twofa.device.manager.service.DeviceLocalServiceUtil} to access the device local service.
	 */

	/**
	 * Adds the device to the database. Also notifies the appropriate model listeners.
	 *
	 * @param device the device
	 * @return the device that was added
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public Device addDevice(Device device) {
		device.setNew(true);

		return devicePersistence.update(device);
	}

	/**
	 * Creates a new device with the primary key. Does not add the device to the database.
	 *
	 * @param deviceId the primary key for the new device
	 * @return the new device
	 */
	@Override
	@Transactional(enabled = false)
	public Device createDevice(long deviceId) {
		return devicePersistence.create(deviceId);
	}

	/**
	 * Deletes the device with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param deviceId the primary key of the device
	 * @return the device that was removed
	 * @throws PortalException if a device with the primary key could not be found
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public Device deleteDevice(long deviceId) throws PortalException {
		return devicePersistence.remove(deviceId);
	}

	/**
	 * Deletes the device from the database. Also notifies the appropriate model listeners.
	 *
	 * @param device the device
	 * @return the device that was removed
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public Device deleteDevice(Device device) {
		return devicePersistence.remove(device);
	}

	@Override
	public DynamicQuery dynamicQuery() {
		Class<?> clazz = getClass();

		return DynamicQueryFactoryUtil.forClass(Device.class,
			clazz.getClassLoader());
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery) {
		return devicePersistence.findWithDynamicQuery(dynamicQuery);
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
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery, int start,
		int end) {
		return devicePersistence.findWithDynamicQuery(dynamicQuery, start, end);
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
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery, int start,
		int end, OrderByComparator<T> orderByComparator) {
		return devicePersistence.findWithDynamicQuery(dynamicQuery, start, end,
			orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(DynamicQuery dynamicQuery) {
		return devicePersistence.countWithDynamicQuery(dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(DynamicQuery dynamicQuery,
		Projection projection) {
		return devicePersistence.countWithDynamicQuery(dynamicQuery, projection);
	}

	@Override
	public Device fetchDevice(long deviceId) {
		return devicePersistence.fetchByPrimaryKey(deviceId);
	}

	/**
	 * Returns the device with the primary key.
	 *
	 * @param deviceId the primary key of the device
	 * @return the device
	 * @throws PortalException if a device with the primary key could not be found
	 */
	@Override
	public Device getDevice(long deviceId) throws PortalException {
		return devicePersistence.findByPrimaryKey(deviceId);
	}

	@Override
	public ActionableDynamicQuery getActionableDynamicQuery() {
		ActionableDynamicQuery actionableDynamicQuery = new DefaultActionableDynamicQuery();

		actionableDynamicQuery.setBaseLocalService(deviceLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(Device.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName("deviceId");

		return actionableDynamicQuery;
	}

	@Override
	public IndexableActionableDynamicQuery getIndexableActionableDynamicQuery() {
		IndexableActionableDynamicQuery indexableActionableDynamicQuery = new IndexableActionableDynamicQuery();

		indexableActionableDynamicQuery.setBaseLocalService(deviceLocalService);
		indexableActionableDynamicQuery.setClassLoader(getClassLoader());
		indexableActionableDynamicQuery.setModelClass(Device.class);

		indexableActionableDynamicQuery.setPrimaryKeyPropertyName("deviceId");

		return indexableActionableDynamicQuery;
	}

	protected void initActionableDynamicQuery(
		ActionableDynamicQuery actionableDynamicQuery) {
		actionableDynamicQuery.setBaseLocalService(deviceLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(Device.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName("deviceId");
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel deletePersistedModel(PersistedModel persistedModel)
		throws PortalException {
		return deviceLocalService.deleteDevice((Device)persistedModel);
	}

	@Override
	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException {
		return devicePersistence.findByPrimaryKey(primaryKeyObj);
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
	public List<Device> getDevices(int start, int end) {
		return devicePersistence.findAll(start, end);
	}

	/**
	 * Returns the number of devices.
	 *
	 * @return the number of devices
	 */
	@Override
	public int getDevicesCount() {
		return devicePersistence.countAll();
	}

	/**
	 * Updates the device in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param device the device
	 * @return the device that was updated
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public Device updateDevice(Device device) {
		return devicePersistence.update(device);
	}

	/**
	 * Returns the device local service.
	 *
	 * @return the device local service
	 */
	public DeviceLocalService getDeviceLocalService() {
		return deviceLocalService;
	}

	/**
	 * Sets the device local service.
	 *
	 * @param deviceLocalService the device local service
	 */
	public void setDeviceLocalService(DeviceLocalService deviceLocalService) {
		this.deviceLocalService = deviceLocalService;
	}

	/**
	 * Returns the device persistence.
	 *
	 * @return the device persistence
	 */
	public DevicePersistence getDevicePersistence() {
		return devicePersistence;
	}

	/**
	 * Sets the device persistence.
	 *
	 * @param devicePersistence the device persistence
	 */
	public void setDevicePersistence(DevicePersistence devicePersistence) {
		this.devicePersistence = devicePersistence;
	}

	/**
	 * Returns the device code local service.
	 *
	 * @return the device code local service
	 */
	public com.bemis.portal.twofa.device.manager.service.DeviceCodeLocalService getDeviceCodeLocalService() {
		return deviceCodeLocalService;
	}

	/**
	 * Sets the device code local service.
	 *
	 * @param deviceCodeLocalService the device code local service
	 */
	public void setDeviceCodeLocalService(
		com.bemis.portal.twofa.device.manager.service.DeviceCodeLocalService deviceCodeLocalService) {
		this.deviceCodeLocalService = deviceCodeLocalService;
	}

	/**
	 * Returns the device code persistence.
	 *
	 * @return the device code persistence
	 */
	public DeviceCodePersistence getDeviceCodePersistence() {
		return deviceCodePersistence;
	}

	/**
	 * Sets the device code persistence.
	 *
	 * @param deviceCodePersistence the device code persistence
	 */
	public void setDeviceCodePersistence(
		DeviceCodePersistence deviceCodePersistence) {
		this.deviceCodePersistence = deviceCodePersistence;
	}

	/**
	 * Returns the device manager local service.
	 *
	 * @return the device manager local service
	 */
	public com.bemis.portal.twofa.device.manager.service.DeviceManagerLocalService getDeviceManagerLocalService() {
		return deviceManagerLocalService;
	}

	/**
	 * Sets the device manager local service.
	 *
	 * @param deviceManagerLocalService the device manager local service
	 */
	public void setDeviceManagerLocalService(
		com.bemis.portal.twofa.device.manager.service.DeviceManagerLocalService deviceManagerLocalService) {
		this.deviceManagerLocalService = deviceManagerLocalService;
	}

	/**
	 * Returns the counter local service.
	 *
	 * @return the counter local service
	 */
	public com.liferay.counter.kernel.service.CounterLocalService getCounterLocalService() {
		return counterLocalService;
	}

	/**
	 * Sets the counter local service.
	 *
	 * @param counterLocalService the counter local service
	 */
	public void setCounterLocalService(
		com.liferay.counter.kernel.service.CounterLocalService counterLocalService) {
		this.counterLocalService = counterLocalService;
	}

	/**
	 * Returns the class name local service.
	 *
	 * @return the class name local service
	 */
	public com.liferay.portal.kernel.service.ClassNameLocalService getClassNameLocalService() {
		return classNameLocalService;
	}

	/**
	 * Sets the class name local service.
	 *
	 * @param classNameLocalService the class name local service
	 */
	public void setClassNameLocalService(
		com.liferay.portal.kernel.service.ClassNameLocalService classNameLocalService) {
		this.classNameLocalService = classNameLocalService;
	}

	/**
	 * Returns the class name persistence.
	 *
	 * @return the class name persistence
	 */
	public ClassNamePersistence getClassNamePersistence() {
		return classNamePersistence;
	}

	/**
	 * Sets the class name persistence.
	 *
	 * @param classNamePersistence the class name persistence
	 */
	public void setClassNamePersistence(
		ClassNamePersistence classNamePersistence) {
		this.classNamePersistence = classNamePersistence;
	}

	/**
	 * Returns the resource local service.
	 *
	 * @return the resource local service
	 */
	public com.liferay.portal.kernel.service.ResourceLocalService getResourceLocalService() {
		return resourceLocalService;
	}

	/**
	 * Sets the resource local service.
	 *
	 * @param resourceLocalService the resource local service
	 */
	public void setResourceLocalService(
		com.liferay.portal.kernel.service.ResourceLocalService resourceLocalService) {
		this.resourceLocalService = resourceLocalService;
	}

	/**
	 * Returns the user local service.
	 *
	 * @return the user local service
	 */
	public com.liferay.portal.kernel.service.UserLocalService getUserLocalService() {
		return userLocalService;
	}

	/**
	 * Sets the user local service.
	 *
	 * @param userLocalService the user local service
	 */
	public void setUserLocalService(
		com.liferay.portal.kernel.service.UserLocalService userLocalService) {
		this.userLocalService = userLocalService;
	}

	/**
	 * Returns the user persistence.
	 *
	 * @return the user persistence
	 */
	public UserPersistence getUserPersistence() {
		return userPersistence;
	}

	/**
	 * Sets the user persistence.
	 *
	 * @param userPersistence the user persistence
	 */
	public void setUserPersistence(UserPersistence userPersistence) {
		this.userPersistence = userPersistence;
	}

	public void afterPropertiesSet() {
		persistedModelLocalServiceRegistry.register("com.bemis.portal.twofa.device.manager.model.Device",
			deviceLocalService);
	}

	public void destroy() {
		persistedModelLocalServiceRegistry.unregister(
			"com.bemis.portal.twofa.device.manager.model.Device");
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return DeviceLocalService.class.getName();
	}

	protected Class<?> getModelClass() {
		return Device.class;
	}

	protected String getModelClassName() {
		return Device.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) {
		try {
			DataSource dataSource = devicePersistence.getDataSource();

			DB db = DBManagerUtil.getDB();

			sql = db.buildSQL(sql);
			sql = PortalUtil.transformSQL(sql);

			SqlUpdate sqlUpdate = SqlUpdateFactoryUtil.getSqlUpdate(dataSource,
					sql);

			sqlUpdate.update();
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
	}

	@BeanReference(type = DeviceLocalService.class)
	protected DeviceLocalService deviceLocalService;
	@BeanReference(type = DevicePersistence.class)
	protected DevicePersistence devicePersistence;
	@BeanReference(type = com.bemis.portal.twofa.device.manager.service.DeviceCodeLocalService.class)
	protected com.bemis.portal.twofa.device.manager.service.DeviceCodeLocalService deviceCodeLocalService;
	@BeanReference(type = DeviceCodePersistence.class)
	protected DeviceCodePersistence deviceCodePersistence;
	@BeanReference(type = com.bemis.portal.twofa.device.manager.service.DeviceManagerLocalService.class)
	protected com.bemis.portal.twofa.device.manager.service.DeviceManagerLocalService deviceManagerLocalService;
	@ServiceReference(type = com.liferay.counter.kernel.service.CounterLocalService.class)
	protected com.liferay.counter.kernel.service.CounterLocalService counterLocalService;
	@ServiceReference(type = com.liferay.portal.kernel.service.ClassNameLocalService.class)
	protected com.liferay.portal.kernel.service.ClassNameLocalService classNameLocalService;
	@ServiceReference(type = ClassNamePersistence.class)
	protected ClassNamePersistence classNamePersistence;
	@ServiceReference(type = com.liferay.portal.kernel.service.ResourceLocalService.class)
	protected com.liferay.portal.kernel.service.ResourceLocalService resourceLocalService;
	@ServiceReference(type = com.liferay.portal.kernel.service.UserLocalService.class)
	protected com.liferay.portal.kernel.service.UserLocalService userLocalService;
	@ServiceReference(type = UserPersistence.class)
	protected UserPersistence userPersistence;
	@ServiceReference(type = PersistedModelLocalServiceRegistry.class)
	protected PersistedModelLocalServiceRegistry persistedModelLocalServiceRegistry;
}
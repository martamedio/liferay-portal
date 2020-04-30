/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.multi.factor.authentication.timebased.otp.service.persistence;

import com.liferay.multi.factor.authentication.timebased.otp.model.MFATimebasedOTPEntry;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.io.Serializable;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * The persistence utility for the mfa timebased otp entry service. This utility wraps <code>com.liferay.multi.factor.authentication.timebased.otp.service.persistence.impl.MFATimebasedOTPEntryPersistenceImpl</code> and provides direct access to the database for CRUD operations. This utility should only be used by the service layer, as it must operate within a transaction. Never access this utility in a JSP, controller, model, or other front-end class.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Arthur Chan
 * @see MFATimebasedOTPEntryPersistence
 * @generated
 */
public class MFATimebasedOTPEntryUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#clearCache()
	 */
	public static void clearCache() {
		getPersistence().clearCache();
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#clearCache(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static void clearCache(MFATimebasedOTPEntry mfaTimebasedOTPEntry) {
		getPersistence().clearCache(mfaTimebasedOTPEntry);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#countWithDynamicQuery(DynamicQuery)
	 */
	public static long countWithDynamicQuery(DynamicQuery dynamicQuery) {
		return getPersistence().countWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#fetchByPrimaryKeys(Set)
	 */
	public static Map<Serializable, MFATimebasedOTPEntry> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {

		return getPersistence().fetchByPrimaryKeys(primaryKeys);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery)
	 */
	public static List<MFATimebasedOTPEntry> findWithDynamicQuery(
		DynamicQuery dynamicQuery) {

		return getPersistence().findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int)
	 */
	public static List<MFATimebasedOTPEntry> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return getPersistence().findWithDynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int, OrderByComparator)
	 */
	public static List<MFATimebasedOTPEntry> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<MFATimebasedOTPEntry> orderByComparator) {

		return getPersistence().findWithDynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static MFATimebasedOTPEntry update(
		MFATimebasedOTPEntry mfaTimebasedOTPEntry) {

		return getPersistence().update(mfaTimebasedOTPEntry);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel, ServiceContext)
	 */
	public static MFATimebasedOTPEntry update(
		MFATimebasedOTPEntry mfaTimebasedOTPEntry,
		ServiceContext serviceContext) {

		return getPersistence().update(mfaTimebasedOTPEntry, serviceContext);
	}

	/**
	 * Returns the mfa timebased otp entry where userId = &#63; or throws a <code>NoSuchEntryException</code> if it could not be found.
	 *
	 * @param userId the user ID
	 * @return the matching mfa timebased otp entry
	 * @throws NoSuchEntryException if a matching mfa timebased otp entry could not be found
	 */
	public static MFATimebasedOTPEntry findByUserId(long userId)
		throws com.liferay.multi.factor.authentication.timebased.otp.exception.
			NoSuchEntryException {

		return getPersistence().findByUserId(userId);
	}

	/**
	 * Returns the mfa timebased otp entry where userId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param userId the user ID
	 * @return the matching mfa timebased otp entry, or <code>null</code> if a matching mfa timebased otp entry could not be found
	 */
	public static MFATimebasedOTPEntry fetchByUserId(long userId) {
		return getPersistence().fetchByUserId(userId);
	}

	/**
	 * Returns the mfa timebased otp entry where userId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param userId the user ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching mfa timebased otp entry, or <code>null</code> if a matching mfa timebased otp entry could not be found
	 */
	public static MFATimebasedOTPEntry fetchByUserId(
		long userId, boolean useFinderCache) {

		return getPersistence().fetchByUserId(userId, useFinderCache);
	}

	/**
	 * Removes the mfa timebased otp entry where userId = &#63; from the database.
	 *
	 * @param userId the user ID
	 * @return the mfa timebased otp entry that was removed
	 */
	public static MFATimebasedOTPEntry removeByUserId(long userId)
		throws com.liferay.multi.factor.authentication.timebased.otp.exception.
			NoSuchEntryException {

		return getPersistence().removeByUserId(userId);
	}

	/**
	 * Returns the number of mfa timebased otp entries where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @return the number of matching mfa timebased otp entries
	 */
	public static int countByUserId(long userId) {
		return getPersistence().countByUserId(userId);
	}

	/**
	 * Caches the mfa timebased otp entry in the entity cache if it is enabled.
	 *
	 * @param mfaTimebasedOTPEntry the mfa timebased otp entry
	 */
	public static void cacheResult(MFATimebasedOTPEntry mfaTimebasedOTPEntry) {
		getPersistence().cacheResult(mfaTimebasedOTPEntry);
	}

	/**
	 * Caches the mfa timebased otp entries in the entity cache if it is enabled.
	 *
	 * @param mfaTimebasedOTPEntries the mfa timebased otp entries
	 */
	public static void cacheResult(
		List<MFATimebasedOTPEntry> mfaTimebasedOTPEntries) {

		getPersistence().cacheResult(mfaTimebasedOTPEntries);
	}

	/**
	 * Creates a new mfa timebased otp entry with the primary key. Does not add the mfa timebased otp entry to the database.
	 *
	 * @param mfaTimebasedOTPEntryId the primary key for the new mfa timebased otp entry
	 * @return the new mfa timebased otp entry
	 */
	public static MFATimebasedOTPEntry create(long mfaTimebasedOTPEntryId) {
		return getPersistence().create(mfaTimebasedOTPEntryId);
	}

	/**
	 * Removes the mfa timebased otp entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param mfaTimebasedOTPEntryId the primary key of the mfa timebased otp entry
	 * @return the mfa timebased otp entry that was removed
	 * @throws NoSuchEntryException if a mfa timebased otp entry with the primary key could not be found
	 */
	public static MFATimebasedOTPEntry remove(long mfaTimebasedOTPEntryId)
		throws com.liferay.multi.factor.authentication.timebased.otp.exception.
			NoSuchEntryException {

		return getPersistence().remove(mfaTimebasedOTPEntryId);
	}

	public static MFATimebasedOTPEntry updateImpl(
		MFATimebasedOTPEntry mfaTimebasedOTPEntry) {

		return getPersistence().updateImpl(mfaTimebasedOTPEntry);
	}

	/**
	 * Returns the mfa timebased otp entry with the primary key or throws a <code>NoSuchEntryException</code> if it could not be found.
	 *
	 * @param mfaTimebasedOTPEntryId the primary key of the mfa timebased otp entry
	 * @return the mfa timebased otp entry
	 * @throws NoSuchEntryException if a mfa timebased otp entry with the primary key could not be found
	 */
	public static MFATimebasedOTPEntry findByPrimaryKey(
			long mfaTimebasedOTPEntryId)
		throws com.liferay.multi.factor.authentication.timebased.otp.exception.
			NoSuchEntryException {

		return getPersistence().findByPrimaryKey(mfaTimebasedOTPEntryId);
	}

	/**
	 * Returns the mfa timebased otp entry with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param mfaTimebasedOTPEntryId the primary key of the mfa timebased otp entry
	 * @return the mfa timebased otp entry, or <code>null</code> if a mfa timebased otp entry with the primary key could not be found
	 */
	public static MFATimebasedOTPEntry fetchByPrimaryKey(
		long mfaTimebasedOTPEntryId) {

		return getPersistence().fetchByPrimaryKey(mfaTimebasedOTPEntryId);
	}

	/**
	 * Returns all the mfa timebased otp entries.
	 *
	 * @return the mfa timebased otp entries
	 */
	public static List<MFATimebasedOTPEntry> findAll() {
		return getPersistence().findAll();
	}

	/**
	 * Returns a range of all the mfa timebased otp entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>MFATimebasedOTPEntryModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of mfa timebased otp entries
	 * @param end the upper bound of the range of mfa timebased otp entries (not inclusive)
	 * @return the range of mfa timebased otp entries
	 */
	public static List<MFATimebasedOTPEntry> findAll(int start, int end) {
		return getPersistence().findAll(start, end);
	}

	/**
	 * Returns an ordered range of all the mfa timebased otp entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>MFATimebasedOTPEntryModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of mfa timebased otp entries
	 * @param end the upper bound of the range of mfa timebased otp entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of mfa timebased otp entries
	 */
	public static List<MFATimebasedOTPEntry> findAll(
		int start, int end,
		OrderByComparator<MFATimebasedOTPEntry> orderByComparator) {

		return getPersistence().findAll(start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the mfa timebased otp entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>MFATimebasedOTPEntryModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of mfa timebased otp entries
	 * @param end the upper bound of the range of mfa timebased otp entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of mfa timebased otp entries
	 */
	public static List<MFATimebasedOTPEntry> findAll(
		int start, int end,
		OrderByComparator<MFATimebasedOTPEntry> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findAll(
			start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Removes all the mfa timebased otp entries from the database.
	 */
	public static void removeAll() {
		getPersistence().removeAll();
	}

	/**
	 * Returns the number of mfa timebased otp entries.
	 *
	 * @return the number of mfa timebased otp entries
	 */
	public static int countAll() {
		return getPersistence().countAll();
	}

	public static MFATimebasedOTPEntryPersistence getPersistence() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker
		<MFATimebasedOTPEntryPersistence, MFATimebasedOTPEntryPersistence>
			_serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(
			MFATimebasedOTPEntryPersistence.class);

		ServiceTracker
			<MFATimebasedOTPEntryPersistence, MFATimebasedOTPEntryPersistence>
				serviceTracker =
					new ServiceTracker
						<MFATimebasedOTPEntryPersistence,
						 MFATimebasedOTPEntryPersistence>(
							 bundle.getBundleContext(),
							 MFATimebasedOTPEntryPersistence.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}
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

package com.liferay.multi.factor.authentication.timebased.otp.service;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Provides the local service utility for MFATimebasedOTPEntry. This utility wraps
 * <code>com.liferay.multi.factor.authentication.timebased.otp.service.impl.MFATimebasedOTPEntryLocalServiceImpl</code> and
 * is an access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Arthur Chan
 * @see MFATimebasedOTPEntryLocalService
 * @generated
 */
public class MFATimebasedOTPEntryLocalServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.multi.factor.authentication.timebased.otp.service.impl.MFATimebasedOTPEntryLocalServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * Adds the mfa timebased otp entry to the database. Also notifies the appropriate model listeners.
	 *
	 * @param mfaTimebasedOTPEntry the mfa timebased otp entry
	 * @return the mfa timebased otp entry that was added
	 */
	public static com.liferay.multi.factor.authentication.timebased.otp.model.
		MFATimebasedOTPEntry addMFATimebasedOTPEntry(
			com.liferay.multi.factor.authentication.timebased.otp.model.
				MFATimebasedOTPEntry mfaTimebasedOTPEntry) {

		return getService().addMFATimebasedOTPEntry(mfaTimebasedOTPEntry);
	}

	public static com.liferay.multi.factor.authentication.timebased.otp.model.
		MFATimebasedOTPEntry addTimebasedOTPEntry(
				String sharedSecret, long userId)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addTimebasedOTPEntry(sharedSecret, userId);
	}

	/**
	 * Creates a new mfa timebased otp entry with the primary key. Does not add the mfa timebased otp entry to the database.
	 *
	 * @param mfaTimebasedOTPEntryId the primary key for the new mfa timebased otp entry
	 * @return the new mfa timebased otp entry
	 */
	public static com.liferay.multi.factor.authentication.timebased.otp.model.
		MFATimebasedOTPEntry createMFATimebasedOTPEntry(
			long mfaTimebasedOTPEntryId) {

		return getService().createMFATimebasedOTPEntry(mfaTimebasedOTPEntryId);
	}

	/**
	 * Deletes the mfa timebased otp entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param mfaTimebasedOTPEntryId the primary key of the mfa timebased otp entry
	 * @return the mfa timebased otp entry that was removed
	 * @throws PortalException if a mfa timebased otp entry with the primary key could not be found
	 */
	public static com.liferay.multi.factor.authentication.timebased.otp.model.
		MFATimebasedOTPEntry deleteMFATimebasedOTPEntry(
				long mfaTimebasedOTPEntryId)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deleteMFATimebasedOTPEntry(mfaTimebasedOTPEntryId);
	}

	/**
	 * Deletes the mfa timebased otp entry from the database. Also notifies the appropriate model listeners.
	 *
	 * @param mfaTimebasedOTPEntry the mfa timebased otp entry
	 * @return the mfa timebased otp entry that was removed
	 */
	public static com.liferay.multi.factor.authentication.timebased.otp.model.
		MFATimebasedOTPEntry deleteMFATimebasedOTPEntry(
			com.liferay.multi.factor.authentication.timebased.otp.model.
				MFATimebasedOTPEntry mfaTimebasedOTPEntry) {

		return getService().deleteMFATimebasedOTPEntry(mfaTimebasedOTPEntry);
	}

	/**
	 * @throws PortalException
	 */
	public static com.liferay.portal.kernel.model.PersistedModel
			deletePersistedModel(
				com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deletePersistedModel(persistedModel);
	}

	public static com.liferay.portal.kernel.dao.orm.DynamicQuery
		dynamicQuery() {

		return getService().dynamicQuery();
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	public static <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return getService().dynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.multi.factor.authentication.timebased.otp.model.impl.MFATimebasedOTPEntryModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	public static <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) {

		return getService().dynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.multi.factor.authentication.timebased.otp.model.impl.MFATimebasedOTPEntryModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	public static <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<T> orderByComparator) {

		return getService().dynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	public static long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return getService().dynamicQueryCount(dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	public static long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {

		return getService().dynamicQueryCount(dynamicQuery, projection);
	}

	public static com.liferay.multi.factor.authentication.timebased.otp.model.
		MFATimebasedOTPEntry fetchMFATimebasedOTPEntry(
			long mfaTimebasedOTPEntryId) {

		return getService().fetchMFATimebasedOTPEntry(mfaTimebasedOTPEntryId);
	}

	public static com.liferay.multi.factor.authentication.timebased.otp.model.
		MFATimebasedOTPEntry fetchMFATimebasedOTPEntryByUserId(long userId) {

		return getService().fetchMFATimebasedOTPEntryByUserId(userId);
	}

	public static com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return getService().getActionableDynamicQuery();
	}

	public static
		com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
			getIndexableActionableDynamicQuery() {

		return getService().getIndexableActionableDynamicQuery();
	}

	/**
	 * Returns a range of all the mfa timebased otp entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.multi.factor.authentication.timebased.otp.model.impl.MFATimebasedOTPEntryModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of mfa timebased otp entries
	 * @param end the upper bound of the range of mfa timebased otp entries (not inclusive)
	 * @return the range of mfa timebased otp entries
	 */
	public static java.util.List
		<com.liferay.multi.factor.authentication.timebased.otp.model.
			MFATimebasedOTPEntry> getMFATimebasedOTPEntries(
				int start, int end) {

		return getService().getMFATimebasedOTPEntries(start, end);
	}

	/**
	 * Returns the number of mfa timebased otp entries.
	 *
	 * @return the number of mfa timebased otp entries
	 */
	public static int getMFATimebasedOTPEntriesCount() {
		return getService().getMFATimebasedOTPEntriesCount();
	}

	/**
	 * Returns the mfa timebased otp entry with the primary key.
	 *
	 * @param mfaTimebasedOTPEntryId the primary key of the mfa timebased otp entry
	 * @return the mfa timebased otp entry
	 * @throws PortalException if a mfa timebased otp entry with the primary key could not be found
	 */
	public static com.liferay.multi.factor.authentication.timebased.otp.model.
		MFATimebasedOTPEntry getMFATimebasedOTPEntry(
				long mfaTimebasedOTPEntryId)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getMFATimebasedOTPEntry(mfaTimebasedOTPEntryId);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	/**
	 * @throws PortalException
	 */
	public static com.liferay.portal.kernel.model.PersistedModel
			getPersistedModel(java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getPersistedModel(primaryKeyObj);
	}

	public static com.liferay.multi.factor.authentication.timebased.otp.model.
		MFATimebasedOTPEntry resetFailedAttempts(long userId)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().resetFailedAttempts(userId);
	}

	public static com.liferay.multi.factor.authentication.timebased.otp.model.
		MFATimebasedOTPEntry updateAttempts(
				long userId, String ip, boolean success)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateAttempts(userId, ip, success);
	}

	/**
	 * Updates the mfa timebased otp entry in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param mfaTimebasedOTPEntry the mfa timebased otp entry
	 * @return the mfa timebased otp entry that was updated
	 */
	public static com.liferay.multi.factor.authentication.timebased.otp.model.
		MFATimebasedOTPEntry updateMFATimebasedOTPEntry(
			com.liferay.multi.factor.authentication.timebased.otp.model.
				MFATimebasedOTPEntry mfaTimebasedOTPEntry) {

		return getService().updateMFATimebasedOTPEntry(mfaTimebasedOTPEntry);
	}

	public static MFATimebasedOTPEntryLocalService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker
		<MFATimebasedOTPEntryLocalService, MFATimebasedOTPEntryLocalService>
			_serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(
			MFATimebasedOTPEntryLocalService.class);

		ServiceTracker
			<MFATimebasedOTPEntryLocalService, MFATimebasedOTPEntryLocalService>
				serviceTracker =
					new ServiceTracker
						<MFATimebasedOTPEntryLocalService,
						 MFATimebasedOTPEntryLocalService>(
							 bundle.getBundleContext(),
							 MFATimebasedOTPEntryLocalService.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}
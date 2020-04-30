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

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link MFATimebasedOTPEntryLocalService}.
 *
 * @author Arthur Chan
 * @see MFATimebasedOTPEntryLocalService
 * @generated
 */
public class MFATimebasedOTPEntryLocalServiceWrapper
	implements MFATimebasedOTPEntryLocalService,
			   ServiceWrapper<MFATimebasedOTPEntryLocalService> {

	public MFATimebasedOTPEntryLocalServiceWrapper(
		MFATimebasedOTPEntryLocalService mfaTimebasedOTPEntryLocalService) {

		_mfaTimebasedOTPEntryLocalService = mfaTimebasedOTPEntryLocalService;
	}

	/**
	 * Adds the mfa timebased otp entry to the database. Also notifies the appropriate model listeners.
	 *
	 * @param mfaTimebasedOTPEntry the mfa timebased otp entry
	 * @return the mfa timebased otp entry that was added
	 */
	@Override
	public com.liferay.multi.factor.authentication.timebased.otp.model.
		MFATimebasedOTPEntry addMFATimebasedOTPEntry(
			com.liferay.multi.factor.authentication.timebased.otp.model.
				MFATimebasedOTPEntry mfaTimebasedOTPEntry) {

		return _mfaTimebasedOTPEntryLocalService.addMFATimebasedOTPEntry(
			mfaTimebasedOTPEntry);
	}

	@Override
	public com.liferay.multi.factor.authentication.timebased.otp.model.
		MFATimebasedOTPEntry addTimebasedOTPEntry(
				String sharedSecret, long userId)
			throws com.liferay.portal.kernel.exception.PortalException {

		return _mfaTimebasedOTPEntryLocalService.addTimebasedOTPEntry(
			sharedSecret, userId);
	}

	/**
	 * Creates a new mfa timebased otp entry with the primary key. Does not add the mfa timebased otp entry to the database.
	 *
	 * @param mfaTimebasedOTPEntryId the primary key for the new mfa timebased otp entry
	 * @return the new mfa timebased otp entry
	 */
	@Override
	public com.liferay.multi.factor.authentication.timebased.otp.model.
		MFATimebasedOTPEntry createMFATimebasedOTPEntry(
			long mfaTimebasedOTPEntryId) {

		return _mfaTimebasedOTPEntryLocalService.createMFATimebasedOTPEntry(
			mfaTimebasedOTPEntryId);
	}

	/**
	 * Deletes the mfa timebased otp entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param mfaTimebasedOTPEntryId the primary key of the mfa timebased otp entry
	 * @return the mfa timebased otp entry that was removed
	 * @throws PortalException if a mfa timebased otp entry with the primary key could not be found
	 */
	@Override
	public com.liferay.multi.factor.authentication.timebased.otp.model.
		MFATimebasedOTPEntry deleteMFATimebasedOTPEntry(
				long mfaTimebasedOTPEntryId)
			throws com.liferay.portal.kernel.exception.PortalException {

		return _mfaTimebasedOTPEntryLocalService.deleteMFATimebasedOTPEntry(
			mfaTimebasedOTPEntryId);
	}

	/**
	 * Deletes the mfa timebased otp entry from the database. Also notifies the appropriate model listeners.
	 *
	 * @param mfaTimebasedOTPEntry the mfa timebased otp entry
	 * @return the mfa timebased otp entry that was removed
	 */
	@Override
	public com.liferay.multi.factor.authentication.timebased.otp.model.
		MFATimebasedOTPEntry deleteMFATimebasedOTPEntry(
			com.liferay.multi.factor.authentication.timebased.otp.model.
				MFATimebasedOTPEntry mfaTimebasedOTPEntry) {

		return _mfaTimebasedOTPEntryLocalService.deleteMFATimebasedOTPEntry(
			mfaTimebasedOTPEntry);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel deletePersistedModel(
			com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _mfaTimebasedOTPEntryLocalService.deletePersistedModel(
			persistedModel);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return _mfaTimebasedOTPEntryLocalService.dynamicQuery();
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

		return _mfaTimebasedOTPEntryLocalService.dynamicQuery(dynamicQuery);
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
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) {

		return _mfaTimebasedOTPEntryLocalService.dynamicQuery(
			dynamicQuery, start, end);
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
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<T> orderByComparator) {

		return _mfaTimebasedOTPEntryLocalService.dynamicQuery(
			dynamicQuery, start, end, orderByComparator);
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

		return _mfaTimebasedOTPEntryLocalService.dynamicQueryCount(
			dynamicQuery);
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

		return _mfaTimebasedOTPEntryLocalService.dynamicQueryCount(
			dynamicQuery, projection);
	}

	@Override
	public com.liferay.multi.factor.authentication.timebased.otp.model.
		MFATimebasedOTPEntry fetchMFATimebasedOTPEntry(
			long mfaTimebasedOTPEntryId) {

		return _mfaTimebasedOTPEntryLocalService.fetchMFATimebasedOTPEntry(
			mfaTimebasedOTPEntryId);
	}

	@Override
	public com.liferay.multi.factor.authentication.timebased.otp.model.
		MFATimebasedOTPEntry fetchMFATimebasedOTPEntryByUserId(long userId) {

		return _mfaTimebasedOTPEntryLocalService.
			fetchMFATimebasedOTPEntryByUserId(userId);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return _mfaTimebasedOTPEntryLocalService.getActionableDynamicQuery();
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		return _mfaTimebasedOTPEntryLocalService.
			getIndexableActionableDynamicQuery();
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
	@Override
	public java.util.List
		<com.liferay.multi.factor.authentication.timebased.otp.model.
			MFATimebasedOTPEntry> getMFATimebasedOTPEntries(
				int start, int end) {

		return _mfaTimebasedOTPEntryLocalService.getMFATimebasedOTPEntries(
			start, end);
	}

	/**
	 * Returns the number of mfa timebased otp entries.
	 *
	 * @return the number of mfa timebased otp entries
	 */
	@Override
	public int getMFATimebasedOTPEntriesCount() {
		return _mfaTimebasedOTPEntryLocalService.
			getMFATimebasedOTPEntriesCount();
	}

	/**
	 * Returns the mfa timebased otp entry with the primary key.
	 *
	 * @param mfaTimebasedOTPEntryId the primary key of the mfa timebased otp entry
	 * @return the mfa timebased otp entry
	 * @throws PortalException if a mfa timebased otp entry with the primary key could not be found
	 */
	@Override
	public com.liferay.multi.factor.authentication.timebased.otp.model.
		MFATimebasedOTPEntry getMFATimebasedOTPEntry(
				long mfaTimebasedOTPEntryId)
			throws com.liferay.portal.kernel.exception.PortalException {

		return _mfaTimebasedOTPEntryLocalService.getMFATimebasedOTPEntry(
			mfaTimebasedOTPEntryId);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _mfaTimebasedOTPEntryLocalService.getOSGiServiceIdentifier();
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel getPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _mfaTimebasedOTPEntryLocalService.getPersistedModel(
			primaryKeyObj);
	}

	@Override
	public com.liferay.multi.factor.authentication.timebased.otp.model.
		MFATimebasedOTPEntry resetFailedAttempts(long userId)
			throws com.liferay.portal.kernel.exception.PortalException {

		return _mfaTimebasedOTPEntryLocalService.resetFailedAttempts(userId);
	}

	@Override
	public com.liferay.multi.factor.authentication.timebased.otp.model.
		MFATimebasedOTPEntry updateAttempts(
				long userId, String ip, boolean success)
			throws com.liferay.portal.kernel.exception.PortalException {

		return _mfaTimebasedOTPEntryLocalService.updateAttempts(
			userId, ip, success);
	}

	/**
	 * Updates the mfa timebased otp entry in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param mfaTimebasedOTPEntry the mfa timebased otp entry
	 * @return the mfa timebased otp entry that was updated
	 */
	@Override
	public com.liferay.multi.factor.authentication.timebased.otp.model.
		MFATimebasedOTPEntry updateMFATimebasedOTPEntry(
			com.liferay.multi.factor.authentication.timebased.otp.model.
				MFATimebasedOTPEntry mfaTimebasedOTPEntry) {

		return _mfaTimebasedOTPEntryLocalService.updateMFATimebasedOTPEntry(
			mfaTimebasedOTPEntry);
	}

	@Override
	public MFATimebasedOTPEntryLocalService getWrappedService() {
		return _mfaTimebasedOTPEntryLocalService;
	}

	@Override
	public void setWrappedService(
		MFATimebasedOTPEntryLocalService mfaTimebasedOTPEntryLocalService) {

		_mfaTimebasedOTPEntryLocalService = mfaTimebasedOTPEntryLocalService;
	}

	private MFATimebasedOTPEntryLocalService _mfaTimebasedOTPEntryLocalService;

}
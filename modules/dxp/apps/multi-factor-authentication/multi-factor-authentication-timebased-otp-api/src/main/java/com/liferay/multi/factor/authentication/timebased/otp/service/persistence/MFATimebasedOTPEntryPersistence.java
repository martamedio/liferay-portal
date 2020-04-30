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

import com.liferay.multi.factor.authentication.timebased.otp.exception.NoSuchEntryException;
import com.liferay.multi.factor.authentication.timebased.otp.model.MFATimebasedOTPEntry;
import com.liferay.portal.kernel.service.persistence.BasePersistence;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The persistence interface for the mfa timebased otp entry service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Arthur Chan
 * @see MFATimebasedOTPEntryUtil
 * @generated
 */
@ProviderType
public interface MFATimebasedOTPEntryPersistence
	extends BasePersistence<MFATimebasedOTPEntry> {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link MFATimebasedOTPEntryUtil} to access the mfa timebased otp entry persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	 * Returns the mfa timebased otp entry where userId = &#63; or throws a <code>NoSuchEntryException</code> if it could not be found.
	 *
	 * @param userId the user ID
	 * @return the matching mfa timebased otp entry
	 * @throws NoSuchEntryException if a matching mfa timebased otp entry could not be found
	 */
	public MFATimebasedOTPEntry findByUserId(long userId)
		throws NoSuchEntryException;

	/**
	 * Returns the mfa timebased otp entry where userId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param userId the user ID
	 * @return the matching mfa timebased otp entry, or <code>null</code> if a matching mfa timebased otp entry could not be found
	 */
	public MFATimebasedOTPEntry fetchByUserId(long userId);

	/**
	 * Returns the mfa timebased otp entry where userId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param userId the user ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching mfa timebased otp entry, or <code>null</code> if a matching mfa timebased otp entry could not be found
	 */
	public MFATimebasedOTPEntry fetchByUserId(
		long userId, boolean useFinderCache);

	/**
	 * Removes the mfa timebased otp entry where userId = &#63; from the database.
	 *
	 * @param userId the user ID
	 * @return the mfa timebased otp entry that was removed
	 */
	public MFATimebasedOTPEntry removeByUserId(long userId)
		throws NoSuchEntryException;

	/**
	 * Returns the number of mfa timebased otp entries where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @return the number of matching mfa timebased otp entries
	 */
	public int countByUserId(long userId);

	/**
	 * Caches the mfa timebased otp entry in the entity cache if it is enabled.
	 *
	 * @param mfaTimebasedOTPEntry the mfa timebased otp entry
	 */
	public void cacheResult(MFATimebasedOTPEntry mfaTimebasedOTPEntry);

	/**
	 * Caches the mfa timebased otp entries in the entity cache if it is enabled.
	 *
	 * @param mfaTimebasedOTPEntries the mfa timebased otp entries
	 */
	public void cacheResult(
		java.util.List<MFATimebasedOTPEntry> mfaTimebasedOTPEntries);

	/**
	 * Creates a new mfa timebased otp entry with the primary key. Does not add the mfa timebased otp entry to the database.
	 *
	 * @param mfaTimebasedOTPEntryId the primary key for the new mfa timebased otp entry
	 * @return the new mfa timebased otp entry
	 */
	public MFATimebasedOTPEntry create(long mfaTimebasedOTPEntryId);

	/**
	 * Removes the mfa timebased otp entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param mfaTimebasedOTPEntryId the primary key of the mfa timebased otp entry
	 * @return the mfa timebased otp entry that was removed
	 * @throws NoSuchEntryException if a mfa timebased otp entry with the primary key could not be found
	 */
	public MFATimebasedOTPEntry remove(long mfaTimebasedOTPEntryId)
		throws NoSuchEntryException;

	public MFATimebasedOTPEntry updateImpl(
		MFATimebasedOTPEntry mfaTimebasedOTPEntry);

	/**
	 * Returns the mfa timebased otp entry with the primary key or throws a <code>NoSuchEntryException</code> if it could not be found.
	 *
	 * @param mfaTimebasedOTPEntryId the primary key of the mfa timebased otp entry
	 * @return the mfa timebased otp entry
	 * @throws NoSuchEntryException if a mfa timebased otp entry with the primary key could not be found
	 */
	public MFATimebasedOTPEntry findByPrimaryKey(long mfaTimebasedOTPEntryId)
		throws NoSuchEntryException;

	/**
	 * Returns the mfa timebased otp entry with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param mfaTimebasedOTPEntryId the primary key of the mfa timebased otp entry
	 * @return the mfa timebased otp entry, or <code>null</code> if a mfa timebased otp entry with the primary key could not be found
	 */
	public MFATimebasedOTPEntry fetchByPrimaryKey(long mfaTimebasedOTPEntryId);

	/**
	 * Returns all the mfa timebased otp entries.
	 *
	 * @return the mfa timebased otp entries
	 */
	public java.util.List<MFATimebasedOTPEntry> findAll();

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
	public java.util.List<MFATimebasedOTPEntry> findAll(int start, int end);

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
	public java.util.List<MFATimebasedOTPEntry> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<MFATimebasedOTPEntry>
			orderByComparator);

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
	public java.util.List<MFATimebasedOTPEntry> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<MFATimebasedOTPEntry>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Removes all the mfa timebased otp entries from the database.
	 */
	public void removeAll();

	/**
	 * Returns the number of mfa timebased otp entries.
	 *
	 * @return the number of mfa timebased otp entries
	 */
	public int countAll();

}
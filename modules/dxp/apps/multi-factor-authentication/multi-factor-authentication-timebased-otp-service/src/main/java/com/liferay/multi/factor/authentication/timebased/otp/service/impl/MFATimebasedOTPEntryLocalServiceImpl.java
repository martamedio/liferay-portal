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

package com.liferay.multi.factor.authentication.timebased.otp.service.impl;

import com.liferay.multi.factor.authentication.timebased.otp.exception.NoSuchEntryException;
import com.liferay.multi.factor.authentication.timebased.otp.model.MFATimebasedOTPEntry;
import com.liferay.multi.factor.authentication.timebased.otp.service.base.MFATimebasedOTPEntryLocalServiceBaseImpl;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;

import java.util.Date;

import org.osgi.service.component.annotations.Component;

/**
 * The implementation of the mfa timebased otp entry local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the <code>com.liferay.multi.factor.authentication.timebased.otp.service.MFATimebasedOTPEntryLocalService</code> interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Arthur Chan
 * @see MFATimebasedOTPEntryLocalServiceBaseImpl
 */
@Component(
	property = "model.class.name=com.liferay.multi.factor.authentication.timebased.otp.model.MFATimebasedOTPEntry",
	service = AopService.class
)
public class MFATimebasedOTPEntryLocalServiceImpl
	extends MFATimebasedOTPEntryLocalServiceBaseImpl {

	public MFATimebasedOTPEntry addTimebasedOTPEntry(
			String sharedSecret, long userId)
		throws PortalException {

		MFATimebasedOTPEntry mfaTimebasedOTPEntry =
			mfaTimebasedOTPEntryPersistence.fetchByUserId(userId);

		if (mfaTimebasedOTPEntry != null) {
			throw new IllegalArgumentException(
				"There is already one Timebased One-Time password for user " +
					userId);
		}

		User user = userLocalService.getUserById(userId);

		long entryId = counterLocalService.increment();

		mfaTimebasedOTPEntry = mfaTimebasedOTPEntryPersistence.create(entryId);

		mfaTimebasedOTPEntry.setCompanyId(user.getCompanyId());
		mfaTimebasedOTPEntry.setUserId(userId);
		mfaTimebasedOTPEntry.setUserName(user.getFullName());
		mfaTimebasedOTPEntry.setCreateDate(new Date());
		mfaTimebasedOTPEntry.setSharedSecret(sharedSecret);

		return mfaTimebasedOTPEntryPersistence.update(mfaTimebasedOTPEntry);
	}

	public MFATimebasedOTPEntry fetchMFATimebasedOTPEntryByUserId(long userId) {
		return mfaTimebasedOTPEntryPersistence.fetchByUserId(userId);
	}

	public MFATimebasedOTPEntry resetFailedAttempts(long userId)
		throws PortalException {

		MFATimebasedOTPEntry mfaTimebasedOTPEntry =
			mfaTimebasedOTPEntryPersistence.fetchByUserId(userId);

		if (mfaTimebasedOTPEntry == null) {
			throw new NoSuchEntryException("User ID " + userId);
		}

		mfaTimebasedOTPEntry.setFailedAttempts(0);
		mfaTimebasedOTPEntry.setLastFailDate(null);
		mfaTimebasedOTPEntry.setLastFailIP(null);

		return mfaTimebasedOTPEntryPersistence.update(mfaTimebasedOTPEntry);
	}

	public MFATimebasedOTPEntry updateAttempts(
			long userId, String ip, boolean success)
		throws PortalException {

		MFATimebasedOTPEntry mfaTimebasedOTPEntry =
			mfaTimebasedOTPEntryPersistence.fetchByUserId(userId);

		if (mfaTimebasedOTPEntry == null) {
			throw new NoSuchEntryException("User ID " + userId);
		}

		if (success) {
			mfaTimebasedOTPEntry.setFailedAttempts(0);
			mfaTimebasedOTPEntry.setLastFailDate(null);
			mfaTimebasedOTPEntry.setLastFailIP(null);
			mfaTimebasedOTPEntry.setLastSuccessDate(new Date());
			mfaTimebasedOTPEntry.setLastSuccessIP(ip);
		}
		else {
			mfaTimebasedOTPEntry.setFailedAttempts(
				mfaTimebasedOTPEntry.getFailedAttempts() + 1);
			mfaTimebasedOTPEntry.setLastFailDate(new Date());
			mfaTimebasedOTPEntry.setLastFailIP(ip);
		}

		return mfaTimebasedOTPEntryPersistence.update(mfaTimebasedOTPEntry);
	}

}
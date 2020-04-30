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

package com.liferay.multi.factor.authentication.timebased.otp.model.impl;

import com.liferay.multi.factor.authentication.timebased.otp.model.MFATimebasedOTPEntry;
import com.liferay.petra.lang.HashUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.MVCCModel;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.util.Date;

/**
 * The cache model class for representing MFATimebasedOTPEntry in entity cache.
 *
 * @author Arthur Chan
 * @generated
 */
public class MFATimebasedOTPEntryCacheModel
	implements CacheModel<MFATimebasedOTPEntry>, Externalizable, MVCCModel {

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof MFATimebasedOTPEntryCacheModel)) {
			return false;
		}

		MFATimebasedOTPEntryCacheModel mfaTimebasedOTPEntryCacheModel =
			(MFATimebasedOTPEntryCacheModel)obj;

		if ((mfaTimebasedOTPEntryId ==
				mfaTimebasedOTPEntryCacheModel.mfaTimebasedOTPEntryId) &&
			(mvccVersion == mfaTimebasedOTPEntryCacheModel.mvccVersion)) {

			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		int hashCode = HashUtil.hash(0, mfaTimebasedOTPEntryId);

		return HashUtil.hash(hashCode, mvccVersion);
	}

	@Override
	public long getMvccVersion() {
		return mvccVersion;
	}

	@Override
	public void setMvccVersion(long mvccVersion) {
		this.mvccVersion = mvccVersion;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(27);

		sb.append("{mvccVersion=");
		sb.append(mvccVersion);
		sb.append(", mfaTimebasedOTPEntryId=");
		sb.append(mfaTimebasedOTPEntryId);
		sb.append(", companyId=");
		sb.append(companyId);
		sb.append(", userId=");
		sb.append(userId);
		sb.append(", userName=");
		sb.append(userName);
		sb.append(", createDate=");
		sb.append(createDate);
		sb.append(", modifiedDate=");
		sb.append(modifiedDate);
		sb.append(", failedAttempts=");
		sb.append(failedAttempts);
		sb.append(", lastFailDate=");
		sb.append(lastFailDate);
		sb.append(", lastFailIP=");
		sb.append(lastFailIP);
		sb.append(", lastSuccessDate=");
		sb.append(lastSuccessDate);
		sb.append(", lastSuccessIP=");
		sb.append(lastSuccessIP);
		sb.append(", sharedSecret=");
		sb.append(sharedSecret);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public MFATimebasedOTPEntry toEntityModel() {
		MFATimebasedOTPEntryImpl mfaTimebasedOTPEntryImpl =
			new MFATimebasedOTPEntryImpl();

		mfaTimebasedOTPEntryImpl.setMvccVersion(mvccVersion);
		mfaTimebasedOTPEntryImpl.setMfaTimebasedOTPEntryId(
			mfaTimebasedOTPEntryId);
		mfaTimebasedOTPEntryImpl.setCompanyId(companyId);
		mfaTimebasedOTPEntryImpl.setUserId(userId);

		if (userName == null) {
			mfaTimebasedOTPEntryImpl.setUserName("");
		}
		else {
			mfaTimebasedOTPEntryImpl.setUserName(userName);
		}

		if (createDate == Long.MIN_VALUE) {
			mfaTimebasedOTPEntryImpl.setCreateDate(null);
		}
		else {
			mfaTimebasedOTPEntryImpl.setCreateDate(new Date(createDate));
		}

		if (modifiedDate == Long.MIN_VALUE) {
			mfaTimebasedOTPEntryImpl.setModifiedDate(null);
		}
		else {
			mfaTimebasedOTPEntryImpl.setModifiedDate(new Date(modifiedDate));
		}

		mfaTimebasedOTPEntryImpl.setFailedAttempts(failedAttempts);

		if (lastFailDate == Long.MIN_VALUE) {
			mfaTimebasedOTPEntryImpl.setLastFailDate(null);
		}
		else {
			mfaTimebasedOTPEntryImpl.setLastFailDate(new Date(lastFailDate));
		}

		if (lastFailIP == null) {
			mfaTimebasedOTPEntryImpl.setLastFailIP("");
		}
		else {
			mfaTimebasedOTPEntryImpl.setLastFailIP(lastFailIP);
		}

		if (lastSuccessDate == Long.MIN_VALUE) {
			mfaTimebasedOTPEntryImpl.setLastSuccessDate(null);
		}
		else {
			mfaTimebasedOTPEntryImpl.setLastSuccessDate(
				new Date(lastSuccessDate));
		}

		if (lastSuccessIP == null) {
			mfaTimebasedOTPEntryImpl.setLastSuccessIP("");
		}
		else {
			mfaTimebasedOTPEntryImpl.setLastSuccessIP(lastSuccessIP);
		}

		if (sharedSecret == null) {
			mfaTimebasedOTPEntryImpl.setSharedSecret("");
		}
		else {
			mfaTimebasedOTPEntryImpl.setSharedSecret(sharedSecret);
		}

		mfaTimebasedOTPEntryImpl.resetOriginalValues();

		return mfaTimebasedOTPEntryImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		mvccVersion = objectInput.readLong();

		mfaTimebasedOTPEntryId = objectInput.readLong();

		companyId = objectInput.readLong();

		userId = objectInput.readLong();
		userName = objectInput.readUTF();
		createDate = objectInput.readLong();
		modifiedDate = objectInput.readLong();

		failedAttempts = objectInput.readInt();
		lastFailDate = objectInput.readLong();
		lastFailIP = objectInput.readUTF();
		lastSuccessDate = objectInput.readLong();
		lastSuccessIP = objectInput.readUTF();
		sharedSecret = objectInput.readUTF();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput) throws IOException {
		objectOutput.writeLong(mvccVersion);

		objectOutput.writeLong(mfaTimebasedOTPEntryId);

		objectOutput.writeLong(companyId);

		objectOutput.writeLong(userId);

		if (userName == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(userName);
		}

		objectOutput.writeLong(createDate);
		objectOutput.writeLong(modifiedDate);

		objectOutput.writeInt(failedAttempts);
		objectOutput.writeLong(lastFailDate);

		if (lastFailIP == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(lastFailIP);
		}

		objectOutput.writeLong(lastSuccessDate);

		if (lastSuccessIP == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(lastSuccessIP);
		}

		if (sharedSecret == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(sharedSecret);
		}
	}

	public long mvccVersion;
	public long mfaTimebasedOTPEntryId;
	public long companyId;
	public long userId;
	public String userName;
	public long createDate;
	public long modifiedDate;
	public int failedAttempts;
	public long lastFailDate;
	public String lastFailIP;
	public long lastSuccessDate;
	public String lastSuccessIP;
	public String sharedSecret;

}
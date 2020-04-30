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

package com.liferay.multi.factor.authentication.timebased.otp.model;

import com.liferay.petra.sql.dsl.Column;
import com.liferay.petra.sql.dsl.base.BaseTable;

import java.sql.Types;

import java.util.Date;

/**
 * The table class for the &quot;MFATimebasedOTPEntry&quot; database table.
 *
 * @author Arthur Chan
 * @see MFATimebasedOTPEntry
 * @generated
 */
public class MFATimebasedOTPEntryTable
	extends BaseTable<MFATimebasedOTPEntryTable> {

	public static final MFATimebasedOTPEntryTable INSTANCE =
		new MFATimebasedOTPEntryTable();

	public final Column<MFATimebasedOTPEntryTable, Long> mvccVersion =
		createColumn(
			"mvccVersion", Long.class, Types.BIGINT, Column.FLAG_NULLITY);
	public final Column<MFATimebasedOTPEntryTable, Long>
		mfaTimebasedOTPEntryId = createColumn(
			"mfaTimebasedOTPEntryId", Long.class, Types.BIGINT,
			Column.FLAG_PRIMARY);
	public final Column<MFATimebasedOTPEntryTable, Long> companyId =
		createColumn(
			"companyId", Long.class, Types.BIGINT, Column.FLAG_DEFAULT);
	public final Column<MFATimebasedOTPEntryTable, Long> userId = createColumn(
		"userId", Long.class, Types.BIGINT, Column.FLAG_DEFAULT);
	public final Column<MFATimebasedOTPEntryTable, String> userName =
		createColumn(
			"userName", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<MFATimebasedOTPEntryTable, Date> createDate =
		createColumn(
			"createDate", Date.class, Types.TIMESTAMP, Column.FLAG_DEFAULT);
	public final Column<MFATimebasedOTPEntryTable, Date> modifiedDate =
		createColumn(
			"modifiedDate", Date.class, Types.TIMESTAMP, Column.FLAG_DEFAULT);
	public final Column<MFATimebasedOTPEntryTable, Integer> failedAttempts =
		createColumn(
			"failedAttempts", Integer.class, Types.INTEGER,
			Column.FLAG_DEFAULT);
	public final Column<MFATimebasedOTPEntryTable, Date> lastFailDate =
		createColumn(
			"lastFailDate", Date.class, Types.TIMESTAMP, Column.FLAG_DEFAULT);
	public final Column<MFATimebasedOTPEntryTable, String> lastFailIP =
		createColumn(
			"lastFailIP", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<MFATimebasedOTPEntryTable, Date> lastSuccessDate =
		createColumn(
			"lastSuccessDate", Date.class, Types.TIMESTAMP,
			Column.FLAG_DEFAULT);
	public final Column<MFATimebasedOTPEntryTable, String> lastSuccessIP =
		createColumn(
			"lastSuccessIP", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<MFATimebasedOTPEntryTable, String> sharedSecret =
		createColumn(
			"sharedSecret", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);

	private MFATimebasedOTPEntryTable() {
		super("MFATimebasedOTPEntry", MFATimebasedOTPEntryTable::new);
	}

}
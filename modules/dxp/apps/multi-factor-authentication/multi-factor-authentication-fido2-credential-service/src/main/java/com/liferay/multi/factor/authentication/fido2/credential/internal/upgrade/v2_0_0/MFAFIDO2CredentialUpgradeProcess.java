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

package com.liferay.multi.factor.authentication.fido2.credential.internal.upgrade.v2_0_0;

import com.liferay.multi.factor.authentication.fido2.credential.internal.upgrade.v2_0_0.util.MFAFIDO2CredentialEntryTable;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.LoggingTimer;
import com.liferay.portal.kernel.util.Validator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Marta Medio
 */
public class MFAFIDO2CredentialUpgradeProcess extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {
		_dropIndexes();

		if (hasColumn("MFAFIDO2CredentialEntry", "credentialKey")) {
			alter(
				MFAFIDO2CredentialEntryTable.class,
				new AlterColumnType("credentialKey", "LONGTEXT null"));
		}

		if (!hasColumn("MFAFIDO2CredentialEntry", "credentialKeyHash")) {
			alter(
				MFAFIDO2CredentialEntryTable.class,
				new AlterTableAddColumn("credentialKeyHash", "LONG null"));

			_generateCredentialKeyHash();
		}

		_createIndexes();
	}

	private void _createIndexes() throws Exception {
		runSQLTemplateString(
			"create unique index IX_F2E36027 on MFAFIDO2CredentialEntry (" +
				"userId, credentialKeyHash)",
			false);
		runSQLTemplateString(
			"create index IX_A95911A1 on MFAFIDO2CredentialEntry (" +
				"credentialKeyHash)",
			false);
	}

	private void _dropIndexes() throws Exception {
		if (hasIndex("MFAFIDO2CredentialEntry", "IX_4C5F79F9")) {
			runSQLTemplateString(
				"drop index IX_4C5F79F9 on MFAFIDO2CredentialEntry", false);
		}

		if (hasIndex("MFAFIDO2CredentialEntry", "IX_2B0CF873")) {
			runSQLTemplateString(
				"drop index IX_2B0CF873 on MFAFIDO2CredentialEntry", false);
		}
	}

	private void _generateCredentialKeyHash() throws Exception {
		try (LoggingTimer loggingTimer = new LoggingTimer();
			ResultSet credentialKeysResultSet = _getCredentialKeys()) {

			while (credentialKeysResultSet.next()) {
				String credentialKey = credentialKeysResultSet.getString(
					"credentialKey");

				if (Validator.isNull(credentialKey)) {
					continue;
				}

				_updateCredentialKeys(credentialKey, credentialKey.hashCode());
			}
		}
	}

	private ResultSet _getCredentialKeys() throws SQLException {
		String sql =
			"select MFAFIDO2CredentialEntry.credentialKey from " +
				"MFAFIDO2CredentialEntry";

		PreparedStatement preparedStatement = connection.prepareStatement(sql);

		return preparedStatement.executeQuery();
	}

	private void _updateCredentialKeys(
			String credentialKey, int credentialKeyHash)
		throws SQLException {

		String sql =
			"update MFAFIDO2CredentialEntry set credentialKeyHash = ? where " +
				"credentialKey = ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(
				sql)) {

			preparedStatement.setLong(1, credentialKeyHash);
			preparedStatement.setString(2, credentialKey);

			preparedStatement.execute();
		}
	}

}
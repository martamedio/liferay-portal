create index IX_2B0CF873 on MFAFIDO2CredentialEntry (credentialKey[$COLUMN_LENGTH:2000000$]);
create index IX_A95911A1 on MFAFIDO2CredentialEntry (credentialKeyHash);
create unique index IX_4C5F79F9 on MFAFIDO2CredentialEntry (userId, credentialKey[$COLUMN_LENGTH:2000000$]);
create unique index IX_F2E36027 on MFAFIDO2CredentialEntry (userId, credentialKeyHash);
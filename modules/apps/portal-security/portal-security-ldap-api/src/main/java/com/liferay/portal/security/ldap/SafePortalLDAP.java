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

package com.liferay.portal.security.ldap;

import com.liferay.portal.security.ldap.validator.LDAPFilter;

import java.util.List;

import javax.naming.Binding;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;
import javax.naming.ldap.LdapName;

import org.osgi.annotation.versioning.ProviderType;

/**
 * @author Tomas Polesovsky
 * @author Marta Medio
 */
@ProviderType
public interface SafePortalLDAP {

	public Binding getGroup(long ldapServerId, long companyId, String groupName)
		throws Exception;

	public Attributes getGroupAttributes(
			long ldapServerId, long companyId, LdapContext ldapContext,
			LdapName userGroupDNLdapName)
		throws Exception;

	public Attributes getGroupAttributes(
			long ldapServerId, long companyId, LdapContext ldapContext,
			LdapName userGroupDNName, boolean includeReferenceAttributes)
		throws Exception;

	public byte[] getGroups(
			long companyId, LdapContext ldapContext, byte[] cookie,
			int maxResults, LdapName baseDNLdapName, LDAPFilter groupFilter,
			List<SearchResult> searchResults)
		throws Exception;

	public byte[] getGroups(
			long companyId, LdapContext ldapContext, byte[] cookie,
			int maxResults, LdapName baseDNLdapName, LDAPFilter groupFilter,
			String[] attributeIds, List<SearchResult> searchResults)
		throws Exception;

	public byte[] getGroups(
			long ldapServerId, long companyId, LdapContext ldapContext,
			byte[] cookie, int maxResults, List<SearchResult> searchResults)
		throws Exception;

	public byte[] getGroups(
			long ldapServerId, long companyId, LdapContext ldapContext,
			byte[] cookie, int maxResults, String[] attributeIds,
			List<SearchResult> searchResults)
		throws Exception;

	public String getGroupsDN(long ldapServerId, long companyId)
		throws Exception;

	public long getLdapServerId(
			long companyId, String screenName, String emailAddress)
		throws Exception;

	public Attribute getMultivaluedAttribute(
			long companyId, LdapContext ldapContext, LdapName baseDNLdapName,
			LDAPFilter ldapFilter, Attribute attribute)
		throws Exception;

	public SafeLdapContext getSafeLdapContext(
		long ldapServerId, long companyId);

	public SafeLdapContext getSafeLdapContext(
		long companyId, String providerURL, String principal,
		String credentials);

	public Binding getUser(
			long ldapServerId, long companyId, String screenName,
			String emailAddress)
		throws Exception;

	public Binding getUser(
			long ldapServerId, long companyId, String screenName,
			String emailAddress, boolean checkOriginalEmail)
		throws Exception;

	public Attributes getUserAttributes(
			long ldapServerId, long companyId, LdapContext ldapContext,
			LdapName fullDistinguishedLdapName)
		throws Exception;

	public byte[] getUsers(
			long companyId, LdapContext ldapContext, byte[] cookie,
			int maxResults, LdapName baseDNLdapName, LDAPFilter userFilter,
			List<SearchResult> searchResults)
		throws Exception;

	public byte[] getUsers(
			long companyId, LdapContext ldapContext, byte[] cookie,
			int maxResults, LdapName baseDNLdapName, LDAPFilter userFilter,
			String[] attributeIds, List<SearchResult> searchResults)
		throws Exception;

	public byte[] getUsers(
			long ldapServerId, long companyId, LdapContext ldapContext,
			byte[] cookie, int maxResults, List<SearchResult> searchResults)
		throws Exception;

	public byte[] getUsers(
			long ldapServerId, long companyId, LdapContext ldapContext,
			byte[] cookie, int maxResults, String[] attributeIds,
			List<SearchResult> searchResults)
		throws Exception;

	public String getUsersDN(long ldapServerId, long companyId)
		throws Exception;

	public boolean hasUser(
			long ldapServerId, long companyId, String screenName,
			String emailAddress)
		throws Exception;

	public boolean isGroupMember(
			long ldapServerId, long companyId, LdapName groupDNLdapName,
			LdapName userDNLdapName)
		throws Exception;

	public boolean isUserGroupMember(
			long ldapServerId, long companyId, LdapName groupDNLdapName,
			LdapName userDNLdapName)
		throws Exception;

	public byte[] searchLDAP(
			long companyId, LdapContext ldapContext, byte[] cookie,
			int maxResults, LdapName baseDNLdapName, LDAPFilter filter,
			String[] attributeIds, List<SearchResult> searchResults)
		throws Exception;

}
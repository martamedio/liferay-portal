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

package com.liferay.oauth2.provider.web.internal;

import com.liferay.oauth2.provider.scope.liferay.LiferayOAuth2Scope;
import com.liferay.oauth2.provider.scope.liferay.ScopeLocator;
import com.liferay.oauth2.provider.scope.liferay.spi.ScopeDescriptorLocator;
import com.liferay.oauth2.provider.scope.spi.scope.descriptor.ScopeDescriptor;
import com.liferay.petra.string.StringPool;

import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Marta Medio
 */
public class ScopeAliasScopeDescriptor {

	public ScopeAliasScopeDescriptor(
		long companyId, ScopeDescriptorLocator scopeDescriptorLocator,
		ScopeLocator scopeLocator) {

		_companyId = companyId;
		_scopeDescriptorLocator = scopeDescriptorLocator;
		_scopeDescriptor = scopeDescriptorLocator.getScopeDescriptor(
			_companyId);
		_scopeLocator = scopeLocator;
	}

	public String getDescription(
		String scopeAlias, Locale locale, String delimiter) {

		String description = null;

		if (_scopeDescriptor != null) {
			description = _scopeDescriptor.describeScope(scopeAlias, locale);
		}

		if (description == null) {
			description = StringPool.BLANK;

			Set<String> descriptions = new LinkedHashSet<>();

			for (LiferayOAuth2Scope liferayOAuth2Scope :
					_scopeLocator.getLiferayOAuth2Scopes(
						_companyId, scopeAlias)) {

				ScopeDescriptor applicationScopeDescriptor =
					_scopeDescriptorLocator.getScopeDescriptor(
						_companyId, liferayOAuth2Scope.getApplicationName());

				descriptions.add(
					applicationScopeDescriptor.describeScope(
						liferayOAuth2Scope.getScope(), locale));
			}

			if (!descriptions.isEmpty()) {
				Stream<String> streamDescriptions = descriptions.stream();

				description = streamDescriptions.collect(
					Collectors.joining(delimiter));
			}
		}

		return description;
	}

	private final long _companyId;
	private final ScopeDescriptor _scopeDescriptor;
	private final ScopeDescriptorLocator _scopeDescriptorLocator;
	private final ScopeLocator _scopeLocator;

}
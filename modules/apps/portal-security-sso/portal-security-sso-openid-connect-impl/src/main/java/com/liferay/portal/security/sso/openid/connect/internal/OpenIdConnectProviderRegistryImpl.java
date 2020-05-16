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

package com.liferay.portal.security.sso.openid.connect.internal;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.security.sso.openid.connect.OpenIdConnectProvider;
import com.liferay.portal.security.sso.openid.connect.OpenIdConnectProviderRegistry;
import com.liferay.portal.security.sso.openid.connect.OpenIdConnectServiceException;
import com.liferay.portal.security.sso.openid.connect.internal.configuration.OpenIdConnectProviderConfiguration;

import com.nimbusds.openid.connect.sdk.op.OIDCProviderMetadata;
import com.nimbusds.openid.connect.sdk.rp.OIDCClientMetadata;

import java.net.URL;

import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.osgi.framework.Constants;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedServiceFactory;
import org.osgi.service.component.annotations.Component;

/**
 * @author Thuong Dinh
 * @author Edward C. Han
 */
@Component(
	immediate = true,
	property = Constants.SERVICE_PID + "=com.liferay.portal.security.sso.openid.connect.internal.configuration.OpenIdConnectProviderConfiguration",
	service = {ManagedServiceFactory.class, OpenIdConnectProviderRegistry.class}
)
public class OpenIdConnectProviderRegistryImpl
	implements ManagedServiceFactory,
			   OpenIdConnectProviderRegistry
				   <OIDCClientMetadata, OIDCProviderMetadata> {

	@Override
	public void deleted(String factoryPid) {
		removeOpenConnectIdProvider(factoryPid);
	}

	@Override
	public OpenIdConnectProvider<OIDCClientMetadata, OIDCProviderMetadata>
			findOpenIdConnectProvider(long companyId, String name)
		throws OpenIdConnectServiceException.ProviderException {

		OpenIdConnectProvider openIdConnectProvider = getOpenIdConnectProvider(
			companyId, name);

		if (openIdConnectProvider == null) {
			throw new OpenIdConnectServiceException.ProviderException(
				"Unable to find an OpenId Connect provider with name " + name);
		}

		return openIdConnectProvider;
	}

	@Override
	public String getName() {
		return "OpenId Connect Provider Factory";
	}

	@Override
	public OpenIdConnectProvider<OIDCClientMetadata, OIDCProviderMetadata>
		getOpenIdConnectProvider(long companyId, String name) {

		return _openIdConnectProvidersPerCompanyName.get(
			_getOpenIdConnectCompanyName(companyId, name));
	}

	@Override
	public Collection<String> getOpenIdConnectProviderNames(long companyId) {
		if (_openIdConnectProvidersPerCompanyName.isEmpty()) {
			return Collections.emptySet();
		}

		Collection<String> openIdConnectProviderNames =
			_getOpenIdConnectProviderNamesByCompany(_DEFAULT_COMPANY_ID);

		openIdConnectProviderNames.addAll(
			_getOpenIdConnectProviderNamesByCompany(companyId));

		return openIdConnectProviderNames;
	}

	@Override
	public void updated(String factoryPid, Dictionary<String, ?> properties)
		throws ConfigurationException {

		OpenIdConnectProviderConfiguration openIdConnectProviderConfiguration =
			ConfigurableUtil.createConfigurable(
				OpenIdConnectProviderConfiguration.class, properties);

		synchronized (_openIdConnectProvidersPerFactory) {
			removeOpenConnectIdProvider(factoryPid);

			addOpenConnectIdConnectProvider(
				GetterUtil.getLong(properties.get("companyId")), factoryPid,
				createOpenIdConnectProvider(
					openIdConnectProviderConfiguration));
		}
	}

	protected void addOpenConnectIdConnectProvider(
		long companyId, String factoryPid,
		OpenIdConnectProvider openIdConnectProvider) {

		synchronized (_openIdConnectProvidersPerFactory) {
			String openIdConnectCompanyName = _getOpenIdConnectCompanyName(
				companyId, openIdConnectProvider.getName());

			_openIdConnectProvidersPerFactory.put(
				factoryPid, openIdConnectCompanyName);

			_openIdConnectProvidersPerCompanyName.put(
				openIdConnectCompanyName, openIdConnectProvider);
		}
	}

	protected OpenIdConnectProvider<OIDCClientMetadata, OIDCProviderMetadata>
			createOpenIdConnectProvider(
				OpenIdConnectProviderConfiguration
					openIdConnectProviderConfiguration)
		throws ConfigurationException {

		OpenIdConnectMetadataFactory openIdConnectMetadataFactory = null;

		try {
			if (Validator.isNotNull(
					openIdConnectProviderConfiguration.discoveryEndPoint())) {

				openIdConnectMetadataFactory =
					new OpenIdConnectMetadataFactoryImpl(
						openIdConnectProviderConfiguration.providerName(),
						new URL(
							openIdConnectProviderConfiguration.
								discoveryEndPoint()),
						openIdConnectProviderConfiguration.
							discoveryEndPointCacheInMillis());
			}
			else {
				openIdConnectMetadataFactory =
					new OpenIdConnectMetadataFactoryImpl(
						openIdConnectProviderConfiguration.providerName(),
						openIdConnectProviderConfiguration.
							idTokenSigningAlgValues(),
						openIdConnectProviderConfiguration.issuerURL(),
						openIdConnectProviderConfiguration.subjectTypes(),
						openIdConnectProviderConfiguration.jwksURI(),
						openIdConnectProviderConfiguration.
							authorizationEndPoint(),
						openIdConnectProviderConfiguration.tokenEndPoint(),
						openIdConnectProviderConfiguration.userInfoEndPoint());
			}
		}
		catch (Exception exception) {
			throw new ConfigurationException(
				null,
				StringBundler.concat(
					"Unable to instantiate provider metadata factory for ",
					openIdConnectProviderConfiguration.providerName(), ": ",
					exception.getMessage()),
				exception);
		}

		return new OpenIdConnectProviderImpl(
			openIdConnectProviderConfiguration.providerName(),
			openIdConnectProviderConfiguration.openIdConnectClientId(),
			openIdConnectProviderConfiguration.openIdConnectClientSecret(),
			openIdConnectProviderConfiguration.scopes(),
			openIdConnectMetadataFactory);
	}

	protected void removeOpenConnectIdProvider(String factoryPid) {
		synchronized (_openIdConnectProvidersPerFactory) {
			String openIdConnectCompanyName =
				_openIdConnectProvidersPerFactory.remove(factoryPid);

			if (openIdConnectCompanyName != null) {
				_openIdConnectProvidersPerCompanyName.remove(
					openIdConnectCompanyName);
			}
		}
	}

	private String _getOpenIdConnectCompanyName(long companyId, String name) {
		return companyId + StringPool.DASH + name;
	}

	private Collection<String> _getOpenIdConnectProviderNamesByCompany(
		long companyId) {

		Set<String> openIdConnectNames =
			_openIdConnectProvidersPerCompanyName.keySet();

		Stream<String> stream = openIdConnectNames.stream();

		return stream.filter(
			value -> value.startsWith(companyId + StringPool.DASH)
		).map(
			value -> value.substring(value.indexOf(StringPool.DASH) + 1)
		).collect(
			Collectors.toList()
		);
	}

	private static final long _DEFAULT_COMPANY_ID = 0;

	private final Map
		<String,
		 OpenIdConnectProvider<OIDCClientMetadata, OIDCProviderMetadata>>
			_openIdConnectProvidersPerCompanyName = new ConcurrentHashMap<>();
	private final Map<String, String> _openIdConnectProvidersPerFactory =
		new ConcurrentHashMap<>();

}
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

package com.liferay.multi.factor.authentication.web.internal.policy;

import com.liferay.multi.factor.authentication.spi.checker.browser.MFABrowserChecker;
import com.liferay.multi.factor.authentication.spi.checker.headless.MFAHeadlessChecker;
import com.liferay.multi.factor.authentication.web.internal.system.configuration.MFASystemConfiguration;
import com.liferay.osgi.service.tracker.collections.map.PropertyServiceReferenceMapper;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.module.configuration.ConfigurationProviderUtil;
import com.liferay.portal.kernel.util.ListUtil;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * @author Marta Medio
 */
@Component(service = MFAPolicy.class)
public class MFAPolicy {

	public List<MFABrowserChecker> getAvailableMFABrowserCheckers(
		long companyId, long userId) {

		List<MFABrowserChecker> mfaBrowserCheckers =
			_mfaBrowserCheckerServiceTrackerMap.getService(companyId);

		Stream<MFABrowserChecker> stream = mfaBrowserCheckers.stream();

		return stream.filter(
			mfaBrowserChecker -> mfaBrowserChecker.isAvailable(userId)
		).collect(
			Collectors.toList()
		);
	}

	public Optional<MFAHeadlessChecker> getMFAHeadlessChecker(long companyId) {
		List<MFAHeadlessChecker> mfaHeadlessCheckerList =
			_mfaHeadlessCheckerServiceTrackerMap.getService(companyId);

		if (!ListUtil.isEmpty(mfaHeadlessCheckerList)) {
			return Optional.of(mfaHeadlessCheckerList.get(0));
		}

		return Optional.empty();
	}

	public boolean isMFAEnabled(long companyId) {
		try {
			MFASystemConfiguration mfaSystemConfiguration =
				ConfigurationProviderUtil.getSystemConfiguration(
					MFASystemConfiguration.class);

			if (!mfaSystemConfiguration.disableGlobally()) {
				return !ListUtil.isEmpty(
					_mfaBrowserCheckerServiceTrackerMap.getService(companyId));
			}

			return false;
		}
		catch (ConfigurationException configurationException) {
			throw new SystemException(configurationException);
		}
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_mfaBrowserCheckerServiceTrackerMap =
			ServiceTrackerMapFactory.openMultiValueMap(
				bundleContext, MFABrowserChecker.class, "(companyId=*)",
				new PropertyServiceReferenceMapper<>("companyId"));

		_mfaHeadlessCheckerServiceTrackerMap =
			ServiceTrackerMapFactory.openMultiValueMap(
				bundleContext, MFAHeadlessChecker.class, "(companyId=*)",
				new PropertyServiceReferenceMapper<>("companyId"));
	}

	@Deactivate
	protected void deactivate() {
		_mfaBrowserCheckerServiceTrackerMap.close();
	}

	private ServiceTrackerMap<Long, List<MFABrowserChecker>>
		_mfaBrowserCheckerServiceTrackerMap;
	private ServiceTrackerMap<Long, List<MFAHeadlessChecker>>
		_mfaHeadlessCheckerServiceTrackerMap;

}
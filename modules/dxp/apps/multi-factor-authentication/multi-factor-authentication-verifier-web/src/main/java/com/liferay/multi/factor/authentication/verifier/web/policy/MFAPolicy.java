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

package com.liferay.multi.factor.authentication.verifier.web.policy;

import com.liferay.multi.factor.authentication.verifier.spi.checker.MFABrowserChecker;
import com.liferay.multi.factor.authentication.verifier.spi.checker.MFAHeadlessChecker;
import com.liferay.multi.factor.authentication.verifier.spi.checker.MFASetupChecker;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
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

	public List<MFABrowserChecker> getAvailableBrowserCheckers(
		long companyId, long userId) {

		List<MFABrowserChecker> activeMfaBrowserCheckers =
			_mfaBrowserCheckerServiceTrackerMap.getService(
				String.valueOf(companyId));

		Stream<MFABrowserChecker> stream = activeMfaBrowserCheckers.stream();

		return stream.filter(
			mfaBrowserChecker -> mfaBrowserChecker.isAvailable(userId)
		).collect(
			Collectors.toList()
		);
	}

	public Optional<MFAHeadlessChecker> getMFAHeadlessChecker(long companyId) {
		List<MFAHeadlessChecker> mfaHeadlessCheckerList =
			_mfaHeadlessCheckerServiceTrackerMap.getService(
				String.valueOf(companyId));

		if (!ListUtil.isEmpty(mfaHeadlessCheckerList)) {
			return Optional.of(mfaHeadlessCheckerList.get(0));
		}

		return Optional.empty();
	}

	public Optional<MFASetupChecker> getMFASetupChecker(long companyId) {
		List<MFASetupChecker> mfaSetupCheckerList =
			_mfaSetupCheckerServiceTrackerMap.getService(
				String.valueOf(companyId));

		if (!ListUtil.isEmpty(mfaSetupCheckerList)) {
			return Optional.of(mfaSetupCheckerList.get(0));
		}

		return Optional.empty();
	}

	public boolean isMFAEnabled(long companyId) {
		return !ListUtil.isEmpty(
			_mfaBrowserCheckerServiceTrackerMap.getService(
				String.valueOf(companyId)));
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_mfaBrowserCheckerServiceTrackerMap =
			ServiceTrackerMapFactory.openMultiValueMap(
				bundleContext, MFABrowserChecker.class, "companyId");
		_mfaHeadlessCheckerServiceTrackerMap =
			ServiceTrackerMapFactory.openMultiValueMap(
				bundleContext, MFAHeadlessChecker.class, "companyId");
		_mfaSetupCheckerServiceTrackerMap =
			ServiceTrackerMapFactory.openMultiValueMap(
				bundleContext, MFASetupChecker.class, "companyId");
	}

	@Deactivate
	protected void deactivate() {
		_mfaBrowserCheckerServiceTrackerMap.close();
		_mfaHeadlessCheckerServiceTrackerMap.close();
		_mfaSetupCheckerServiceTrackerMap.close();
	}

	private ServiceTrackerMap<String, List<MFABrowserChecker>>
		_mfaBrowserCheckerServiceTrackerMap;
	private ServiceTrackerMap<String, List<MFAHeadlessChecker>>
		_mfaHeadlessCheckerServiceTrackerMap;
	private ServiceTrackerMap<String, List<MFASetupChecker>>
		_mfaSetupCheckerServiceTrackerMap;

}
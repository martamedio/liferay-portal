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
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;

import java.util.ArrayList;
import java.util.List;
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
			_mfaBrowserCheckerServiceTrackerMap.getService(companyId);

		List<MFABrowserChecker> availableMfaBrowserCheckers = null;

		if (ListUtil.isNotEmpty(activeMfaBrowserCheckers)) {
			availableMfaBrowserCheckers = new ArrayList<>();

			for (MFABrowserChecker mfaBrowserChecker :
					activeMfaBrowserCheckers) {

				if (mfaBrowserChecker instanceof MFASetupChecker) {
					MFASetupChecker mfaSetupChecker =
						(MFASetupChecker)mfaBrowserChecker;

					if (mfaSetupChecker.isUserSetupComplete(userId)) {
						availableMfaBrowserCheckers.add(mfaBrowserChecker);
					}
				}
				else {
					availableMfaBrowserCheckers.add(mfaBrowserChecker);
				}
			}
		}

		return availableMfaBrowserCheckers;
	}

	public MFAHeadlessChecker getMFAHeadlessChecker(long companyId) {
		List<MFAHeadlessChecker> mfaHeadlessCheckerList =
			_mfaHeadlessCheckerServiceTrackerMap.getService(companyId);

		if (!ListUtil.isEmpty(mfaHeadlessCheckerList)) {
			Stream<MFAHeadlessChecker> stream = mfaHeadlessCheckerList.stream();

			MFAHeadlessChecker mfaHeadlessChecker = stream.findFirst(
			).get();

			if (mfaHeadlessChecker.isEnabled()) {
				return mfaHeadlessChecker;
			}
		}

		return null;
	}

	public MFASetupChecker getMFASetupChecker(long companyId) {
		List<MFASetupChecker> mfaSetupCheckerList =
			_mfaSetupCheckerServiceTrackerMap.getService(companyId);

		if (!ListUtil.isEmpty(mfaSetupCheckerList)) {
			Stream<MFASetupChecker> stream = mfaSetupCheckerList.stream();

			MFASetupChecker mfaSetupChecker = stream.findFirst(
			).get();

			if (mfaSetupChecker.isEnabled()) {
				return mfaSetupChecker;
			}
		}

		return null;
	}

	public boolean isMFAEnabled(long companyId, long userId) {
		List<MFABrowserChecker> availableBrowserCheckers =
			getAvailableBrowserCheckers(companyId, userId);

		if (ListUtil.isNotEmpty(availableBrowserCheckers)) {
			Stream<MFABrowserChecker> stream =
				availableBrowserCheckers.stream();

			MFABrowserChecker mfaBrowserChecker = stream.findFirst(
			).get();

			if ((mfaBrowserChecker != null) && mfaBrowserChecker.isEnabled()) {
				return true;
			}
		}

		return false;
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_mfaBrowserCheckerServiceTrackerMap =
			ServiceTrackerMapFactory.openMultiValueMap(
				bundleContext, MFABrowserChecker.class, "(companyId=*)",
				(serviceReference, emitter) -> emitter.emit(
					GetterUtil.getLong(
						serviceReference.getProperty("companyId"))));

		_mfaHeadlessCheckerServiceTrackerMap =
			ServiceTrackerMapFactory.openMultiValueMap(
				bundleContext, MFAHeadlessChecker.class, "(companyId=*)",
				(serviceReference, emitter) -> emitter.emit(
					GetterUtil.getLong(
						serviceReference.getProperty("companyId"))));

		_mfaSetupCheckerServiceTrackerMap =
			ServiceTrackerMapFactory.openMultiValueMap(
				bundleContext, MFASetupChecker.class, "(companyId=*)",
				(serviceReference, emitter) -> emitter.emit(
					GetterUtil.getLong(
						serviceReference.getProperty("companyId"))));
	}

	@Deactivate
	protected void deactivate() {
		_mfaBrowserCheckerServiceTrackerMap.close();
		_mfaHeadlessCheckerServiceTrackerMap.close();
		_mfaSetupCheckerServiceTrackerMap.close();
	}

	private ServiceTrackerMap<Long, List<MFABrowserChecker>>
		_mfaBrowserCheckerServiceTrackerMap;
	private ServiceTrackerMap<Long, List<MFAHeadlessChecker>>
		_mfaHeadlessCheckerServiceTrackerMap;
	private ServiceTrackerMap<Long, List<MFASetupChecker>>
		_mfaSetupCheckerServiceTrackerMap;

}
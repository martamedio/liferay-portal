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

package com.liferay.portal.kernel.util;

import com.liferay.portal.kernel.concurrent.DefaultNoticeableFuture;
import com.liferay.portal.kernel.concurrent.NoticeableFuture;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;

import java.util.Enumeration;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Michael C. Han
 * @author Shuyang Zhou
 * @author Marta Medio
 */
public class InetAddressUtil {

	public static InetAddress getInetAddressByName(String domain)
		throws UnknownHostException {

		int threadLimit = GetterUtil.getInteger(
			PropsUtil.get(PropsKeys.DNS_SECURITY_THREAD_LIMIT));

		AtomicInteger atomicInteger = new AtomicInteger(threadLimit);

		InetAddress inetAddress = null;

		try {
			if (atomicInteger.getAndDecrement() > 0) {
				Future<InetAddress> result = _submit(
					"InetAddressUtil", () -> InetAddress.getByName(domain));

				int timeout = GetterUtil.getInteger(
					PropsUtil.get(
						PropsKeys.DNS_SECURITY_ADDRESS_TIMEOUT_SECONDS));

				inetAddress = result.get(timeout, TimeUnit.SECONDS);
			}
			else {
				_log.error(
					"Thread limit exceeded to determine address for host: " +
						domain);
			}
		}
		catch (ExecutionException | InterruptedException | TimeoutException e) {
			if (_log.isDebugEnabled()) {
				_log.debug(e, e);
			}

			throw new UnknownHostException("Unable to resolve URL: " + domain);
		}
		finally {
			atomicInteger.incrementAndGet();
		}

		return inetAddress;
	}

	public static String getLocalHostName() throws Exception {
		return LocalHostNameHolder._LOCAL_HOST_NAME;
	}

	public static InetAddress getLocalInetAddress() throws Exception {
		Enumeration<NetworkInterface> enu1 =
			NetworkInterface.getNetworkInterfaces();

		while (enu1.hasMoreElements()) {
			NetworkInterface networkInterface = enu1.nextElement();

			Enumeration<InetAddress> enu2 = networkInterface.getInetAddresses();

			while (enu2.hasMoreElements()) {
				InetAddress inetAddress = enu2.nextElement();

				if (!inetAddress.isLoopbackAddress() &&
					(inetAddress instanceof Inet4Address)) {

					return inetAddress;
				}
			}
		}

		throw new SystemException("No local internet address");
	}

	public static InetAddress getLoopbackInetAddress()
		throws UnknownHostException {

		return InetAddress.getByName("127.0.0.1");
	}

	public static boolean isLocalInetAddress(InetAddress inetAddress) {
		if (inetAddress.isAnyLocalAddress() ||
			inetAddress.isLinkLocalAddress() ||
			inetAddress.isLoopbackAddress() ||
			inetAddress.isSiteLocalAddress()) {

			return true;
		}

		return false;
	}

	private static <T> NoticeableFuture<T> _submit(
		String threadName, Callable<T> callable) {

		DefaultNoticeableFuture<T> defaultNoticeableFuture =
			new DefaultNoticeableFuture<>(callable);

		Thread thread = new Thread(defaultNoticeableFuture, threadName);

		thread.setDaemon(true);

		thread.start();

		return defaultNoticeableFuture;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		InetAddressUtil.class);

	private static class LocalHostNameHolder {

		private static final String _LOCAL_HOST_NAME;

		static {
			try {
				InetAddress inetAddress = getLocalInetAddress();

				_LOCAL_HOST_NAME = inetAddress.getHostName();
			}
			catch (Exception e) {
				throw new ExceptionInInitializerError(e);
			}
		}

	}

}
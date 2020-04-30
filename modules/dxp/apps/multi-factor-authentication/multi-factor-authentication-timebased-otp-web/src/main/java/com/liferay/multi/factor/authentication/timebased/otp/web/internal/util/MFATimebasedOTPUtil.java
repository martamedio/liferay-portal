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

package com.liferay.multi.factor.authentication.timebased.otp.web.internal.util;

import com.liferay.petra.string.StringBundler;

import java.nio.ByteBuffer;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Arthur Chan
 */
public class MFATimebasedOTPUtil {

	public static final String MFA_TIMEBASED_OTP_ALGORITHM = "HmacSHA1";

	public static String generateTimebasedOTPBasedInHmac(
		byte[] key, long count, int digits) {

		if ((digits < 1) || (digits > 9)) {
			throw new IllegalArgumentException(
				StringBundler.concat(
					"Timebased OTP can only generate 1-9 digits but ", digits,
					" requested"));
		}

		Mac mac = null;

		try {
			mac = Mac.getInstance(MFA_TIMEBASED_OTP_ALGORITHM);

			mac.init(new SecretKeySpec(key, MFA_TIMEBASED_OTP_ALGORITHM));
		}
		catch (InvalidKeyException invalidKeyException) {
			throw new IllegalArgumentException(
				"Invalid secret key for algorithm " +
					MFA_TIMEBASED_OTP_ALGORITHM,
				invalidKeyException);
		}
		catch (NoSuchAlgorithmException noSuchAlgorithmException) {
			throw new IllegalArgumentException(
				"Invalid algorithm " + MFA_TIMEBASED_OTP_ALGORITHM,
				noSuchAlgorithmException);
		}

		ByteBuffer byteBuffer = ByteBuffer.allocate(8);

		byteBuffer.putLong(count);

		byte[] hmac = mac.doFinal(byteBuffer.array());

		int offset = hmac[hmac.length - 1] & 0xf;

		int binary =
			(hmac[offset + 0x3] & 0xff) | ((hmac[offset + 0x2] & 0xff) << 8) |
			((hmac[offset + 0x1] & 0xff) << 16) |
			((hmac[offset + 0x0] & 0x7f) << 24);

		int otp = binary % (int)Math.pow(10, digits);

		return String.format(StringBundler.concat("%0", digits, "d"), otp);
	}

	public static boolean verifyTimebasedOTP(
		byte[] key, String timebasedOTPValue, long clockSkewMs,
		long timeWindowMs, int digits) {

		long min = (System.currentTimeMillis() - clockSkewMs) / timeWindowMs;
		long max = (System.currentTimeMillis() + clockSkewMs) / timeWindowMs;

		for (long i = min; i <= max; i++) {
			String generatedTimebaseOtp = generateTimebasedOTPBasedInHmac(
				key, i, digits);

			if (generatedTimebaseOtp.equals(timebasedOTPValue)) {
				return true;
			}
		}

		return false;
	}

}
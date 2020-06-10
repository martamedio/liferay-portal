<%--
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
--%>

<%@ include file="/init.jsp" %>

<%
String mfaTimeBasedOTPAlgorithm = GetterUtil.getString(request.getAttribute(MFATimeBasedOTPWebKeys.MFA_TIME_BASED_OTP_ALGORITHM));
String mfaTimeBasedOTPCompanyName = GetterUtil.getString(request.getAttribute(MFATimeBasedOTPWebKeys.MFA_TIME_BASED_OTP_COMPANY_NAME));
int mfaTimeBasedOTPDigits = GetterUtil.getInteger(request.getAttribute(MFATimeBasedOTPWebKeys.MFA_TIME_BASED_OTP_DIGITS));
String mfaTimeBasedOTPSharedSecret = (String)session.getAttribute(MFATimeBasedOTPWebKeys.MFA_TIME_BASED_OTP_SHARED_SECRET);
int mfaTimeBasedOTPTimeCounter = GetterUtil.getInteger(request.getAttribute(MFATimeBasedOTPWebKeys.MFA_TIME_BASED_OTP_TIME_COUNTER));
String userEmailAddress = user.getEmailAddress();
%>

<div class="sheet-section">
	<div class="alert alert-info">
		<liferay-ui:message key="user-account-setup-description" />
	</div>

	<aui:input label="mfa-timebased-otp" name="mfaTimeBasedOTP" showRequiredLabel="yes" />

	<aui:input disabled="<%= true %>" label="shared-secret" name="sharedSecret" type="text" value="<%= mfaTimeBasedOTPSharedSecret %>" />

	<div class="qrcode-setup" id="<portlet:namespace/>qrcode"></div>
</div>

<div class="sheet-footer">
	<aui:button type="submit" value="submit" />
</div>

<aui:script require='<%= npmResolvedPackageName + "/qrcode/generateQRCode as generateQRCode" %>'>
	var account = '<%= HtmlUtil.escapeJS(userEmailAddress) %>';
	var algorithm = '<%= HtmlUtil.escapeJS(mfaTimeBasedOTPAlgorithm) %>';
	var digits = '<%= mfaTimeBasedOTPDigits %>';
	var issuer = '<%= HtmlUtil.escapeJS(mfaTimeBasedOTPCompanyName) %>';
	var secret = '<%= HtmlUtil.escapeJS(mfaTimeBasedOTPSharedSecret) %>';
	var timeCounter = '<%= mfaTimeBasedOTPTimeCounter %>';

	var url = new URL(
		'otpauth://totp/' +
			encodeURIComponent(issuer) +
			':' +
			encodeURIComponent(account)
	);

	url.searchParams.append('secret', secret);
	url.searchParams.append('issuer', issuer);
	url.searchParams.append('algoritm', algorithm);
	url.searchParams.append('digits', digits);
	url.searchParams.append('counter', timeCounter);

	generateQRCode.default('<portlet:namespace/>qrcode', url.toString());
</aui:script>
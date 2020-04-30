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
String algorithm = GetterUtil.getString(request.getAttribute("mfaTimebasedOTPAlgorithm"));
String companyName = GetterUtil.getString(request.getAttribute("companyName"));
String sharedSecret = (String)request.getAttribute("sharedSecret");

int digits = GetterUtil.getInteger(request.getAttribute("mfaTimebasedOTPDigits"));
int timeWindow = GetterUtil.getInteger(request.getAttribute("mfaTimeBasedOTPTimeWindow"));

User mfaUser = (User)request.getAttribute("mfaUser");

String qrCodeLibraryUrl = (String)request.getAttribute("qrCodeLibraryUrl");
%>

<div class="sheet-section">
	<aui:input name="sharedSecret" type="hidden" value="<%= sharedSecret %>" />

	<aui:input name="userId" type="hidden" value="<%= mfaUser.getUserId() %>" />

	<div class="alert alert-info">
		<liferay-ui:message key="user-account-setup-description" />
	</div>

	<aui:input label="mfa-timebased-otp" name="timebasedOtp" showRequiredLabel="yes" />

	<div id="<portlet:namespace/>qrcode"></div>
</div>

<div class="sheet-footer">
	<aui:button type="submit" value="submit" />
</div>

<script src="<%=qrCodeLibraryUrl%>" type="text/javascript"></script>

<script type="text/javascript">
	function <portlet:namespace/>renderQRCode() {
		var account = '<%= HtmlUtil.escapeJS(mfaUser.getEmailAddress()) %>';
		var algorithm = '<%= HtmlUtil.escapeJS(algorithm) %>';
		var digits = '<%= digits %>';
		var issuer = '<%= HtmlUtil.escapeJS(companyName) %>';
		var secret = '<%= HtmlUtil.escapeJS(sharedSecret) %>';
		var timeWindow = '<%= timeWindow %>';

		var text = "otpauth://totp/";
		text += encodeURIComponent(issuer) + ":" + encodeURIComponent(account);
		text += "?secret=" + encodeURIComponent(secret) + "&issuer=" + encodeURIComponent(issuer);
		text += "&algorithm="+encodeURIComponent(algorithm)+"&digits="+encodeURIComponent(digits);
		text += "&counter="+encodeURIComponent(timeWindow);

		new QRCode(document.getElementById("<portlet:namespace/>qrcode"), {
			text: text,
			width: 256,
			height: 256,
			colorDark: "#000000",
			colorLight: "#ffffff"
		});
	}

	<portlet:namespace/>renderQRCode();
</script>
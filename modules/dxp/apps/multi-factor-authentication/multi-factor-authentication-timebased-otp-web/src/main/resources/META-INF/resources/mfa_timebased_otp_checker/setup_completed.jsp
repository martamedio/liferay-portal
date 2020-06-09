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

<div class="sheet-section">
	<liferay-ui:message key="mfa-timebased-otp-verification-already-configured" />

	<aui:input name="mfaRemoveTimeBasedOTP" type="hidden" value="<%= true %>" />
</div>

<div class="sheet-footer">
	<aui:button type="submit" value="generate-new-verification" />
</div>
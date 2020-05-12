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
List<MFABrowserChecker> availableBrowserCheckers = (List<MFABrowserChecker>)request.getAttribute(MFAWebKeys.MFA_AVAILABLE_CHECKERS);
MFABrowserChecker mfaBrowserChecker = (MFABrowserChecker)request.getAttribute(MFAWebKeys.MFA_CHECKER);
int mfaCheckerIndex = ParamUtil.getInteger(request, "mfaCheckerIndex");
long mfaUserId = (Long)request.getAttribute(MFAWebKeys.MFA_USER_ID);
String mfaCheckerName = (String)request.getAttribute(MFAWebKeys.MFA_CHECKER_NAME);

String redirect = ParamUtil.getString(request, "redirect");
%>

<portlet:actionURL name="/mfa_verify/verify" var="verifyURL">
	<portlet:param name="mvcRenderCommandName" value="/mfa_verify/view" />
</portlet:actionURL>

<aui:form action="<%= verifyURL %>" cssClass="container-fluid-1280 sign-in-form" data-senna-off="true" method="post" name="fm">
	<aui:input name="mfaCheckerIndex" type="hidden" value="<%= mfaCheckerIndex %>" />
	<aui:input name="saveLastPath" type="hidden" value="<%= false %>" />
	<aui:input name="redirect" type="hidden" value="<%= redirect %>" />

	<liferay-ui:error key="mfaFailedVerification" message="multi-factor-authentication-has-failed" />

	<%
	mfaBrowserChecker.includeBrowserVerification(request, response, mfaUserId);
	%>

	<c:if test="<%= availableBrowserCheckers.size() > 1 %>">
		<portlet:renderURL copyCurrentRenderParameters="<%= true %>" var="useAnotherMFABrowserChecker">
			<portlet:param name="mfaCheckerIndex" value='<%= ((mfaCheckerIndex + 1) < availableBrowserCheckers.size()) ? String.valueOf(mfaCheckerIndex + 1) : "0" %>' />
			<portlet:param name="mvcRenderCommandName" value="/mfa_verify/view" />
			<portlet:param name="redirect" value='<%= ParamUtil.getString(request, "redirect") %>' />
			<portlet:param name="saveLastPath" value="<%= Boolean.FALSE.toString() %>" />
		</portlet:renderURL>

		<b><a href="<%= HtmlUtil.escapeAttribute(useAnotherMFABrowserChecker) %>"><liferay-ui:message key="use-another-mfa-verifier" />: <%= mfaCheckerName %></a></b>
	</c:if>

	<aui:button-row>
		<aui:button type="submit" value="submit" />
	</aui:button-row>
</aui:form>
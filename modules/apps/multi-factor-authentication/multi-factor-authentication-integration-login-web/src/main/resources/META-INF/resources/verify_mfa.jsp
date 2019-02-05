<%--
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
--%>

<%@ include file="/init.jsp" %>

<portlet:actionURL name="/login/verify_mfa" var="verifyMFAActionURL">
	<portlet:param name="mvcRenderCommandName" value="/login/verify_mfa" />
</portlet:actionURL>

<aui:form action="<%= verifyMFAActionURL %>" cssClass="sign-in-form container-fluid-1280" method="post" name="fm">
	<aui:input name="encryptedParameterMapJSON" type="hidden" value="<%= ParamUtil.getString(renderRequest, "encryptedParameterMapJSON")%>" />
	<aui:input name="redirect" type="hidden" value="<%= ParamUtil.getString(renderRequest, "redirect") %>" />

	<liferay-ui:error key="mfaFailed" message="multi-factor-authentication-failed" />

	<liferay-util:dynamic-include key="com.liferay.multi.factor.authentication.integration.login.web#/verify_mfa.jsp" />

	<aui:button-row>
		<aui:button type="submit" value="submit" />
	</aui:button-row>
</aui:form>
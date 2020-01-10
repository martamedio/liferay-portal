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

<%@ include file="/admin/init.jsp" %>

<%
String redirect = ParamUtil.getString(request, "redirect");

OAuth2Application oAuth2Application = oAuth2AdminPortletDisplayContext.getOAuth2Application();

AssignSimpleScopesDisplayContext assignSimpleScopesDisplayContext = (AssignSimpleScopesDisplayContext)oAuth2AdminPortletSimpleDisplayContext;
%>

<div class="container-fluid container-fluid-max-xl container-view">
	<liferay-ui:error exception="<%= OAuth2ApplicationClientCredentialUserIdException.class %>">

		<%
		OAuth2ApplicationClientCredentialUserIdException oAuth2ApplicationClientCredentialUserIdException = ((OAuth2ApplicationClientCredentialUserIdException)errorException);
		%>

		<c:choose>
			<c:when test="<%= Validator.isNotNull(oAuth2ApplicationClientCredentialUserIdException.getClientCredentialUserScreenName()) %>">
				<liferay-ui:message arguments="<%= oAuth2ApplicationClientCredentialUserIdException.getClientCredentialUserScreenName() %>" key="this-operation-cannot-be-performed-because-you-cannot-impersonate-x" />
			</c:when>
			<c:otherwise>
				<liferay-ui:message arguments="<%= oAuth2ApplicationClientCredentialUserIdException.getClientCredentialUserId() %>" key="this-operation-cannot-be-performed-because-you-cannot-impersonate-x" />
			</c:otherwise>
		</c:choose>
	</liferay-ui:error>

	<div class="row">
		<div class="col-lg-12">
			<portlet:actionURL name="/admin/assign_scopes" var="assignScopesURL">
				<portlet:param name="mvcRenderCommandName" value="/admin/assign_scopes" />
				<portlet:param name="appTab" value="assign_scopes" />
				<portlet:param name="backURL" value="<%= redirect %>" />
				<portlet:param name="oAuth2ApplicationId" value="<%= String.valueOf(oAuth2Application.getOAuth2ApplicationId()) %>" />
			</portlet:actionURL>

			<aui:form action="<%= assignScopesURL %>" name="fm">
				<ul class="list-group">

					<%
						Set<String> assignedScopeAliases = assignSimpleScopesDisplayContext.getAssignedScopeAliases();
						Set<String> deletedAssignedScopeAliases = assignSimpleScopesDisplayContext.getAssignedDeletedScopeAliases();
						Map<String, String> scopeAliases = assignSimpleScopesDisplayContext.getAvailableScopeAliases();

					for (Map.Entry<String, String> scopeData : scopeAliases.entrySet()) {
						String scopeKey = scopeData.getKey();
						String scopeDescription = scopeData.getValue();

						boolean removedScope = deletedAssignedScopeAliases.contains(scopeKey);
						boolean assignedScope = assignedScopeAliases.contains(scopeKey);
					%>

						<li class="list-group-item list-group-item-flex<c:if test="<%= removedScope %>"> removed-scope</c:if>">
							<div class="autofit-col">
								<div class="form-group form-inline input-checkbox-wrapper">
									<aui:input checked="<%= assignedScope %>" disabled="<%= removedScope %>" id="<%= scopeKey %>" label="" name="scopeAliases" type="checkbox" value="<%= scopeKey %>" />
								</div>
							</div>

							<div class="autofit-col autofit-col-expand">
								<h4 class="list-group-title text-truncate">
									<label for="<%= scopeKey %>">
										<div>
											<%= scopeKey %>
										</div>
									</label>
								</h4>

								<div>
									<span class="badge badge-secondary">
										<span class="badge-item">
											<%= scopeDescription %>
										</span>
									</span>
								</div>
							</div>
						</li>

					<%
					}
					%>

				</ul>

				<aui:button-row>
					<aui:button id="save" type="submit" value="save" />

					<aui:button href="<%= PortalUtil.escapeRedirect(redirect) %>" type="cancel" />
				</aui:button-row>
			</aui:form>
		</div>
	</div>
</div>

<aui:script>
	A.one('#<portlet:namespace />save').on('click', function(event) {
		event.preventDefault();

		var scopeAliases = [];

		A.all('input[name="<portlet:namespace />scopeAliases"]:checked').each(
			function() {
				scopeAliases.push(this.val());
			}
		);

		document.<portlet:namespace/>fm.submit();
	});
</aui:script>
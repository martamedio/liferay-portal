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

AssignScopesTreeDisplayContext
	assignScopesTreeDisplayContext = (AssignScopesTreeDisplayContext)oAuth2AdminPortletTreeDisplayContext;

Tree.Node availableScopeAliases = assignScopesTreeDisplayContext.getScopeAliasTreeNode();

Set<String> assignedScopeAliases = assignScopesTreeDisplayContext.getAssignedScopeAliases();
Set<String> deletedScopeAliases = assignScopesTreeDisplayContext.getAssignedDeletedScopeAliases();

Map<String, String> scopeAliasesDescriptions = assignScopesTreeDisplayContext.getScopeAliasDescriptionMap();

pageContext.setAttribute("assignedScopeAliases", assignedScopeAliases);
pageContext.setAttribute("deletedScopeAliases", deletedScopeAliases);
pageContext.setAttribute("scopeAliasesDescriptions", scopeAliasesDescriptions);
%>

<div class="container-fluid container-fluid-max-xl container-view">

<div class="sheet">
	<div class="sheet-header">
		<h2 class="sheet-title"><liferay-ui:message key="scopes" /></h2>

		<div class="sheet-text"><liferay-ui:message key="scopes-description" /></div>
	</div>

<div class="sheet-section">
	<liferay-ui:error exception="<%= OAuth2ApplicationClientCredentialUserIdException.class %>">

		<%
		OAuth2ApplicationClientCredentialUserIdException oAuth2ApplicationClientCredentialUserIdException = (OAuth2ApplicationClientCredentialUserIdException)errorException;
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
					<liferay-tree:tree
						trees="<%= availableScopeAliases.getTrees() %>"
					>
						<jsp:attribute
							name="nodeJspFragment"
						>
						<li class="borderless list-group-item<c:if test="${deletedScopeAliases.contains(node.value)}"> removed-scope</c:if>" id="${node.value}-container">
							<div class="row">
									<c:choose>
										<c:when test="${parentNodes.size() > 0}">
										<div class="col-md-6">
											<div class="scope-children-${parentNodes.size()}">
												<aui:input checked="${assignedScopeAliases.contains(node.value)}" data-has-childrens="true" data-parent="${parents.getFirst().value}" disabled="${deletedScopeAliases.contains(node.value)}" id="${node.value}" label="${node.value}" name="scopeAliases" type="checkbox" value="${node.value}" />
											</div>
										</div>
										</c:when>
										<c:otherwise>
										<div class="col-md-6">
											<aui:input checked="${assignedScopeAliases.contains(node.value)}" data-has-childrens="true" disabled="${deletedScopeAliases.contains(node.value)}" id="${node.value}" label="${node.value}" name="scopeAliases" type="checkbox" value="${node.value}" />
										</div>
										</c:otherwise>
									</c:choose>
								<div class="col-md-6 text-left">
									${scopeAliasesDescriptions.get(node.value)}
								</div>
							</div>
						</li>

						<liferay-tree:render-children
							trees="${node.children}"
						/>
						</jsp:attribute>

						<jsp:attribute
							name="leafJspFragment"
						>
						<li class="borderless list-group-item<c:if test="${deletedScopeAliases.contains(node.value)}"> removed-scope</c:if>" id="${node.value}-container">
							<div class="row">
									<c:choose>
										<c:when test="${parentNodes.size() > 0}">
										<div class="col-md-6">
											<div class="scope-children-${parentNodes.size()}">
												<aui:input checked="${assignedScopeAliases.contains(node.value)}" data-parent="${parents.getFirst().value}" disabled="${deletedScopeAliases.contains(node.value)}" id="${node.value}" label="${node.value}" name="scopeAliases" type="checkbox" value="${node.value}" />
											</div>
										</div>
										</c:when>
										<c:otherwise>
										<div class="col-md-6">
											<aui:input checked="${assignedScopeAliases.contains(node.value)}" disabled="${deletedScopeAliases.contains(node.value)}" id="${node.value}" label="${node.value}" name="scopeAliases" type="checkbox" value="${node.value}" />
										</div>
										</c:otherwise>
									</c:choose>
								<div class="col-md-6 text-left">
									${scopeAliasesDescriptions.get(node.value)}
								</div>
							</div>
						</li>
						</jsp:attribute>
					</liferay-tree:tree>
					</li>
				</ul>

				<aui:button-row>
					<aui:button id="save" type="submit" value="save" />

					<aui:button href="<%= PortalUtil.escapeRedirect(redirect) %>" type="cancel" />
				</aui:button-row>
			</aui:form>
		</div>
	</div>
</div>
</div>
</div>

<aui:script require="metal-dom/src/dom as dom">
	AUI().use('node', 'aui-modal', function(A) {
		A.all('input[name="<portlet:namespace />scopeAliases"]').each(function() {
			this.on('click', function() {
				<portlet:namespace />recalculateScopeChildrens(this);
				<portlet:namespace />recalculateScopeParents(this);
			});
		});

		<portlet:namespace />recalculateScopeChildrens = function(checkboxElement) {
			var valueId = checkboxElement.val();
			var isChecked = checkboxElement.attr('checked');
			A.all('input[data-parent=' + valueId + ']').each(function() {
				this.attr('checked', isChecked);
				var hasChildrens = checkboxElement.attr('data-has-childrens');
				if (hasChildrens) {
					<portlet:namespace />recalculateScopeChildrens(this);
				}
			});
		};

		<portlet:namespace />recalculateScopeParents = function(checkboxElement) {
			var parent = checkboxElement.attr('data-parent');
			var isChecked = checkboxElement.attr('checked');

			if (parent && !isChecked) {
				var parentElement = A.one('input[value=' + parent + ']');
				parentElement.attr('checked', isChecked);
				<portlet:namespace />recalculateScopeParents(parentElement);
			}
		};

		<portlet:namespace />checkNewScopesInCheckedParents = function() {
			A.all('input[data-has-childrens="true"]').each(function() {
				if (this.attr('checked')) {
					var parentValue = this.attr('value');
					A.all('input[data-parent=' + parentValue + ']').each(
						function() {
							if (!this.attr('checked')) {
								this.attr('checked', true);
								var elementId = this.attr('value');
								A.one('#' + elementId + '-container').addClass(
									'added-scope'
								);
							}
						}
					);
				}
			});
		};

		<portlet:namespace />checkNewScopesInCheckedParents();
	});
</aui:script>
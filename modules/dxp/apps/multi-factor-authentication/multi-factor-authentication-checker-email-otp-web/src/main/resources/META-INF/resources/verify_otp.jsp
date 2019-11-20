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

<div id="<portlet:namespace/>phaseOne">
	<h2>
		<liferay-ui:message key="your-one-time-password-will-be-sent-to-your-email-address" />
	</h2>

	<aui:button id="sendEmailButton" value="send" />
</div>

<div id="<portlet:namespace/>messageContainer"></div>

<div id="<portlet:namespace/>phaseTwo">
	<h2>
		<liferay-ui:message key="please-enter-the-one-time-password-from-the-email" />
	</h2>

	<aui:input name="otp" showRequiredLabel="yes" />
</div>

<aui:script use="aui-base,aui-io-request">
	<liferay-portlet:resourceURL id="/mfa/sendemailotp" portletName="<%= MFAPortletKeys.MFA_VERIFY_PORTLET %>" var="sendOTPURL">
	</liferay-portlet:resourceURL>

	A.one('#<portlet:namespace />sendEmailButton').on(
		'click',
		function(event) {
			var sendEmailButton = A.one('#<portlet:namespace />sendEmailButton');

			sendEmailButton.setAttribute('disabled', 'disabled');

			var buttonText = sendEmailButton.text();

			var data = {
				p_auth: Liferay.authToken
			};

			var setupEmail = A.one('#<portlet:namespace />setupEmail');

			if (setupEmail) {
				data["email"] = setupEmail.val();
			}

			var sendOTPURL = '<%= HtmlUtil.escapeJS(sendOTPURL) %>';

			A.io.request(
				sendOTPURL,
				{
					dataType: 'JSON',
					data: data,
					method: 'POST',
					on: {
						failure: function(event, id, obj) {
							var messageContainer = A.one('#<portlet:namespace />messageContainer');
							messageContainer.html('<span class="alert alert-danger"><liferay-ui:message key="unable-to-send-email" /></span>');

							sendEmailButton.text(buttonText);
							sendEmailButton.removeAttribute('disabled');
						},
						success: function(event, id, obj) {
							var messageContainer = A.one('#<portlet:namespace />messageContainer');
							messageContainer.html('<span class="alert alert-success"><liferay-ui:message key="email-sent-please-enter-the-received-code" /></span>');

							var phaseTwo = A.one('#<portlet:namespace />phaseTwo');
							phaseTwo.disabled = false;
						}
					}
				}
			);
		}
	);
</aui:script>
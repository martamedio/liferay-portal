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

package com.liferay.multi.factor.authentication.checker.email.otp.web.internal.portlet.action;

import com.liferay.multi.factor.authentication.checker.email.otp.web.internal.checker.EmailOTPMFAChecker;
import com.liferay.multi.factor.authentication.checker.email.otp.web.internal.configuration.EmailOTPConfiguration;
import com.liferay.multi.factor.authentication.checker.email.otp.web.internal.constants.LoginPortletKeys;
import com.liferay.multi.factor.authentication.checker.email.otp.web.internal.constants.MFAPortletKeys;
import com.liferay.multi.factor.authentication.checker.email.otp.web.internal.constants.WebKeys;
import com.liferay.petra.encryptor.Encryptor;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.module.configuration.ConfigurationProviderUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.LiferayPortletURL;
import com.liferay.portal.kernel.portlet.PortletURLFactory;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.auth.session.AuthenticatedSessionManagerUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Accessor;
import com.liferay.portal.kernel.util.DigesterUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.security.Key;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.ActionURL;
import javax.portlet.PortletRequest;
import javax.portlet.WindowState;
import javax.portlet.filter.ActionRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Tomas Polesovsky
 */
@Component(
	property = {
		"javax.portlet.name=" + LoginPortletKeys.FAST_LOGIN,
		"javax.portlet.name=" + LoginPortletKeys.LOGIN,
		"mvc.command.name=/login/login", "service.ranking:Integer=1"
	},
	service = MVCActionCommand.class
)
public class LoginMFAMVCActionCommand extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		EmailOTPConfiguration emailOTPConfiguration =
			ConfigurationProviderUtil.getCompanyConfiguration(
				EmailOTPConfiguration.class,
				_portal.getCompanyId(actionRequest));

		if (!emailOTPConfiguration.enabled()) {
			_loginMVCActionCommand.processAction(actionRequest, actionResponse);

			return;
		}

		String state = ParamUtil.getString(actionRequest, "state");

		if (!Validator.isBlank(state)) {
			actionRequest = _loadRequestFromState(actionRequest, state);
		}

		String login = ParamUtil.getString(actionRequest, "login");
		String password = ParamUtil.getString(actionRequest, "password");

		if (!Validator.isBlank(login) && !Validator.isBlank(password)) {
			HttpServletRequest httpServletRequest =
				_portal.getOriginalServletRequest(
					_portal.getHttpServletRequest(actionRequest));

			long userId =
				AuthenticatedSessionManagerUtil.getAuthenticatedUserId(
					httpServletRequest, login, password, null);

			if ((userId > 0) &&
				!_emailOTPMFAChecker.isBrowserVerified(
					httpServletRequest, userId)) {

				_redirectToVerify(userId, actionRequest, actionResponse);

				return;
			}
		}

		_loginMVCActionCommand.processAction(actionRequest, actionResponse);
	}

	private LiferayPortletURL _createLiferayPortletURL(
		HttpServletRequest httpServletRequest, String redirectURL) {

		httpServletRequest = _portal.getOriginalServletRequest(
			httpServletRequest);

		long plid = 0;
		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				com.liferay.portal.kernel.util.WebKeys.THEME_DISPLAY);

		if (themeDisplay != null) {
			plid = themeDisplay.getPlid();
		}

		LiferayPortletURL liferayPortletURL = _portletURLFactory.create(
			httpServletRequest, MFAPortletKeys.MFA_VERIFY_PORTLET, plid,
			PortletRequest.RENDER_PHASE);

		liferayPortletURL.setParameter(
			"saveLastPath", Boolean.FALSE.toString());
		liferayPortletURL.setParameter(
			"mvcRenderCommandName", "/mfa_verify/view");
		liferayPortletURL.setParameter("redirect", redirectURL);

		return liferayPortletURL;
	}

	private Map<String, Object> _decrypt(
			HttpSession httpSession, String encryptedStateMapJSON)
		throws Exception {

		String digest = (String)httpSession.getAttribute(WebKeys.DIGEST);

		if (!StringUtil.equals(
				DigesterUtil.digest(encryptedStateMapJSON), digest)) {

			throw new PrincipalException("User sent unverified data");
		}

		Key key = (Key)httpSession.getAttribute(WebKeys.KEY);

		String stateMapJSON = Encryptor.decrypt(key, encryptedStateMapJSON);

		Map<String, Object> stateMap = _jsonFactory.looseDeserialize(
			stateMapJSON, Map.class);

		Map<String, Object> requestParameters =
			(Map<String, Object>)stateMap.get("requestParameters");

		for (Map.Entry<String, Object> entry : requestParameters.entrySet()) {
			Object value = entry.getValue();

			if (value instanceof List) {
				entry.setValue(
					ListUtil.toArray((List<?>)value, _STRING_ACCESSOR));
			}
		}

		return stateMap;
	}

	private String _encrypt(
			HttpSession httpSession, Map<String, Object> stateMap)
		throws Exception {

		Key key = Encryptor.generateKey();
		String stateMapJSON = _jsonFactory.looseSerializeDeep(stateMap);

		String encryptedStateMapJSON = Encryptor.encrypt(key, stateMapJSON);

		httpSession.setAttribute(
			WebKeys.DIGEST, DigesterUtil.digest(encryptedStateMapJSON));

		httpSession.setAttribute(WebKeys.KEY, key);

		return encryptedStateMapJSON;
	}

	private ActionRequest _loadRequestFromState(
			ActionRequest actionRequest, String encryptedStateMap)
		throws Exception {

		HttpServletRequest httpServletRequest =
			_portal.getOriginalServletRequest(
				_portal.getHttpServletRequest(actionRequest));

		Map<String, Object> stateMap = _decrypt(
			httpServletRequest.getSession(), encryptedStateMap);

		Map<String, String[]> requestParameters =
			(Map<String, String[]>)stateMap.get("requestParameters");

		return new ActionRequestWrapper(actionRequest) {

			@Override
			public String getParameter(String name) {
				return MapUtil.getString(requestParameters, name, null);
			}

			@Override
			public Map<String, String[]> getParameterMap() {
				return new HashMap<>(requestParameters);
			}

			@Override
			public Enumeration<String> getParameterNames() {
				return Collections.enumeration(requestParameters.keySet());
			}

			@Override
			public String[] getParameterValues(String name) {
				return requestParameters.get(name);
			}

		};
	}

	private void _redirectToVerify(
			long userId, ActionRequest actionRequest,
			ActionResponse actionResponse)
		throws Exception {

		Map<String, Object> stateMap = HashMapBuilder.<String, Object>put(
			"requestParameters", actionRequest.getParameterMap()
		).build();

		HttpServletRequest httpServletRequest =
			_portal.getOriginalServletRequest(
				_portal.getHttpServletRequest(actionRequest));

		HttpSession httpSession = httpServletRequest.getSession();

		String state = _encrypt(httpSession, stateMap);

		LiferayPortletResponse liferayPortletResponse =
			_portal.getLiferayPortletResponse(actionResponse);

		ActionURL actionURL = liferayPortletResponse.createActionURL();

		actionURL.setParameter(ActionRequest.ACTION_NAME, "/login/login");
		actionURL.setParameter("state", state);

		httpSession.setAttribute(WebKeys.MFA_USER_ID, userId);

		LiferayPortletURL liferayPortletURL = _createLiferayPortletURL(
			httpServletRequest, actionURL.toString());

		String portletId = ParamUtil.getString(httpServletRequest, "p_p_id");

		if (LoginPortletKeys.FAST_LOGIN.equals(portletId)) {
			liferayPortletURL.setWindowState(actionRequest.getWindowState());
		}
		else {
			liferayPortletURL.setWindowState(WindowState.MAXIMIZED);
		}

		actionRequest.setAttribute(
			com.liferay.portal.kernel.util.WebKeys.REDIRECT,
			liferayPortletURL.toString());
	}

	private static final Accessor<Object, String> _STRING_ACCESSOR =
		new Accessor<Object, String>() {

			@Override
			public String get(Object object) {
				return String.valueOf(object);
			}

			@Override
			public Class<String> getAttributeClass() {
				return String.class;
			}

			@Override
			public Class<Object> getTypeClass() {
				return Object.class;
			}

		};

	@Reference
	private EmailOTPMFAChecker _emailOTPMFAChecker;

	@Reference
	private JSONFactory _jsonFactory;

	@Reference(
		target = "(component.name=com.liferay.login.web.internal.portlet.action.LoginMVCActionCommand)"
	)
	private MVCActionCommand _loginMVCActionCommand;

	@Reference
	private Portal _portal;

	@Reference
	private PortletURLFactory _portletURLFactory;

}
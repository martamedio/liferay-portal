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

package com.liferay.dynamic.data.mapping.taglib.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import com.liferay.dynamic.data.mapping.taglib.servlet.ServletContextUtil;

/**
 * @author Bruno Basto
 * @generated
 */
public abstract class BaseHTMLFieldTag extends com.liferay.taglib.util.IncludeTag {

	@Override
	public int doStartTag() throws JspException {
		setAttributeNamespace(_ATTRIBUTE_NAMESPACE);

		return super.doStartTag();
	}

	public long getClassNameId() {
		return _classNameId;
	}

	public long getClassPK() {
		return _classPK;
	}

	public com.liferay.portlet.dynamicdatamapping.storage.Field getField() {
		return _field;
	}

	public java.lang.String getFieldsNamespace() {
		return _fieldsNamespace;
	}

	public boolean getReadOnly() {
		return _readOnly;
	}

	public boolean getRepeatable() {
		return _repeatable;
	}

	public java.util.Locale getRequestedLocale() {
		return _requestedLocale;
	}

	public boolean getShowEmptyFieldLabel() {
		return _showEmptyFieldLabel;
	}
	
	public java.lang.Object getServletContext() {
		return _servletContext;
	}

	public void setClassNameId(long classNameId) {
		_classNameId = classNameId;

		setScopedAttribute("classNameId", classNameId);
	}

	public void setClassPK(long classPK) {
		_classPK = classPK;

		setScopedAttribute("classPK", classPK);
	}

	public void setField(com.liferay.portlet.dynamicdatamapping.storage.Field field) {
		_field = field;

		setScopedAttribute("field", field);
	}

	public void setFieldsNamespace(java.lang.String fieldsNamespace) {
		_fieldsNamespace = fieldsNamespace;

		setScopedAttribute("fieldsNamespace", fieldsNamespace);
	}

	public void setReadOnly(boolean readOnly) {
		_readOnly = readOnly;

		setScopedAttribute("readOnly", readOnly);
	}

	public void setRepeatable(boolean repeatable) {
		_repeatable = repeatable;

		setScopedAttribute("repeatable", repeatable);
	}

	public void setRequestedLocale(java.util.Locale requestedLocale) {
		_requestedLocale = requestedLocale;

		setScopedAttribute("requestedLocale", requestedLocale);
	}

	public void setShowEmptyFieldLabel(boolean showEmptyFieldLabel) {
		_showEmptyFieldLabel = showEmptyFieldLabel;

		setScopedAttribute("showEmptyFieldLabel", showEmptyFieldLabel);
	}
	
	public void setServletContext(java.lang.Object servletContext) {
		_servletContext = servletContext;

		setScopedAttribute("servletContext", servletContext);
	}
	
	@Override
	public void setPageContext(PageContext pageContext) {
		super.setPageContext(pageContext);

		setServletContext(ServletContextUtil.getServletContext());
	}

	@Override
	protected void cleanUp() {
		super.cleanUp();

		_classNameId = 0;
		_classPK = 0;
		_field = null;
		_fieldsNamespace = null;
		_readOnly = false;
		_repeatable = true;
		_requestedLocale = null;
		_showEmptyFieldLabel = true;
		_servletContext = null;
	}

	@Override
	protected String getEndPage() {
		return _END_PAGE;
	}

	@Override
	protected String getStartPage() {
		return _START_PAGE;
	}

	@Override
	protected void setAttributes(HttpServletRequest request) {
		setNamespacedAttribute(request, "classNameId", _classNameId);
		setNamespacedAttribute(request, "classPK", _classPK);
		setNamespacedAttribute(request, "field", _field);
		setNamespacedAttribute(request, "fieldsNamespace", _fieldsNamespace);
		setNamespacedAttribute(request, "readOnly", _readOnly);
		setNamespacedAttribute(request, "repeatable", _repeatable);
		setNamespacedAttribute(request, "requestedLocale", _requestedLocale);
		setNamespacedAttribute(request, "showEmptyFieldLabel", _showEmptyFieldLabel);
		setNamespacedAttribute(request, "servletContext", _servletContext);
	}

	protected static final String _ATTRIBUTE_NAMESPACE = "liferay-ddm:html-field:";

	private static final String _END_PAGE =
		"/taglib/html_field/end.jsp";

	private static final String _START_PAGE =
		"/taglib/html_field/start.jsp";

	private long _classNameId = 0;
	private long _classPK = 0;
	private com.liferay.portlet.dynamicdatamapping.storage.Field _field = null;
	private java.lang.String _fieldsNamespace = null;
	private boolean _readOnly = false;
	private boolean _repeatable = true;
	private java.util.Locale _requestedLocale = null;
	private boolean _showEmptyFieldLabel = true;
	private java.lang.Object _servletContext = null;

}
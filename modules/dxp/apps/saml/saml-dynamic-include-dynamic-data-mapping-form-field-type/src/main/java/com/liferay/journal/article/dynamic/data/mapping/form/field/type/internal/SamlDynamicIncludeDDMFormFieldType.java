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

package com.liferay.journal.article.dynamic.data.mapping.form.field.type.internal;

import com.liferay.dynamic.data.mapping.form.field.type.BaseDDMFormFieldType;
import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldType;
import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldTypeSettings;
import com.liferay.frontend.js.loader.modules.extender.npm.JSPackage;
import com.liferay.frontend.js.loader.modules.extender.npm.NPMResolver;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Stian Sigvartsen
 */
@Component(
	immediate = true,
	property = {
		"ddm.form.field.type.data.domain=saml_dynamic_include",
		"ddm.form.field.type.description=saml-dynamic-include-description",
		"ddm.form.field.type.display.order:Integer=10",
		"ddm.form.field.type.group=basic",
		"ddm.form.field.type.icon=web-content",
		"ddm.form.field.type.label=saml-dynamic-include",
		"ddm.form.field.type.name=saml_dynamic_include",
		"ddm.form.field.type.scope=saml"
	},
	service = DDMFormFieldType.class
)
public class SamlDynamicIncludeDDMFormFieldType extends BaseDDMFormFieldType {

//	@Override
//	public Class<? extends DDMFormFieldTypeSettings>
//		getDDMFormFieldTypeSettings() {
//
//		return JournalArticleDDMFormFieldTypeSettings.class;
//	}

	@Override
	public String getModuleName() {
		JSPackage jsPackage = _npmResolver.getJSPackage();

		return jsPackage.getResolvedId() + "/SamlDynamicInclude";
	}

	@Override
	public String getName() {
		return "saml_dynamic_include";
	}

	@Override
	public boolean isCustomDDMFormFieldType() {
		return true;
	}

	@Reference
	private NPMResolver _npmResolver;

}
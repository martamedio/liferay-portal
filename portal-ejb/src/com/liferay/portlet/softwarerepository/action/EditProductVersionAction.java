/**
 * Copyright (c) 2000-2006 Liferay, Inc. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.liferay.portlet.softwarerepository.action;

import com.liferay.portal.model.Layout;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.struts.PortletAction;
import com.liferay.portal.util.Constants;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.softwarerepository.NoSuchProductEntryException;
import com.liferay.portlet.softwarerepository.NoSuchProductVersionException;
import com.liferay.portlet.softwarerepository.service.SRProductVersionServiceUtil;
import com.liferay.util.ParamUtil;
import com.liferay.util.servlet.SessionErrors;

import javax.portlet.*;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * <a href="EditProductVersionAction.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Jorge Ferrer
 *
 */
public class EditProductVersionAction
	extends PortletAction {

	public void processAction(
			ActionMapping mapping, ActionForm form, PortletConfig config,
			ActionRequest req, ActionResponse res)
		throws Exception {

		String cmd = ParamUtil.getString(req, Constants.CMD);

		try {
			if (cmd.equals(Constants.ADD) || cmd.equals(Constants.UPDATE)) {
				updateEntry(req);
			}
			else if (cmd.equals(Constants.DELETE)) {
				deleteEntry(req);
			}

			sendRedirect(req, res);
		}
		catch (Exception e) {
			if (e instanceof NoSuchProductEntryException ||
				e instanceof PrincipalException) {

				SessionErrors.add(req, e.getClass().getName());

				setForward(req, "portlet.software_repository.error");
			}
			else {
				throw e;
			}
		}
	}

	public ActionForward render(
			ActionMapping mapping, ActionForm form, PortletConfig config,
			RenderRequest req, RenderResponse res)
		throws Exception {

		try {
			ActionUtil.getProductEntry(req);
			ActionUtil.getProductVersion(req);
		}
		catch (Exception e) {
			if (e instanceof NoSuchProductEntryException ||
				e instanceof NoSuchProductVersionException ||
				e instanceof PrincipalException) {

				SessionErrors.add(req, e.getClass().getName());

				return mapping.findForward("portlet.software_repository.error");
			}
			else {
				throw e;
			}
		}

		return mapping.findForward(getForward(
			req, "portlet.software_repository.edit_product_version"));
	}

	protected void deleteEntry(ActionRequest req) throws Exception {
		long productVersionId = ParamUtil.getLong(req, "productVersionId");

		SRProductVersionServiceUtil.deleteProductVersion(productVersionId);
	}

	protected void updateEntry(ActionRequest req) throws Exception {
		Layout layout = (Layout)req.getAttribute(WebKeys.LAYOUT);

		long productVersionId = ParamUtil.getLong(req, "productVersionId");
		long productEntryId = ParamUtil.getLong(req, "productEntryId");

		String version = ParamUtil.getString(req, "version");
		String changeLog = ParamUtil.getString(req, "changeLog");
		long[] frameworkVersionIds = ParamUtil.
			getLongValues(req, "frameworkVersions");
		String downloadPageURL = ParamUtil.getString(req, "downloadPageURL");
		String directDownloadURL = ParamUtil.getString(
			req, "directDownloadURL");
		boolean repoStoreArtifact = ParamUtil.getBoolean(
			req, "repoStoreArtifact");

		if (productVersionId <= 0) {

			// Add entry

			SRProductVersionServiceUtil.addProductVersion(
				productEntryId, version, changeLog, frameworkVersionIds,
				downloadPageURL, directDownloadURL, repoStoreArtifact);
		}
		else {

			// Update entry

			SRProductVersionServiceUtil.updateProductVersion(
				productVersionId, version, changeLog, frameworkVersionIds,
				downloadPageURL, directDownloadURL, repoStoreArtifact);
		}
	}

}
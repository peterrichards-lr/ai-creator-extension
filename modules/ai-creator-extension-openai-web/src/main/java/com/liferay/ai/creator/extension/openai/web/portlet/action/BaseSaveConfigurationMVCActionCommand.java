/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.ai.creator.extension.openai.web.portlet.action;

import com.liferay.ai.creator.extension.openai.web.client.AICreatorExtensionOpenAIClient;
import com.liferay.ai.creator.openai.web.internal.exception.AICreatorOpenAIClientException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.*;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;

/**
 * @author Lourdes Fernández Besada
 * @author Peter Richards
 */
public abstract class BaseSaveConfigurationMVCActionCommand
        extends BaseMVCActionCommand {

  @Reference(
          policy = ReferencePolicy.DYNAMIC,
          policyOption = ReferencePolicyOption.GREEDY
  )
  protected volatile AICreatorExtensionOpenAIClient aiCreatorOpenAIClient;
  @Reference
  protected Language language;
  @Reference
  protected Portal portal;

  protected abstract void checkPermission(ThemeDisplay themeDisplay)
          throws PortalException, PortletException;

  @Override
  protected void doProcessAction(
          ActionRequest actionRequest, ActionResponse actionResponse)
          throws Exception {

    ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(
            WebKeys.THEME_DISPLAY);

    checkPermission(themeDisplay);

    String successMessageKey = "your-request-completed-successfully";

    String apiKey = ParamUtil.getString(actionRequest, "apiKey");
    String chatGPTModel = ParamUtil.getString(actionRequest, "chatGPTModel");
    String dallEModel = ParamUtil.getString(actionRequest, "dallEModel");

    if (Validator.isNotNull(apiKey)) {
      try {
        aiCreatorOpenAIClient.validateAPIKey(apiKey);

        successMessageKey = "your-api-key-was-successfully-added";
      } catch (AICreatorOpenAIClientException
              aiCreatorOpenAIClientException) {

        SessionErrors.add(
                actionRequest, aiCreatorOpenAIClientException.getClass(),
                aiCreatorOpenAIClientException);

        hideDefaultErrorMessage(actionRequest);

        sendRedirect(
                actionRequest, actionResponse,
                _getRedirect(actionRequest, true, themeDisplay));

        return;
      }
    }

    saveAICreatorOpenAIConfiguration(
            apiKey, ParamUtil.getBoolean(actionRequest, "enableChatGPT"),
            ParamUtil.getBoolean(actionRequest, "enableDALLE"), chatGPTModel, dallEModel, themeDisplay);

    SessionMessages.add(
            actionRequest, "requestProcessed",
            language.get(themeDisplay.getLocale(), successMessageKey));

    sendRedirect(
            actionRequest, actionResponse,
            _getRedirect(actionRequest, false, themeDisplay));
  }

  protected abstract void saveAICreatorOpenAIConfiguration(
          String apiKey, boolean enableChatGPT, boolean enableDALLE,
          String chatGPTModel, String dallEModel,
          ThemeDisplay themeDisplay)
          throws ConfigurationException;

  private String _getRedirect(
          ActionRequest actionRequest, boolean addParameters,
          ThemeDisplay themeDisplay) {

    String redirect = ParamUtil.getString(actionRequest, "redirect");

    if (Validator.isNull(redirect)) {
      return redirect;
    }

    String namespace = portal.getPortletNamespace(themeDisplay.getPpid());

    redirect = HttpComponentsUtil.removeParameter(
            redirect, namespace + "apiKey");
    redirect = HttpComponentsUtil.removeParameter(
            redirect, namespace + "chatGPTModel");
    redirect = HttpComponentsUtil.removeParameter(
            redirect, namespace + "dalletModel");
    redirect = HttpComponentsUtil.removeParameter(
            redirect, namespace + "enableOpenAI");

    if (!addParameters) {
      return redirect;
    }

    String apiKey = ParamUtil.getString(actionRequest, "apiKey", null);

    if (apiKey != null) {
      redirect = HttpComponentsUtil.addParameter(
              redirect, namespace + "apiKey", apiKey);
    }

    String chatGPTModel = ParamUtil.getString(actionRequest, "chatGPTModel", null);

    if (chatGPTModel != null) {
      redirect = HttpComponentsUtil.addParameter(
              redirect, namespace + "chatGPTModel", chatGPTModel);

    }

    String dallEModel = ParamUtil.getString(actionRequest, "dallEModel", null);

    if (dallEModel != null) {
      redirect = HttpComponentsUtil.addParameter(
              redirect, namespace + "dallEModel", dallEModel);
    }

    return HttpComponentsUtil.addParameter(
            redirect, namespace + "enableOpenAI",
            ParamUtil.getBoolean(actionRequest, "enableOpenAI"));
  }
}
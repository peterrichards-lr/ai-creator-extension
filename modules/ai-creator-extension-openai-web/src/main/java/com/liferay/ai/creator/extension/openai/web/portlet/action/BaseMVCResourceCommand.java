package com.liferay.ai.creator.extension.openai.web.portlet.action;

/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import com.liferay.ai.creator.extension.openai.configuration.manager.AICreatorExtensionOpenAIConfigurationManager;
import com.liferay.ai.creator.extension.openai.web.client.AICreatorExtensionOpenAIClient;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.portlet.JSONPortletResponseUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

/**
 * @author Roberto Díaz
 * @author Lourdes Fernández Besada
 * @author Peter Richards
 */
public abstract class BaseMVCResourceCommand
        extends com.liferay.portal.kernel.portlet.bridges.mvc.
        BaseMVCResourceCommand {

  @Reference(
          policy = ReferencePolicy.DYNAMIC,
          policyOption = ReferencePolicyOption.GREEDY
  )
  protected volatile AICreatorExtensionOpenAIClient aiCreatorOpenAIClient;
  @Reference
  protected AICreatorExtensionOpenAIConfigurationManager
          aiCreatorOpenAIConfigurationManager;
  @Reference
  protected Language language;

  protected void addDisabledConfigurationErrorMessage(
          ResourceRequest resourceRequest, ResourceResponse resourceResponse)
          throws Exception {

    ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(
            WebKeys.THEME_DISPLAY);

    JSONPortletResponseUtil.writeJSON(
            resourceRequest, resourceResponse,
            JSONUtil.put(
                    "error",
                    JSONUtil.put(
                            "message",
                            language.get(
                                    themeDisplay.getLocale(),
                                    "openai-is-disabled.-enable-openai-from-the-settings-" +
                                            "page-or-contact-your-administrator")
                    ).put(
                            "retry", false
                    )));
  }

  protected void addInvalidAPIKeyErrorMessage(
          ResourceRequest resourceRequest, ResourceResponse resourceResponse)
          throws Exception {

    ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(
            WebKeys.THEME_DISPLAY);

    JSONPortletResponseUtil.writeJSON(
            resourceRequest, resourceResponse,
            JSONUtil.put(
                    "error",
                    JSONUtil.put(
                            "message",
                            language.get(
                                    themeDisplay.getLocale(),
                                    "authentication-is-needed-to-use-this-feature.-" +
                                            "contact-your-administrator-to-add-an-api-key-in-" +
                                            "instance-or-site-settings")
                    ).put(
                            "retry", false
                    )));
  }

  protected void addRequiredFieldErrorMessage(
          ResourceRequest resourceRequest, ResourceResponse resourceResponse,
          String fieldName)
          throws Exception {

    ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(
            WebKeys.THEME_DISPLAY);

    JSONPortletResponseUtil.writeJSON(
            resourceRequest, resourceResponse,
            JSONUtil.put(
                    "error",
                    JSONUtil.put(
                            "message",
                            language.format(
                                    themeDisplay.getLocale(), "the-x-is-required",
                                    fieldName)
                    ).put(
                            "retry", false
                    )));
  }

  protected void handleAICreatorOpenAIClientExceptionMessages(
          ResourceRequest resourceRequest, ResourceResponse resourceResponse,
          String message)
          throws Exception {

    JSONPortletResponseUtil.writeJSON(
            resourceRequest, resourceResponse,
            JSONUtil.put("error", JSONUtil.put("message", message)));
  }

}
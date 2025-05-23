/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.ai.creator.extension.openai.web.portlet.action;

import com.liferay.ai.creator.openai.web.internal.constants.AICreatorOpenAIPortletKeys;
import com.liferay.ai.creator.openai.web.internal.exception.AICreatorOpenAIClientException;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.JSONPortletResponseUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import org.osgi.service.component.annotations.Component;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

/**
 * @author Lourdes Fernández Besada]
 * @author Peter Richards
 */
@Component(
        property = {
                "javax.portlet.name=" + AICreatorOpenAIPortletKeys.AI_CREATOR_OPENAI,
                "mvc.command.name=/ai_creator_openai/get_completion",
                "service.ranking:Integer=200"
        },
        service = MVCResourceCommand.class
)
public class GetCompletionMVCResourceCommand extends com.liferay.ai.creator.extension.openai.web.portlet.action.BaseMVCResourceCommand {

  private static final Log _log = LogFactoryUtil.getLog(
          GetCompletionMVCResourceCommand.class);

  @Override
  protected void doServeResource(
          ResourceRequest resourceRequest, ResourceResponse resourceResponse)
          throws Exception {

    ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(
            WebKeys.THEME_DISPLAY);

    if (!aiCreatorOpenAIConfigurationManager.isAICreatorChatGPTGroupEnabled(
            themeDisplay.getCompanyId(), themeDisplay.getScopeGroupId())) {

      addDisabledConfigurationErrorMessage(
              resourceRequest, resourceResponse);

      return;
    }

    String apiKey =
            aiCreatorOpenAIConfigurationManager.getAICreatorOpenAIGroupAPIKey(
                    themeDisplay.getCompanyId(), themeDisplay.getScopeGroupId());
    String chatGPTModel =
            aiCreatorOpenAIConfigurationManager.getGroupChatCPTModel(themeDisplay.getCompanyId(), themeDisplay.getScopeGroupId());


    _log.error(
            StringBundler.concat(
                    "Model: ", chatGPTModel));

    if (Validator.isNull(apiKey)) {
      addInvalidAPIKeyErrorMessage(resourceRequest, resourceResponse);

      return;
    }

    String content = ParamUtil.getString(resourceRequest, "content");

    if (Validator.isNull(content)) {
      addRequiredFieldErrorMessage(
              resourceRequest, resourceResponse, "content");

      return;
    }

    try {
      JSONPortletResponseUtil.writeJSON(
              resourceRequest, resourceResponse,
              JSONUtil.put(
                      "completion",
                      JSONUtil.put(
                              "content",
                              Validator.isBlank(chatGPTModel) ?
                                      aiCreatorOpenAIClient.getCompletion(
                                              apiKey, content,
                                              LocaleUtil.fromLanguageId(
                                                      ParamUtil.getString(
                                                              resourceRequest, "languageId",
                                                              themeDisplay.getLanguageId())),
                                              ParamUtil.getString(
                                                      resourceRequest, "tone", "formal"),
                                              ParamUtil.getInteger(resourceRequest, "words"))
                                      :
                                      aiCreatorOpenAIClient.getCompletion(
                                              apiKey, content,
                                              LocaleUtil.fromLanguageId(
                                                      ParamUtil.getString(
                                                              resourceRequest, "languageId",
                                                              themeDisplay.getLanguageId())),
                                              ParamUtil.getString(
                                                      resourceRequest, "tone", "formal"),
                                              ParamUtil.getInteger(resourceRequest, "words"),
                                              chatGPTModel))));
    } catch (AICreatorOpenAIClientException aiCreatorOpenAIClientException) {
      handleAICreatorOpenAIClientExceptionMessages(
              resourceRequest, resourceResponse,
              aiCreatorOpenAIClientException.getCompletionLocalizedMessage(
                      themeDisplay.getLocale()));
    }
  }

}
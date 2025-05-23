/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.ai.creator.extension.openai.web.portlet.action;

import com.liferay.ai.creator.extension.openai.configuration.manager.AICreatorExtensionOpenAIConfigurationManager;
import com.liferay.configuration.admin.constants.ConfigurationAdminPortletKeys;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.service.permission.GroupPermissionUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.portlet.PortletException;

/**
 * @author Lourdes Fernández Besada
 * @author Peter Richards
 */
@Component(
        property = {
                "javax.portlet.name=" + ConfigurationAdminPortletKeys.SITE_SETTINGS,
                "mvc.command.name=/site_settings/save_group_configuration",
                "service.ranking:Integer=110"
        },
        service = MVCActionCommand.class
)
public class SaveGroupConfigurationMVCActionCommand
        extends com.liferay.ai.creator.extension.openai.web.portlet.action.BaseSaveConfigurationMVCActionCommand {

  @Reference
  private AICreatorExtensionOpenAIConfigurationManager
          _aiCreatorOpenAIConfigurationManager;

  @Override
  protected void checkPermission(ThemeDisplay themeDisplay)
          throws PortalException, PortletException {

    GroupPermissionUtil.check(
            themeDisplay.getPermissionChecker(), themeDisplay.getScopeGroup(),
            ActionKeys.UPDATE);
  }

  @Override
  protected void saveAICreatorOpenAIConfiguration(
          String apiKey, boolean enableChatGPT, boolean enableDALLE, String chatGPTMode, String dalletModel,
          ThemeDisplay themeDisplay)
          throws ConfigurationException {

    _aiCreatorOpenAIConfigurationManager.
            saveAICreatorOpenAIGroupConfiguration(
                    themeDisplay.getScopeGroupId(), apiKey, enableChatGPT,
                    enableDALLE,
                    chatGPTMode, dalletModel);
  }
}
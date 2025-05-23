package com.liferay.ai.creator.extension.openai.configuration.manager;

import com.liferay.ai.creator.openai.configuration.manager.AICreatorOpenAIConfigurationManager;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;

/**
 * @author Peter Richards
 */
public interface AICreatorExtensionOpenAIConfigurationManager extends AICreatorOpenAIConfigurationManager {
  String getCompanyChatCPTModel(long companyId)
          throws ConfigurationException;

  String getGroupChatCPTModel(long companyId, long groupId)
          throws ConfigurationException;

  String getCompanyDALLEModel(long companyId)
          throws ConfigurationException;

  String getGroupDALLEModel(long companyId, long groupId)
          throws ConfigurationException;

  void saveAICreatorOpenAICompanyConfiguration(
          long companyId, String apiKey, boolean enableChatGPT,
          boolean enableDALLE, String chatGPTModel, String dalleModel)
          throws ConfigurationException;

  void saveAICreatorOpenAIGroupConfiguration(
          long groupId, String apiKey, boolean enableChatGPT,
          boolean enableDALLE, String chatGPTModel, String dallEModel)
          throws ConfigurationException;
}

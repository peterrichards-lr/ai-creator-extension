package com.liferay.ai.creator.openai.extension.internal.configuration.manager;

import com.liferay.ai.creator.openai.configuration.manager.AICreatorOpenAIConfigurationManager;
import com.liferay.ai.creator.openai.extension.configuration.AICreatorOpenAICompanyModelsConfiguration;
import com.liferay.ai.creator.openai.extension.configuration.AICreatorOpenAIGroupModelsConfiguration;
import com.liferay.ai.creator.openai.extension.configuration.AICreatorOpenAIModelConstants;
import com.liferay.ai.creator.openai.extension.configuration.manager.AICreatorOpenAIModelsConfigurationManager;
import com.liferay.ai.creator.openai.internal.configuration.manager.AICreatorOpenAIConfigurationManagerImpl;
import com.liferay.portal.configuration.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapDictionaryBuilder;
import com.liferay.portal.kernel.util.Validator;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.lang.reflect.Field;

/**
 * AICreatorOpenAIModelsConfigurationManagerImpl is an implementation of the AICreatorOpenAIModelsConfigurationManager interface.
 *
 * @author dnebing
 */
@Component(
        property = {
                "service.ranking:Integer=200"
        },
        service = {AICreatorOpenAIConfigurationManager.class, AICreatorOpenAIModelsConfigurationManager.class})
public class AICreatorOpenAIModelsConfigurationManagerImpl extends AICreatorOpenAIConfigurationManagerImpl implements AICreatorOpenAIModelsConfigurationManager {

  private ConfigurationProvider _configProvider;

  @Override
  public String getChatGPTCompanyModel(long companyId) throws ConfigurationException {
    try {
      return GetterUtil.getString(getAICreatorOpenAICompanyModelsConfiguration(companyId).chatGPTModel(), AICreatorOpenAIModelConstants.DEFAULT_CHATGPT_MODEL);
    } catch (ConfigurationException e) {
      return AICreatorOpenAIModelConstants.DEFAULT_CHATGPT_MODEL;
    }
  }

  @Override
  public String getDALLECompanyModel(long companyId) throws ConfigurationException {
    try {
      return GetterUtil.getString(getAICreatorOpenAICompanyModelsConfiguration(companyId).dALLEModel(), AICreatorOpenAIModelConstants.DEFAULT_DALL_E_MODEL);
    } catch (ConfigurationException e) {
      return AICreatorOpenAIModelConstants.DEFAULT_DALL_E_MODEL;
    }
  }

  @Override
  public String getChatGPTGroupModel(long companyId, long groupId) throws ConfigurationException {
    // okay, so first let's try and get the configuration for the group...
    try {
      AICreatorOpenAIGroupModelsConfiguration groupConfiguration = _configProvider.getGroupConfiguration(AICreatorOpenAIGroupModelsConfiguration.class, groupId);

      if (!Validator.isNull(groupConfiguration)) {
        if (!Validator.isBlank(groupConfiguration.chatGPTModel())) {
          return groupConfiguration.chatGPTModel();
        }
      }
    } catch (ConfigurationException e) {
      // ignored because we'll fall back to the company configuration
    }

    // if we get here, we'll use the company configuration
    return getChatGPTCompanyModel(companyId);
  }

  @Override
  public String getDALLEGroupModel(long companyId, long groupId) throws ConfigurationException {
    // okay, so first let's try and get the configuration for the group...
    try {
      AICreatorOpenAIGroupModelsConfiguration groupConfiguration = _configProvider.getGroupConfiguration(AICreatorOpenAIGroupModelsConfiguration.class, groupId);

      if (groupConfiguration != null) {
        if (!Validator.isBlank(groupConfiguration.dALLEModel())) {
          return groupConfiguration.dALLEModel();
        }
      }
    } catch (ConfigurationException e) {
      // ignored because we'll fall back to the company configuration
    }

    // if we get here, we'll use the company configuration
    return getDALLECompanyModel(companyId);
  }

  @Override
  public String getChatGPTGroupModel(long groupId) throws ConfigurationException {
    try {
      return GetterUtil.getString(getAICreatorOpenAIGroupModelsConfiguration(groupId).chatGPTModel(), AICreatorOpenAIModelConstants.DEFAULT_CHATGPT_MODEL);
    } catch (ConfigurationException e) {
      return AICreatorOpenAIModelConstants.DEFAULT_CHATGPT_MODEL;
    }
  }

  @Override
  public String getDALLEGroupModel(long groupId) throws ConfigurationException {
    try {
      return GetterUtil.getString(getAICreatorOpenAIGroupModelsConfiguration(groupId).dALLEModel(), AICreatorOpenAIModelConstants.DEFAULT_DALL_E_MODEL);
    } catch (ConfigurationException e) {
      return AICreatorOpenAIModelConstants.DEFAULT_DALL_E_MODEL;
    }
  }

  @Override
  public void saveAICreatorOpenAICompanyModelsConfiguration(
          long companyId, String apiKey, boolean enableChatGPT,
          boolean enableDALLE, String chatGPTModel, String dallEModel)
          throws ConfigurationException {

    // Save the super class configuration
    saveAICreatorOpenAICompanyConfiguration(companyId, apiKey, enableChatGPT, enableDALLE);

    // Save the company models configuration
    _configProvider.saveCompanyConfiguration(AICreatorOpenAICompanyModelsConfiguration.class, companyId,
            HashMapDictionaryBuilder.<String, Object>put(
                    AICreatorOpenAIModelConstants.DEFAULT_CHATGPT_MODEL_KEY, chatGPTModel
            ).put(
                    AICreatorOpenAIModelConstants.DEFAULT_DALL_E_MODEL_KEY, dallEModel
            ).build());
  }

  @Override
  public void saveAICreatorOpenAIGroupModelsConfiguration(
          long companyId, long groupId, String apiKey, boolean enableChatGPT,
          boolean enableDALLE, String chatGPTModel, String dallEModel)
          throws ConfigurationException {

    // Save the super class configuration
    saveAICreatorOpenAIGroupConfiguration(groupId, apiKey, enableChatGPT, enableDALLE);

    // Save the group models configuration
    _configProvider.saveGroupConfiguration(AICreatorOpenAIGroupModelsConfiguration.class, groupId,
            HashMapDictionaryBuilder.<String, Object>put(
                    AICreatorOpenAIModelConstants.DEFAULT_CHATGPT_MODEL_KEY, chatGPTModel
            ).put(
                    AICreatorOpenAIModelConstants.DEFAULT_DALL_E_MODEL_KEY, dallEModel
            ).build());
  }

  private AICreatorOpenAICompanyModelsConfiguration getAICreatorOpenAICompanyModelsConfiguration(long companyId) throws ConfigurationException {
    AICreatorOpenAICompanyModelsConfiguration aiCreatorOpenAICompanyModelsConfiguration = _configProvider.getCompanyConfiguration(AICreatorOpenAICompanyModelsConfiguration.class, companyId);
    if (aiCreatorOpenAICompanyModelsConfiguration == null) {
      throw new ConfigurationException("AICreatorOpenAICompanyModelsConfiguration not found for companyId: " + companyId);
    }
    return aiCreatorOpenAICompanyModelsConfiguration;
  }

  private AICreatorOpenAIGroupModelsConfiguration getAICreatorOpenAIGroupModelsConfiguration(long groupId) throws ConfigurationException {
    AICreatorOpenAIGroupModelsConfiguration aiCreatorOpenAIGroupModelsConfiguration = _configProvider.getGroupConfiguration(AICreatorOpenAIGroupModelsConfiguration.class, groupId);
    if (aiCreatorOpenAIGroupModelsConfiguration == null) {
      throw new ConfigurationException("AICreatorOpenAIGroupModelsConfiguration not found for groupId: " + groupId);
    }
    return aiCreatorOpenAIGroupModelsConfiguration;
  }

  @Reference
  public void setConfigurationProvider(ConfigurationProvider configurationProvider) {
    _configProvider = configurationProvider;

    // need to use reflection to set the value in the super class
    try {
      Field field = getClass().getSuperclass().getDeclaredField("_configurationProvider");
      field.setAccessible(true);
      field.set(this, configurationProvider);
    } catch (NoSuchFieldException | IllegalAccessException exception) {
      throw new RuntimeException("Unable to set _configurationProvider in superclass", exception);
    }
  }
}

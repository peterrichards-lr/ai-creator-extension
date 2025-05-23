package ai.creator.extension.openapi.configuration.manager;

import com.liferay.ai.creator.extension.openai.configuration.AICreatorExtensionOpenAICompanyConfiguration;
import com.liferay.ai.creator.extension.openai.configuration.AICreatorExtensionOpenAIGroupConfiguration;
import com.liferay.ai.creator.extension.openai.configuration.manager.AICreatorExtensionOpenAIConfigurationManager;
import com.liferay.portal.configuration.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.util.HashMapDictionaryBuilder;
import com.liferay.portal.kernel.util.Validator;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
        property = "service.ranking:Integer=110",
        service = AICreatorExtensionOpenAIConfigurationManager.class)
public class AICreatorExtensionOpenAIConfigurationManagerImpl implements AICreatorExtensionOpenAIConfigurationManager {
  @Reference
  private ConfigurationProvider _configurationProvider;

  @Override
  public String getAICreatorOpenAICompanyAPIKey(long companyId)
          throws ConfigurationException {

    AICreatorExtensionOpenAICompanyConfiguration
            aiCreatorOpenAICompanyConfiguration =
            _configurationProvider.getCompanyConfiguration(
                    AICreatorExtensionOpenAICompanyConfiguration.class, companyId);

    return aiCreatorOpenAICompanyConfiguration.apiKey();
  }

  @Override
  public String getAICreatorOpenAIGroupAPIKey(long groupId)
          throws ConfigurationException {

    AICreatorExtensionOpenAIGroupConfiguration aiCreatorOpenAIGroupConfiguration =
            _configurationProvider.getGroupConfiguration(
                    AICreatorExtensionOpenAIGroupConfiguration.class, groupId);

    return aiCreatorOpenAIGroupConfiguration.apiKey();
  }

  @Override
  public String getAICreatorOpenAIGroupAPIKey(long companyId, long groupId)
          throws ConfigurationException {

    AICreatorExtensionOpenAIGroupConfiguration aiCreatorOpenAIGroupConfiguration =
            _configurationProvider.getGroupConfiguration(
                    AICreatorExtensionOpenAIGroupConfiguration.class, groupId);

    if (Validator.isNotNull(aiCreatorOpenAIGroupConfiguration.apiKey())) {
      return aiCreatorOpenAIGroupConfiguration.apiKey();
    }

    AICreatorExtensionOpenAICompanyConfiguration
            aiCreatorOpenAICompanyConfiguration =
            _configurationProvider.getCompanyConfiguration(
                    AICreatorExtensionOpenAICompanyConfiguration.class, companyId);

    return aiCreatorOpenAICompanyConfiguration.apiKey();
  }

  @Override
  public boolean isAICreatorChatGPTCompanyEnabled(long companyId)
          throws ConfigurationException {

    AICreatorExtensionOpenAICompanyConfiguration
            aiCreatorOpenAICompanyConfiguration =
            _configurationProvider.getCompanyConfiguration(
                    AICreatorExtensionOpenAICompanyConfiguration.class, companyId);

    return aiCreatorOpenAICompanyConfiguration.
            enableChatGPTToCreateContent();
  }

  @Override
  public boolean isAICreatorChatGPTGroupEnabled(long companyId, long groupId)
          throws ConfigurationException {

    if (!isAICreatorChatGPTCompanyEnabled(companyId)) {
      return false;
    }

    AICreatorExtensionOpenAIGroupConfiguration aiCreatorOpenAIGroupConfiguration =
            _configurationProvider.getGroupConfiguration(
                    AICreatorExtensionOpenAIGroupConfiguration.class, groupId);

    return aiCreatorOpenAIGroupConfiguration.enableChatGPTToCreateContent();
  }

  @Override
  public boolean isAICreatorDALLECompanyEnabled(long companyId)
          throws ConfigurationException {

    AICreatorExtensionOpenAICompanyConfiguration
            aiCreatorOpenAICompanyConfiguration =
            _configurationProvider.getCompanyConfiguration(
                    AICreatorExtensionOpenAICompanyConfiguration.class, companyId);

    return aiCreatorOpenAICompanyConfiguration.enableDALLEToCreateImages();
  }

  @Override
  public boolean isAICreatorDALLEGroupEnabled(long companyId, long groupId)
          throws ConfigurationException {

    if (!isAICreatorDALLECompanyEnabled(companyId)) {
      return false;
    }

    AICreatorExtensionOpenAIGroupConfiguration aiCreatorOpenAIGroupConfiguration =
            _configurationProvider.getGroupConfiguration(
                    AICreatorExtensionOpenAIGroupConfiguration.class, groupId);

    return aiCreatorOpenAIGroupConfiguration.enableDALLEToCreateImages();
  }

  @Override
  public void saveAICreatorOpenAICompanyConfiguration(
          long companyId, String apiKey, boolean enableChatGPT,
          boolean enableDALLE)
          throws ConfigurationException {

    _configurationProvider.saveCompanyConfiguration(
            AICreatorExtensionOpenAICompanyConfiguration.class, companyId,
            HashMapDictionaryBuilder.<String, Object>put(
                    "apiKey", apiKey
            ).put(
                    "enableChatGPTToCreateContent", enableChatGPT
            ).put(
                    "enableDALLEToCreateImages", enableDALLE
            ).build());
  }

  @Override
  public void saveAICreatorOpenAIGroupConfiguration(
          long groupId, String apiKey, boolean enableChatGPT,
          boolean enableDALLE)
          throws ConfigurationException {

    _configurationProvider.saveGroupConfiguration(
            AICreatorExtensionOpenAIGroupConfiguration.class, groupId,
            HashMapDictionaryBuilder.<String, Object>put(
                    "apiKey", apiKey
            ).put(
                    "enableChatGPTToCreateContent", enableChatGPT
            ).put(
                    "enableDALLEToCreateImages", enableDALLE
            ).build());
  }

  @Override
  public String getCompanyChatCPTModel(long companyId) throws ConfigurationException {

    AICreatorExtensionOpenAICompanyConfiguration
            aiCreatorExtensionOpenAICompanyConfiguration =
            _configurationProvider.getCompanyConfiguration(
                    AICreatorExtensionOpenAICompanyConfiguration.class, companyId);

    return aiCreatorExtensionOpenAICompanyConfiguration.chatGPTModel();
  }

  @Override
  public String getGroupChatCPTModel(long companyId, long groupId) throws ConfigurationException {
    AICreatorExtensionOpenAIGroupConfiguration aiCreatorOpenAIGroupConfiguration =
            _configurationProvider.getGroupConfiguration(
                    AICreatorExtensionOpenAIGroupConfiguration.class, groupId);

    return aiCreatorOpenAIGroupConfiguration.chatGPTModel();
  }

  @Override
  public String getCompanyDALLEModel(long companyId) throws ConfigurationException {
    AICreatorExtensionOpenAICompanyConfiguration
            aiCreatorExtensionOpenAICompanyConfiguration =
            _configurationProvider.getCompanyConfiguration(
                    AICreatorExtensionOpenAICompanyConfiguration.class, companyId);

    return aiCreatorExtensionOpenAICompanyConfiguration.dALLEModel();
  }

  @Override
  public String getGroupDALLEModel(long companyId, long groupId) throws ConfigurationException {
    AICreatorExtensionOpenAIGroupConfiguration aiCreatorOpenAIGroupConfiguration =
            _configurationProvider.getGroupConfiguration(
                    AICreatorExtensionOpenAIGroupConfiguration.class, groupId);

    return aiCreatorOpenAIGroupConfiguration.dallEModel();
  }

  @Override
  public void saveAICreatorOpenAICompanyConfiguration(long companyId, String apiKey, boolean enableChatGPT, boolean enableDALLE, String chatGPTModel, String dallEModel) throws ConfigurationException {
    _configurationProvider.saveGroupConfiguration(
            AICreatorExtensionOpenAIGroupConfiguration.class, companyId,
            HashMapDictionaryBuilder.<String, Object>put(
                    "apiKey", apiKey
            ).put(
                    "enableChatGPTToCreateContent", enableChatGPT
            ).put(
                    "enableDALLEToCreateImages", enableDALLE
            ).put(
                    "chatGPTModel", chatGPTModel
            ).put(
                    "dallEModel", dallEModel
            ).build());
  }

  @Override
  public void saveAICreatorOpenAIGroupConfiguration(long groupId, String apiKey, boolean enableChatGPT, boolean enableDALLE, String chatGPTModel, String dallEModel) throws ConfigurationException {
    _configurationProvider.saveGroupConfiguration(
            AICreatorExtensionOpenAIGroupConfiguration.class, groupId,
            HashMapDictionaryBuilder.<String, Object>put(
                    "apiKey", apiKey
            ).put(
                    "enableChatGPTToCreateContent", enableChatGPT
            ).put(
                    "enableDALLEToCreateImages", enableDALLE
            ).put(
                    "chatGPTModel", chatGPTModel
            ).put(
                    "dallEModel", dallEModel
            ).build());
  }
}

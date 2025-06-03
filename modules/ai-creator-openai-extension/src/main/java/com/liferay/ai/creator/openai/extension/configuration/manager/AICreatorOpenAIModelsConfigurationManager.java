package com.liferay.ai.creator.openai.extension.configuration.manager;

import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.ai.creator.openai.configuration.manager.AICreatorOpenAIConfigurationManager;

/**
 * AICreatorOpenAIModelsConfigurationManager is an interface to manage the models that are used for a company or group.
 * 
 * @author dnebing
 */
public interface AICreatorOpenAIModelsConfigurationManager extends AICreatorOpenAIConfigurationManager {

    public String getChatGPTCompanyModel(long companyId) throws ConfigurationException;

    public String getDALLECompanyModel(long companyId) throws ConfigurationException;

    public String getChatGPTGroupModel(long groupId) throws ConfigurationException;

    public String getChatGPTGroupModel(long companyId, long groupId) throws ConfigurationException;

    public String getDALLEGroupModel(long groupId) throws ConfigurationException;
    
    public String getDALLEGroupModel(long companyId, long groupId) throws ConfigurationException;
    
    public void saveAICreatorOpenAICompanyModelsConfiguration(
        long companyId, String apiKey, boolean enableChatGPT,
        boolean enableDALLE, String chatGPTModel, String dallEModel)
    throws ConfigurationException;

    public void saveAICreatorOpenAIGroupModelsConfiguration(
        long companyId, long groupId, String apiKey, boolean enableChatGPT,
        boolean enableDALLE, String chatGPTModel, String dallEModel)
    throws ConfigurationException;


}

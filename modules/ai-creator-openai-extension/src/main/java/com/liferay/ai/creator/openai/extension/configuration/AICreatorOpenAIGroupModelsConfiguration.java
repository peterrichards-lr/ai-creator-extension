package com.liferay.ai.creator.openai.extension.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * AICreatorOpenAIGroupModelsConfiguration is an interface to capture the models that are used for a group.
 * 
 * @author dnebing
 */
@ExtendedObjectClassDefinition(
        category = "ai-creator",
        scope = ExtendedObjectClassDefinition.Scope.GROUP
)
@Meta.OCD(
        id = "com.liferay.ai.creator.openai.extension.configuration.AICreatorOpenAIGroupModelsConfiguration",
        localization = "content/Language",
        name = "ai-creator-openai-group-models-configuration-name"
)
public interface AICreatorOpenAIGroupModelsConfiguration {
    @Meta.AD(deflt = "gpt-3.5-turbo", name = "chat-gpt-model", required = false)
    String chatGPTModel();
  
    @Meta.AD(deflt = "dall-e-2", name = "dall-e-model", required = false)
    String dALLEModel();  
}

package com.liferay.ai.creator.extension.openai.configuration;

import aQute.bnd.annotation.metatype.Meta;
import com.liferay.ai.creator.openai.configuration.AICreatorOpenAICompanyConfiguration;
import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Peter Richards
 */
@ExtendedObjectClassDefinition(
        category = "ai-creator",
        scope = ExtendedObjectClassDefinition.Scope.COMPANY
)
@Meta.OCD(
        id = "com.liferay.ai.creator.extension.openai.configuration.AICreatorExtensionOpenAICompanyConfiguration",
        localization = "content/Language",
        name = "ai-creator-extension-openai-company-configuration-name"
)
public interface AICreatorExtensionOpenAICompanyConfiguration extends AICreatorOpenAICompanyConfiguration {
  @Meta.AD(deflt = "gpt-3.5-turbo", name = "chat-gpt-model", required = false)
  String chatGPTModel();

  @Meta.AD(deflt = "dall-e-2", name = "dall-e-model", required = false)
  String dALLEModel();
}

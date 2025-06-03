package com.liferay.ai.creator.openai.extension.configuration;

import aQute.bnd.annotation.metatype.Meta;
import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

import static com.liferay.ai.creator.openai.extension.configuration.AICreatorOpenAIModelConstants.*;

/**
 * AICreatorOpenAICompanyModelsConfiguration is an interface to capture the models that are used for a company.
 *
 * @author dnebing
 */
@ExtendedObjectClassDefinition(
        category = "ai-creator",
        scope = ExtendedObjectClassDefinition.Scope.COMPANY
)
@Meta.OCD(
        id = "com.liferay.ai.creator.openai.extension.configuration.AICreatorOpenAICompanyModelsConfiguration",
        localization = "content/Language",
        name = "ai-creator-openai-company-models-configuration-name"
)
public interface AICreatorOpenAICompanyModelsConfiguration {
  @Meta.AD(deflt = DEFAULT_CHATGPT_MODEL, name = DEFAULT_CHATGPT_MODEL_KEY, required = false)
  String chatGPTModel();

  @Meta.AD(deflt = DEFAULT_DALL_E_MODEL, name = DEFAULT_DALL_E_MODEL_KEY, required = false)
  String dALLEModel();

}

package com.liferay.ai.creator.openai.extension.configuration;

import aQute.bnd.annotation.metatype.Meta;
import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

import static com.liferay.ai.creator.openai.extension.configuration.AICreatorOpenAIModelConstants.*;

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
  @Meta.AD(deflt = DEFAULT_CHATGPT_MODEL, name = DEFAULT_CHATGPT_MODEL_KEY, required = false)
  String chatGPTModel();

  @Meta.AD(deflt = DEFAULT_DALL_E_MODEL, name = DEFAULT_DALL_E_MODEL_KEY, required = false)
  String dALLEModel();
}

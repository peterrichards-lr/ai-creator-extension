package com.liferay.ai.creator.extension.openai.web.client;

import com.liferay.ai.creator.openai.web.internal.client.AICreatorOpenAIClient;

import java.util.Locale;

/**
 * @author Peter Richards
 */
public interface AICreatorExtensionOpenAIClient extends AICreatorOpenAIClient {
  String getCompletion(
          String apiKey, String content, Locale locale, String tone,
          int words, String model)
          throws Exception;

  String[] getGenerations(
          String apiKey, String prompt, String size, int numberOfImages, String model)
          throws Exception;

  void validateAPIKey(String apiKey, String chatGPTModel) throws Exception;
}

package com.liferay.ai.creator.extension.openai.web.client;

import com.liferay.ai.creator.openai.web.internal.client.AICreatorOpenAIClient;
import com.liferay.ai.creator.openai.web.internal.exception.AICreatorOpenAIClientException;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.StringUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Locale;

/**
 * @author Lourdes Fernández Besada
 * @author Roberto Díaz
 * @author Peter Richards
 */
@Component(
        property = "service.ranking:Integer=110",
        service = AICreatorOpenAIClient.class
)
public class AICreatorExtensionOpenAIClientImpl implements AICreatorExtensionOpenAIClient {
  protected static final String DEFAULT_CHATGPT_MODEL = "gpt-3.5-turbo";
  protected static final String DEFAULT_DALL_E_MODEL = "dall-e-2";

  protected static final String ENDPOINT_COMPLETION =
          "https://api.openai.com/v1/chat/completions";
  protected static final String ENDPOINT_GENERATIONS =
          "https://api.openai.com/v1/images/generations";
  protected static final String ENDPOINT_VALIDATION_PREFIX =
          "https://api.openai.com/v1/models/";
  private static final Log _log = LogFactoryUtil.getLog(
          AICreatorExtensionOpenAIClientImpl.class);
  @Reference
  private Http _http;
  @Reference
  private JSONFactory _jsonFactory;
  @Reference
  private Language _language;

  @Override
  public String getCompletion(
          String apiKey, String content, Locale locale, String tone,
          int words)
          throws Exception {
    return getCompletion(apiKey, content, locale, tone, words, DEFAULT_CHATGPT_MODEL);
  }

  @Override
  public String getCompletion(
          String apiKey, String content, Locale locale, String tone,
          int words, String model)
          throws Exception {

    Http.Options options = new Http.Options();

    if (_log.isInfoEnabled()) {
      _log.info(
              StringBundler.concat(
                      "Model: ", model));
    }

    options.addHeader("Authorization", "Bearer " + apiKey);
    options.addHeader("Content-Type", ContentTypes.APPLICATION_JSON);
    options.setLocation(ENDPOINT_COMPLETION);
    options.setBody(
            JSONUtil.put(
                    "messages",
                    JSONUtil.putAll(
                            JSONUtil.put(
                                    "content",
                                    _language.format(
                                            locale,
                                            "i-want-you-to-create-a-text-of-approximately-x-" +
                                                    "words,-and-using-a-x-tone",
                                            new String[]{String.valueOf(words), tone})
                            ).put(
                                    "role", "system"
                            ),
                            JSONUtil.put(
                                    "content", content
                            ).put(
                                    "role", "user"
                            ))
            ).put(
                    "model", model
            ).toString(),
            ContentTypes.APPLICATION_JSON, StringPool.UTF8);
    options.setPost(true);

    JSONObject responseJSONObject = _getResponseJSONObject(options);

    JSONArray jsonArray = responseJSONObject.getJSONArray("choices");

    if (JSONUtil.isEmpty(jsonArray)) {
      return StringPool.BLANK;
    }

    JSONObject choiceJSONObject = jsonArray.getJSONObject(0);

    JSONObject messageJSONObject = choiceJSONObject.getJSONObject(
            "message");

    return messageJSONObject.getString("content");
  }

  @Override
  public String[] getGenerations(
          String apiKey, String prompt, String size, int numberOfImages)
          throws Exception {

    if (_log.isInfoEnabled()) {
      _log.info(StringBundler.concat("Default method called", " API Key: ", apiKey));

      Thread.dumpStack();
    }

    return getGenerations(apiKey, prompt, size, numberOfImages, DEFAULT_DALL_E_MODEL);
  }

  @Override
  public String[] getGenerations(
          String apiKey, String prompt, String size, int numberOfImages, String model)
          throws Exception {

    String[] urls = new String[0];

    if (_log.isInfoEnabled()) {
      _log.info(
              StringBundler.concat(
                      "Model: ", model));
    }

    Http.Options options = new Http.Options();

    options.addHeader("Authorization", "Bearer " + apiKey);
    options.addHeader("Content-Type", ContentTypes.APPLICATION_JSON);
    options.setLocation(ENDPOINT_GENERATIONS);
    options.setBody(
            JSONUtil.put(
                    "model", model
            ).put(
                    "n", numberOfImages
            ).put(
                    "prompt", prompt
            ).put(
                    "size", size
            ).toString(),
            ContentTypes.APPLICATION_JSON, StringPool.UTF8);
    options.setPost(true);

    JSONObject responseJSONObject = _getResponseJSONObject(options);

    JSONArray dataJSONArray = responseJSONObject.getJSONArray("data");

    if (!JSONUtil.isEmpty(dataJSONArray)) {
      for (int i = 0; i < dataJSONArray.length(); i++) {
        JSONObject dataJSONObject = dataJSONArray.getJSONObject(i);

        urls = ArrayUtil.append(urls, dataJSONObject.getString("url"));
      }
    }

    return urls;
  }

  private JSONObject _getResponseJSONObject(Http.Options options)
          throws Exception {

    try (InputStream inputStream = _http.URLtoInputStream(options)) {
      Http.Response response = options.getResponse();

      JSONObject responseJSONObject = _jsonFactory.createJSONObject(
              StringUtil.read(inputStream));

      if (responseJSONObject.has("error")) {
        JSONObject errorJSONObject = responseJSONObject.getJSONObject(
                "error");

        if (_log.isDebugEnabled()) {
          _log.debug(
                  StringBundler.concat(
                          "Endpoint: ", options.getLocation(),
                          ", OpenAI API error: ", errorJSONObject));
        }

        throw new AICreatorOpenAIClientException(
                errorJSONObject.getString("code"),
                errorJSONObject.getString("message"),
                response.getResponseCode());
      } else if (response.getResponseCode() != HttpURLConnection.HTTP_OK) {
        if (_log.isDebugEnabled()) {
          _log.debug(
                  StringBundler.concat(
                          "Endpoint: ", options.getLocation(),
                          ", OpenAI API response code: ",
                          response.getResponseCode()));
        }

        throw new AICreatorOpenAIClientException(
                response.getResponseCode());
      }

      return responseJSONObject;
    } catch (Exception exception) {
      if (_log.isDebugEnabled()) {
        _log.debug(exception);
      }

      if (exception instanceof AICreatorOpenAIClientException) {
        throw exception;
      }

      throw new AICreatorOpenAIClientException(exception);
    }
  }

  @Override
  public void validateAPIKey(String apiKey) throws Exception {
    validateAPIKey(apiKey, DEFAULT_CHATGPT_MODEL);
  }

  @Override
  public void validateAPIKey(String apiKey, String chatGPTModel) throws Exception {
    Http.Options options = new Http.Options();

    options.addHeader("Authorization", "Bearer " + apiKey);
    options.setLocation(ENDPOINT_VALIDATION_PREFIX + chatGPTModel);

    _getResponseJSONObject(options);
  }
}

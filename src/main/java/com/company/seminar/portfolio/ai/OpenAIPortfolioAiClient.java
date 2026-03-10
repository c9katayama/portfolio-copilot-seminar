package com.company.seminar.portfolio.ai;

import com.company.seminar.portfolio.config.OpenAiProperties;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.ai.provider", havingValue = "openai", matchIfMissing = true)
public class OpenAIPortfolioAiClient implements PortfolioAiClient {

  private final OpenAiProperties properties;
  private final ObjectMapper objectMapper;
  private final HttpClient httpClient =
      HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(15)).build();

  @Override
  public String generateDiscussionReply(String prompt) {
    return callOpenAi(prompt);
  }

  @Override
  public String generateProposalDraft(String prompt) {
    return callOpenAi(prompt);
  }

  private String callOpenAi(String prompt) {
    validateConfiguration();

    OpenAiRequest requestBody =
        new OpenAiRequest(
            properties.getModel(),
            properties.getMaxTokens(),
            List.of(new OpenAiMessage("user", prompt)));

    try {
      String requestJson = objectMapper.writeValueAsString(requestBody);
      String url = properties.getBaseUrl() + properties.getChatCompletionsPath();
      HttpRequest request =
          HttpRequest.newBuilder()
              .uri(URI.create(url))
              .timeout(Duration.ofSeconds(60))
              .header("Content-Type", "application/json")
              .header("Authorization", "Bearer " + properties.getApiKey())
              .POST(HttpRequest.BodyPublishers.ofString(requestJson, StandardCharsets.UTF_8))
              .build();

      HttpResponse<String> response =
          httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
      if (response.statusCode() >= 400) {
        log.error(
            "OpenAI API request failed with status {} and body {}",
            response.statusCode(),
            response.body());
        throw new AiIntegrationException(
            "OpenAI API request failed with status " + response.statusCode());
      }

      OpenAiResponse openAiResponse = objectMapper.readValue(response.body(), OpenAiResponse.class);
      if (openAiResponse.choices() == null || openAiResponse.choices().isEmpty()) {
        log.error("OpenAI API returned empty choices: {}", response.body());
        throw new AiIntegrationException("OpenAI API returned empty choices");
      }

      String content = openAiResponse.choices().get(0).message().content();
      if (!StringUtils.hasText(content)) {
        log.error("OpenAI API returned no text content: {}", response.body());
        throw new AiIntegrationException("OpenAI API returned no text content");
      }
      return content;
    } catch (IOException exception) {
      log.error("Failed to parse OpenAI API response", exception);
      throw new AiIntegrationException("Failed to parse OpenAI API response", exception);
    } catch (InterruptedException exception) {
      Thread.currentThread().interrupt();
      log.error("OpenAI API request was interrupted", exception);
      throw new AiIntegrationException("OpenAI API request was interrupted", exception);
    }
  }

  private void validateConfiguration() {
    if (!StringUtils.hasText(properties.getApiKey())) {
      throw new IllegalStateException("OPENAI_API_KEY is not configured");
    }
    if (!StringUtils.hasText(properties.getBaseUrl())
        || !StringUtils.hasText(properties.getChatCompletionsPath())
        || !StringUtils.hasText(properties.getModel())) {
      throw new IllegalStateException("OpenAI AI configuration is incomplete");
    }
  }

  private record OpenAiRequest(
      String model, @JsonProperty("max_tokens") Integer maxTokens, List<OpenAiMessage> messages) {}

  private record OpenAiMessage(String role, String content) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  private record OpenAiResponse(List<OpenAiChoice> choices) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  private record OpenAiChoice(OpenAiMessageResponse message) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  private record OpenAiMessageResponse(String content) {}
}

package com.company.seminar.portfolio.ai;

import com.company.seminar.portfolio.config.AnthropicProperties;
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
@ConditionalOnProperty(name = "app.ai.provider", havingValue = "anthropic")
public class AnthropicPortfolioAiClient implements PortfolioAiClient {

  private static final String ANTHROPIC_VERSION = "2023-06-01";

  private final AnthropicProperties properties;
  private final ObjectMapper objectMapper;
  private final HttpClient httpClient =
      HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(15)).build();

  @Override
  public String generateDiscussionReply(String prompt) {
    return callAnthropic(prompt);
  }

  @Override
  public String generateProposalDraft(String prompt) {
    return callAnthropic(prompt);
  }

  private String callAnthropic(String prompt) {
    validateConfiguration();

    AnthropicRequest requestBody =
        new AnthropicRequest(
            properties.getModel(),
            properties.getMaxTokens(),
            List.of(new AnthropicMessage("user", List.of(new AnthropicContent("text", prompt)))));

    try {
      String requestJson = objectMapper.writeValueAsString(requestBody);
      HttpRequest request =
          HttpRequest.newBuilder()
              .uri(URI.create(properties.getBaseUrl() + properties.getMessagesPath()))
              .timeout(Duration.ofSeconds(60))
              .header("content-type", "application/json")
              .header("x-api-key", properties.getApiKey())
              .header("anthropic-version", ANTHROPIC_VERSION)
              .POST(HttpRequest.BodyPublishers.ofString(requestJson, StandardCharsets.UTF_8))
              .build();

      HttpResponse<String> response =
          httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
      if (response.statusCode() >= 400) {
        log.error(
            "Anthropic API request failed with status {} and body {}",
            response.statusCode(),
            response.body());
        throw new AiIntegrationException(
            "Anthropic API request failed with status " + response.statusCode());
      }

      AnthropicResponse anthropicResponse =
          objectMapper.readValue(response.body(), AnthropicResponse.class);
      if (anthropicResponse.content() == null || anthropicResponse.content().isEmpty()) {
        log.error("Anthropic API returned empty content: {}", response.body());
        throw new AiIntegrationException("Anthropic API returned empty content");
      }

      return anthropicResponse.content().stream()
          .map(AnthropicResponseContent::text)
          .filter(StringUtils::hasText)
          .findFirst()
          .orElseThrow(() -> new AiIntegrationException("Anthropic API returned no text content"));
    } catch (IOException exception) {
      log.error("Failed to parse Anthropic API response", exception);
      throw new AiIntegrationException("Failed to parse Anthropic API response", exception);
    } catch (InterruptedException exception) {
      Thread.currentThread().interrupt();
      log.error("Anthropic API request was interrupted", exception);
      throw new AiIntegrationException("Anthropic API request was interrupted", exception);
    }
  }

  private void validateConfiguration() {
    if (!StringUtils.hasText(properties.getApiKey())) {
      throw new IllegalStateException("ANTHROPIC_API_KEY is not configured");
    }
    if (!StringUtils.hasText(properties.getBaseUrl())
        || !StringUtils.hasText(properties.getMessagesPath())
        || !StringUtils.hasText(properties.getModel())) {
      throw new IllegalStateException("Anthropic AI configuration is incomplete");
    }
  }

  private record AnthropicRequest(
      String model,
      @JsonProperty("max_tokens") Integer maxTokens,
      List<AnthropicMessage> messages) {}

  private record AnthropicMessage(String role, List<AnthropicContent> content) {}

  private record AnthropicContent(String type, String text) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  private record AnthropicResponse(List<AnthropicResponseContent> content) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  private record AnthropicResponseContent(String type, String text) {}
}

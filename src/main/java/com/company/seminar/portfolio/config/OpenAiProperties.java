package com.company.seminar.portfolio.config;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "app.ai.openai")
public class OpenAiProperties {

  private String baseUrl = "https://api.openai.com";

  private String chatCompletionsPath = "/v1/chat/completions";

  private String model = "gpt-4o-mini";

  private String apiKey;

  @Min(1)
  private Integer maxTokens = 1200;
}

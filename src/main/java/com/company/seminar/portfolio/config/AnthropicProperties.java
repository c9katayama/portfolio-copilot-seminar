package com.company.seminar.portfolio.config;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "app.ai.anthropic")
public class AnthropicProperties {

  private String baseUrl;

  private String messagesPath;

  private String model;

  private String apiKey;

  @Min(1)
  private Integer maxTokens;
}

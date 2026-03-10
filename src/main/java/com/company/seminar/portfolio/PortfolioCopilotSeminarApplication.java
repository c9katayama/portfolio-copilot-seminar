package com.company.seminar.portfolio;

import com.company.seminar.portfolio.config.AnthropicProperties;
import com.company.seminar.portfolio.config.OpenAiProperties;
import java.time.Clock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties({OpenAiProperties.class, AnthropicProperties.class})
public class PortfolioCopilotSeminarApplication {

  public static void main(String[] args) {
    SpringApplication.run(PortfolioCopilotSeminarApplication.class, args);
  }

  @Bean
  public Clock clock() {
    return Clock.systemDefaultZone();
  }
}

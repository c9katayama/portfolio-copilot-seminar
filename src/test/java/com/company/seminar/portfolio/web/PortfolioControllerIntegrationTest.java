package com.company.seminar.portfolio.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class PortfolioControllerIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Test
  void portfoliosPageShowsSeededPortfolioData() throws Exception {
    mockMvc
        .perform(get("/portfolios/1"))
        .andExpect(status().isOk())
        .andExpect(content().string(org.hamcrest.Matchers.containsString("Portfolio Copilot")))
        .andExpect(content().string(org.hamcrest.Matchers.containsString("Core Growth 2030")))
        .andExpect(
            content()
                .string(org.hamcrest.Matchers.containsString("Vanguard Total World Stock ETF")));
  }
}

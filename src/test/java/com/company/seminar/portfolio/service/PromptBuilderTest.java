package com.company.seminar.portfolio.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.company.seminar.portfolio.domain.ConversationMessage;
import com.company.seminar.portfolio.domain.Holding;
import com.company.seminar.portfolio.domain.MessageRole;
import com.company.seminar.portfolio.domain.PortfolioSummary;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;

class PromptBuilderTest {

  private final PromptBuilder promptBuilder = new PromptBuilder();

  @Test
  void buildDiscussionPromptIncludesPortfolioContextAndGuardrails() {
    PortfolioSummary portfolio =
        new PortfolioSummary(
            1L,
            "Core Growth 2030",
            "Mass Affluent",
            "長期成長と耐性の両立",
            "Moderate",
            new BigDecimal("18000000"),
            Instant.parse("2026-03-01T10:00:00Z"));
    Holding holding =
        new Holding(
            10L,
            1L,
            "Equity ETF",
            "VT",
            "Vanguard Total World Stock ETF",
            new BigDecimal("42.0"),
            new BigDecimal("7560000"));
    ConversationMessage message =
        new ConversationMessage(
            20L, 1L, MessageRole.ASSISTANT, "既存の議論です。", Instant.parse("2026-03-05T09:00:00Z"));

    String prompt =
        promptBuilder.buildDiscussionPrompt(
            portfolio, List.of(holding), List.of(message), "現状の守りを増やしたいです。");

    assertThat(prompt)
        .contains("Core Growth 2030")
        .contains("Vanguard Total World Stock ETF")
        .contains("既存の議論です。")
        .contains("Do not present your output as regulated investment advice.")
        .contains("現状の守りを増やしたいです。");
  }

  @Test
  void buildProposalPromptIncludesExpectedSections() {
    PortfolioSummary portfolio =
        new PortfolioSummary(
            2L,
            "Income Balance Plus",
            "Retirement Advisory",
            "安定収益の確保",
            "Conservative",
            new BigDecimal("26000000"),
            Instant.parse("2026-03-02T09:30:00Z"));

    String prompt = promptBuilder.buildProposalPrompt(portfolio, List.of(), List.of());

    assertThat(prompt)
        .contains("提案概要")
        .contains("見直しの方向性")
        .contains("No prior discussion")
        .contains("Income Balance Plus");
  }
}

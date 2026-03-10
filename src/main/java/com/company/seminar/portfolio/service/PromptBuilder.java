package com.company.seminar.portfolio.service;

import com.company.seminar.portfolio.domain.ConversationMessage;
import com.company.seminar.portfolio.domain.Holding;
import com.company.seminar.portfolio.domain.PortfolioSummary;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class PromptBuilder {

  public String buildDiscussionPrompt(
      PortfolioSummary portfolio,
      List<Holding> holdings,
      List<ConversationMessage> history,
      String userQuestion) {
    return """
                You are supporting an internal financial solutions team.
                Do not present your output as regulated investment advice.
                Frame the response as an internal discussion draft for an advisor.

                Portfolio Summary
                - Name: %s
                - Client Segment: %s
                - Objective: %s
                - Risk Profile: %s
                - Invested Amount: %s

                Holdings
                %s

                Recent Discussion
                %s

                New User Question
                %s

                Please respond in Japanese with:
                1. current observations
                2. potential rebalance or review angles
                3. risks and points requiring human validation
                """
        .formatted(
            portfolio.name(),
            portfolio.clientSegment(),
            portfolio.objective(),
            portfolio.riskProfile(),
            portfolio.investedAmount().toPlainString(),
            formatHoldings(holdings),
            formatHistory(history),
            userQuestion);
  }

  public String buildProposalPrompt(
      PortfolioSummary portfolio, List<Holding> holdings, List<ConversationMessage> history) {
    return """
                You are drafting an internal proposal support document for a financial solutions team.
                The output must be in Japanese and clearly read like a draft.
                Do not claim final suitability judgement or regulated advice.

                Portfolio Summary
                - Name: %s
                - Client Segment: %s
                - Objective: %s
                - Risk Profile: %s
                - Invested Amount: %s

                Holdings
                %s

                Discussion History
                %s

                Create a structured proposal draft with these sections:
                - 提案概要
                - 現状認識
                - 見直しの方向性
                - 想定メリット
                - 留意点
                - 次回確認事項
                """
        .formatted(
            portfolio.name(),
            portfolio.clientSegment(),
            portfolio.objective(),
            portfolio.riskProfile(),
            portfolio.investedAmount().toPlainString(),
            formatHoldings(holdings),
            formatHistory(history));
  }

  private String formatHoldings(List<Holding> holdings) {
    return holdings.stream()
        .map(
            holding ->
                "- %s / %s / %s / allocation=%s%% / marketValue=%s"
                    .formatted(
                        holding.assetClass(),
                        holding.ticker(),
                        holding.instrumentName(),
                        holding.allocationPercent().toPlainString(),
                        holding.marketValue().toPlainString()))
        .collect(Collectors.joining("\n"));
  }

  private String formatHistory(List<ConversationMessage> history) {
    if (history.isEmpty()) {
      return "- No prior discussion";
    }

    return history.stream()
        .map(message -> "- %s: %s".formatted(message.role().name(), message.content()))
        .collect(Collectors.joining("\n"));
  }
}

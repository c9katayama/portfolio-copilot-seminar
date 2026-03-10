package com.company.seminar.portfolio.service;

import com.company.seminar.portfolio.ai.PortfolioAiClient;
import com.company.seminar.portfolio.domain.Holding;
import com.company.seminar.portfolio.domain.PortfolioSummary;
import com.company.seminar.portfolio.domain.ProposalDraft;
import com.company.seminar.portfolio.repository.ConversationRepository;
import com.company.seminar.portfolio.repository.HoldingRepository;
import com.company.seminar.portfolio.repository.PortfolioRepository;
import com.company.seminar.portfolio.repository.ProposalRepository;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ProposalService {

  private final PortfolioRepository portfolioRepository;
  private final HoldingRepository holdingRepository;
  private final ConversationRepository conversationRepository;
  private final ProposalRepository proposalRepository;
  private final PromptBuilder promptBuilder;
  private final PortfolioAiClient portfolioAiClient;
  private final Clock clock;

  @Transactional
  public ProposalDraft generateProposal(Long portfolioId) {
    PortfolioSummary portfolio =
        portfolioRepository
            .findById(portfolioId)
            .orElseThrow(
                () ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Portfolio not found: " + portfolioId));
    List<Holding> holdings = holdingRepository.findByPortfolioId(portfolioId);
    String prompt =
        promptBuilder.buildProposalPrompt(
            portfolio, holdings, conversationRepository.findByPortfolioId(portfolioId));
    String content = portfolioAiClient.generateProposalDraft(prompt);

    LocalDate currentDate = LocalDate.ofInstant(Instant.now(clock), ZoneId.systemDefault());
    String title = portfolio.name() + " 提案ドラフト " + currentDate.format(DateTimeFormatter.ISO_DATE);

    return proposalRepository.save(portfolioId, title, content, Instant.now(clock));
  }
}

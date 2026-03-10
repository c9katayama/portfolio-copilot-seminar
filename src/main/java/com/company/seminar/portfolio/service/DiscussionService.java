package com.company.seminar.portfolio.service;

import com.company.seminar.portfolio.ai.PortfolioAiClient;
import com.company.seminar.portfolio.domain.ConversationMessage;
import com.company.seminar.portfolio.domain.Holding;
import com.company.seminar.portfolio.domain.MessageRole;
import com.company.seminar.portfolio.domain.PortfolioSummary;
import com.company.seminar.portfolio.repository.ConversationRepository;
import com.company.seminar.portfolio.repository.HoldingRepository;
import com.company.seminar.portfolio.repository.PortfolioRepository;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class DiscussionService {

  private final PortfolioRepository portfolioRepository;
  private final HoldingRepository holdingRepository;
  private final ConversationRepository conversationRepository;
  private final PromptBuilder promptBuilder;
  private final PortfolioAiClient portfolioAiClient;
  private final Clock clock;

  @Transactional
  public List<ConversationMessage> addDiscussion(Long portfolioId, String userMessage) {
    if (!StringUtils.hasText(userMessage)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message must not be empty");
    }

    PortfolioSummary portfolio =
        portfolioRepository
            .findById(portfolioId)
            .orElseThrow(
                () ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Portfolio not found: " + portfolioId));
    List<Holding> holdings = holdingRepository.findByPortfolioId(portfolioId);
    Instant userMessageTime = Instant.now(clock);

    conversationRepository.save(portfolioId, MessageRole.USER, userMessage, userMessageTime);

    List<ConversationMessage> history = conversationRepository.findByPortfolioId(portfolioId);
    String prompt = promptBuilder.buildDiscussionPrompt(portfolio, holdings, history, userMessage);
    String reply = portfolioAiClient.generateDiscussionReply(prompt);
    conversationRepository.save(portfolioId, MessageRole.ASSISTANT, reply, Instant.now(clock));

    return conversationRepository.findByPortfolioId(portfolioId);
  }
}

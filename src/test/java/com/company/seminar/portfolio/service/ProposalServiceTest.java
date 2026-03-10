package com.company.seminar.portfolio.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.company.seminar.portfolio.ai.PortfolioAiClient;
import com.company.seminar.portfolio.domain.ConversationMessage;
import com.company.seminar.portfolio.domain.Holding;
import com.company.seminar.portfolio.domain.MessageRole;
import com.company.seminar.portfolio.domain.PortfolioSummary;
import com.company.seminar.portfolio.domain.ProposalDraft;
import com.company.seminar.portfolio.repository.ConversationRepository;
import com.company.seminar.portfolio.repository.HoldingRepository;
import com.company.seminar.portfolio.repository.PortfolioRepository;
import com.company.seminar.portfolio.repository.ProposalRepository;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProposalServiceTest {

  @Mock private PortfolioRepository portfolioRepository;

  @Mock private HoldingRepository holdingRepository;

  @Mock private ConversationRepository conversationRepository;

  @Mock private ProposalRepository proposalRepository;

  @Mock private PortfolioAiClient portfolioAiClient;

  private final PromptBuilder promptBuilder = new PromptBuilder();

  private ProposalService proposalService;

  @BeforeEach
  void setUp() {
    proposalService =
        new ProposalService(
            portfolioRepository,
            holdingRepository,
            conversationRepository,
            proposalRepository,
            promptBuilder,
            portfolioAiClient,
            Clock.fixed(Instant.parse("2026-03-07T00:00:00Z"), ZoneId.of("Asia/Tokyo")));
  }

  @Test
  void generateProposalBuildsPromptAndPersistsDraft() {
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
            11L,
            1L,
            "Equity ETF",
            "VT",
            "Vanguard Total World Stock ETF",
            new BigDecimal("42.0"),
            new BigDecimal("7560000"));
    ConversationMessage message =
        new ConversationMessage(
            12L, 1L, MessageRole.USER, "リバランス候補を整理したいです。", Instant.parse("2026-03-05T09:00:00Z"));
    ProposalDraft savedDraft =
        new ProposalDraft(
            99L,
            1L,
            "Core Growth 2030 提案ドラフト 2026-03-07",
            "提案ドラフト本文",
            Instant.parse("2026-03-07T00:00:00Z"));

    when(portfolioRepository.findById(1L)).thenReturn(Optional.of(portfolio));
    when(holdingRepository.findByPortfolioId(1L)).thenReturn(List.of(holding));
    when(conversationRepository.findByPortfolioId(1L)).thenReturn(List.of(message));
    when(portfolioAiClient.generateProposalDraft(any())).thenReturn("提案ドラフト本文");
    when(proposalRepository.save(
            eq(1L), eq("Core Growth 2030 提案ドラフト 2026-03-07"), eq("提案ドラフト本文"), any()))
        .thenReturn(savedDraft);

    ProposalDraft result = proposalService.generateProposal(1L);

    ArgumentCaptor<String> promptCaptor = ArgumentCaptor.forClass(String.class);
    verify(portfolioAiClient).generateProposalDraft(promptCaptor.capture());
    assertThat(promptCaptor.getValue())
        .contains("Core Growth 2030")
        .contains("見直しの方向性")
        .contains("Vanguard Total World Stock ETF");
    assertThat(result).isEqualTo(savedDraft);
  }
}

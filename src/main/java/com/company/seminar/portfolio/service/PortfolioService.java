package com.company.seminar.portfolio.service;

import com.company.seminar.portfolio.domain.PortfolioSummary;
import com.company.seminar.portfolio.repository.ConversationRepository;
import com.company.seminar.portfolio.repository.HoldingRepository;
import com.company.seminar.portfolio.repository.PortfolioRepository;
import com.company.seminar.portfolio.repository.ProposalRepository;
import com.company.seminar.portfolio.view.PortfolioDetailView;
import com.company.seminar.portfolio.view.PortfolioPageView;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PortfolioService {

  private final PortfolioRepository portfolioRepository;
  private final HoldingRepository holdingRepository;
  private final ConversationRepository conversationRepository;
  private final ProposalRepository proposalRepository;

  public PortfolioPageView loadPage(Long selectedPortfolioId) {
    List<PortfolioSummary> portfolios = portfolioRepository.findAll();
    if (portfolios.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No portfolios are configured");
    }

    Long resolvedPortfolioId =
        selectedPortfolioId != null ? selectedPortfolioId : portfolios.getFirst().id();

    return new PortfolioPageView(portfolios, getDetail(resolvedPortfolioId));
  }

  public PortfolioDetailView getDetail(Long portfolioId) {
    PortfolioSummary portfolio =
        portfolioRepository
            .findById(portfolioId)
            .orElseThrow(
                () ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Portfolio not found: " + portfolioId));

    return new PortfolioDetailView(
        portfolio,
        holdingRepository.findByPortfolioId(portfolioId),
        conversationRepository.findByPortfolioId(portfolioId),
        proposalRepository.findLatestByPortfolioId(portfolioId).orElse(null));
  }
}

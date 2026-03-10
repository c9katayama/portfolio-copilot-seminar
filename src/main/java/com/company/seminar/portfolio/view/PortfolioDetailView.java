package com.company.seminar.portfolio.view;

import com.company.seminar.portfolio.domain.ConversationMessage;
import com.company.seminar.portfolio.domain.Holding;
import com.company.seminar.portfolio.domain.PortfolioSummary;
import com.company.seminar.portfolio.domain.ProposalDraft;
import java.util.List;

public record PortfolioDetailView(
    PortfolioSummary portfolio,
    List<Holding> holdings,
    List<ConversationMessage> messages,
    ProposalDraft latestProposal) {}

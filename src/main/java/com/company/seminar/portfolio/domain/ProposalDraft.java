package com.company.seminar.portfolio.domain;

import java.time.Instant;

public record ProposalDraft(
    Long id, Long portfolioId, String title, String content, Instant createdAt) {}

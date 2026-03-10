package com.company.seminar.portfolio.domain;

import java.math.BigDecimal;
import java.time.Instant;

public record PortfolioSummary(
    Long id,
    String name,
    String clientSegment,
    String objective,
    String riskProfile,
    BigDecimal investedAmount,
    Instant updatedAt) {}

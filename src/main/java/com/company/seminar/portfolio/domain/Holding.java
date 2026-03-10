package com.company.seminar.portfolio.domain;

import java.math.BigDecimal;

public record Holding(
    Long id,
    Long portfolioId,
    String assetClass,
    String ticker,
    String instrumentName,
    BigDecimal allocationPercent,
    BigDecimal marketValue) {}

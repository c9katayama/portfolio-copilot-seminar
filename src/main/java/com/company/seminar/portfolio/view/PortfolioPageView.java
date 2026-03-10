package com.company.seminar.portfolio.view;

import com.company.seminar.portfolio.domain.PortfolioSummary;
import java.util.List;

public record PortfolioPageView(
    List<PortfolioSummary> portfolios, PortfolioDetailView selectedDetail) {}

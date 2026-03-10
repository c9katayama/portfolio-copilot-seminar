package com.company.seminar.portfolio.web;

import com.company.seminar.portfolio.service.PortfolioService;
import com.company.seminar.portfolio.view.PortfolioPageView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class PortfolioController {

  private final PortfolioService portfolioService;

  @GetMapping("/")
  public String root() {
    return "redirect:/portfolios";
  }

  @GetMapping("/portfolios")
  public String portfolios(Model model) {
    return renderPage(model, portfolioService.loadPage(null));
  }

  @GetMapping("/portfolios/{portfolioId}")
  public String portfolioDetail(@PathVariable Long portfolioId, Model model) {
    return renderPage(model, portfolioService.loadPage(portfolioId));
  }

  private String renderPage(Model model, PortfolioPageView pageView) {
    model.addAttribute("pageView", pageView);
    model.addAttribute("selectedPortfolio", pageView.selectedDetail().portfolio());
    model.addAttribute("holdings", pageView.selectedDetail().holdings());
    model.addAttribute("messages", pageView.selectedDetail().messages());
    model.addAttribute("latestProposal", pageView.selectedDetail().latestProposal());
    return "portfolios";
  }
}

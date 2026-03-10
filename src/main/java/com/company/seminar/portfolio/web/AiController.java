package com.company.seminar.portfolio.web;

import com.company.seminar.portfolio.service.DiscussionService;
import com.company.seminar.portfolio.service.ProposalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/portfolios")
@RequiredArgsConstructor
public class AiController {

  private final DiscussionService discussionService;
  private final ProposalService proposalService;

  @PostMapping("/{portfolioId}/chat")
  @ResponseStatus(HttpStatus.OK)
  public OperationResponse chat(
      @PathVariable Long portfolioId, @Valid @RequestBody ChatRequest request) {
    discussionService.addDiscussion(portfolioId, request.message());
    return new OperationResponse("ok", "discussion updated");
  }

  @PostMapping("/{portfolioId}/proposal")
  @ResponseStatus(HttpStatus.OK)
  public OperationResponse proposal(@PathVariable Long portfolioId) {
    proposalService.generateProposal(portfolioId);
    return new OperationResponse("ok", "proposal generated");
  }
}

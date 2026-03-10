package com.company.seminar.portfolio.repository;

import com.company.seminar.portfolio.domain.ProposalDraft;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProposalRepository {

  private final JdbcClient jdbcClient;

  public Optional<ProposalDraft> findLatestByPortfolioId(Long portfolioId) {
    return jdbcClient
        .sql(
            """
                        select id, portfolio_id, title, content, created_at
                        from proposal_drafts
                        where portfolio_id = :portfolioId
                        order by created_at desc, id desc
                        limit 1
                        """)
        .param("portfolioId", portfolioId)
        .query(this::mapProposal)
        .optional();
  }

  public ProposalDraft save(Long portfolioId, String title, String content, Instant createdAt) {
    jdbcClient
        .sql(
            """
                        insert into proposal_drafts (portfolio_id, title, content, created_at)
                        values (:portfolioId, :title, :content, :createdAt)
                        """)
        .param("portfolioId", portfolioId)
        .param("title", title)
        .param("content", content)
        .param("createdAt", createdAt.toString())
        .update();

    return findLatestByPortfolioId(portfolioId)
        .orElseThrow(() -> new IllegalStateException("Saved proposal could not be reloaded"));
  }

  private ProposalDraft mapProposal(ResultSet resultSet, int rowNum) throws SQLException {
    return new ProposalDraft(
        resultSet.getLong("id"),
        resultSet.getLong("portfolio_id"),
        resultSet.getString("title"),
        resultSet.getString("content"),
        Instant.parse(resultSet.getString("created_at")));
  }
}

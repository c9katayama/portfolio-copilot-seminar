package com.company.seminar.portfolio.repository;

import com.company.seminar.portfolio.domain.ConversationMessage;
import com.company.seminar.portfolio.domain.MessageRole;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ConversationRepository {

  private final JdbcClient jdbcClient;

  public List<ConversationMessage> findByPortfolioId(Long portfolioId) {
    return jdbcClient
        .sql(
            """
                        select id, portfolio_id, role, content, created_at
                        from conversation_messages
                        where portfolio_id = :portfolioId
                        order by created_at, id
                        """)
        .param("portfolioId", portfolioId)
        .query(this::mapMessage)
        .list();
  }

  public void save(Long portfolioId, MessageRole role, String content, Instant createdAt) {
    jdbcClient
        .sql(
            """
                        insert into conversation_messages (portfolio_id, role, content, created_at)
                        values (:portfolioId, :role, :content, :createdAt)
                        """)
        .param("portfolioId", portfolioId)
        .param("role", role.name())
        .param("content", content)
        .param("createdAt", createdAt.toString())
        .update();
  }

  private ConversationMessage mapMessage(ResultSet resultSet, int rowNum) throws SQLException {
    return new ConversationMessage(
        resultSet.getLong("id"),
        resultSet.getLong("portfolio_id"),
        MessageRole.valueOf(resultSet.getString("role")),
        resultSet.getString("content"),
        Instant.parse(resultSet.getString("created_at")));
  }
}

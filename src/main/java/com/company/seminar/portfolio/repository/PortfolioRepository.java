package com.company.seminar.portfolio.repository;

import com.company.seminar.portfolio.domain.PortfolioSummary;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PortfolioRepository {

  private final JdbcClient jdbcClient;

  public List<PortfolioSummary> findAll() {
    return jdbcClient
        .sql(
            """
                        select id, name, client_segment, objective, risk_profile, invested_amount, updated_at
                        from portfolios
                        order by id
                        """)
        .query(this::mapPortfolio)
        .list();
  }

  public Optional<PortfolioSummary> findById(Long portfolioId) {
    return jdbcClient
        .sql(
            """
                        select id, name, client_segment, objective, risk_profile, invested_amount, updated_at
                        from portfolios
                        where id = :portfolioId
                        """)
        .param("portfolioId", portfolioId)
        .query(this::mapPortfolio)
        .optional();
  }

  public Optional<Long> findFirstPortfolioId() {
    return jdbcClient
        .sql("select id from portfolios order by id limit 1")
        .query(Long.class)
        .optional();
  }

  private PortfolioSummary mapPortfolio(ResultSet resultSet, int rowNum) throws SQLException {
    return new PortfolioSummary(
        resultSet.getLong("id"),
        resultSet.getString("name"),
        resultSet.getString("client_segment"),
        resultSet.getString("objective"),
        resultSet.getString("risk_profile"),
        new BigDecimal(resultSet.getString("invested_amount")),
        Instant.parse(resultSet.getString("updated_at")));
  }
}

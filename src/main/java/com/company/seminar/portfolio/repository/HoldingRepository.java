package com.company.seminar.portfolio.repository;

import com.company.seminar.portfolio.domain.Holding;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class HoldingRepository {

  private final JdbcClient jdbcClient;

  public List<Holding> findByPortfolioId(Long portfolioId) {
    return jdbcClient
        .sql(
            """
                        select id, portfolio_id, asset_class, ticker, instrument_name, allocation_percent, market_value
                        from holdings
                        where portfolio_id = :portfolioId
                        order by id
                        """)
        .param("portfolioId", portfolioId)
        .query(this::mapHolding)
        .list();
  }

  private Holding mapHolding(ResultSet resultSet, int rowNum) throws SQLException {
    return new Holding(
        resultSet.getLong("id"),
        resultSet.getLong("portfolio_id"),
        resultSet.getString("asset_class"),
        resultSet.getString("ticker"),
        resultSet.getString("instrument_name"),
        new BigDecimal(resultSet.getString("allocation_percent")),
        new BigDecimal(resultSet.getString("market_value")));
  }
}

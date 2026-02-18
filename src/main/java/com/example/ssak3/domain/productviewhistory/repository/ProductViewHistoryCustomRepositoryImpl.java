package com.example.ssak3.domain.productviewhistory.repository;

import com.example.ssak3.domain.productviewhistory.entity.ProductViewHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductViewHistoryCustomRepositoryImpl implements ProductViewHistoryCustomRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void saveAll(List<ProductViewHistory> histories) {
        String sql = "INSERT INTO product_view_histories (product_id, view_date, view_count, created_at, updated_at)" +
                     " VALUES (?, ?, ?, NOW(), NOW())" +
                     " ON DUPLICATE KEY UPDATE" +
                     " view_count = VALUES(view_count), " +
                     " updated_at = NOW()";

        jdbcTemplate.batchUpdate(sql,
                histories,
                1000,
                (PreparedStatement ps, ProductViewHistory history) -> {
                    ps.setLong(1, history.getProduct().getId());
                    ps.setDate(2, Date.valueOf(history.getViewDate()));
                    ps.setLong(3, history.getViewCount());
        });
    }
}

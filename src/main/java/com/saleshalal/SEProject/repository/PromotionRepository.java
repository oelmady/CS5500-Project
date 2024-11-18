package com.saleshalal.SEProject.repository;


import com.saleshalal.SEProject.model.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PromotionRepository
        extends JpaRepository<Promotion, Long> {
    List<Promotion> findByExpiryDateAfter(LocalDateTime now);

    List<Promotion> findByNameContaining(String name);

    List<Promotion> findByPriceBetween(BigDecimal min, BigDecimal max);
}
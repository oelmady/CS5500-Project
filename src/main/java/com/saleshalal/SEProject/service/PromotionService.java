package com.saleshalal.SEProject.service;

import com.saleshalal.SEProject.model.Promotion;
import com.saleshalal.SEProject.model.Vendor;
import com.saleshalal.SEProject.repository.PromotionRepository;
import jakarta.transaction.Transactional;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PromotionService {

    private final PromotionRepository promotionRepository;

    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    public List<Promotion> getActivePromotions() {
        return promotionRepository.findByExpiryDateAfter(LocalDateTime.now());
    }

    @Transactional
    public Promotion createPromotion(Vendor vendor, Promotion promotion) {
        promotion.setVendor(vendor);
        return promotionRepository.save(promotion);
    }

    @Transactional
    public Promotion updatePromotion(Long promotionId, Promotion updatedPromotion) {
        Promotion existingPromotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new ResourceNotFoundException("Promotion not found"));

        existingPromotion.setName(updatedPromotion.getName());
        existingPromotion.setDescription(updatedPromotion.getDescription());
        existingPromotion.setPrice(updatedPromotion.getPrice());
        existingPromotion.setAvailableQuantity(updatedPromotion.getAvailableQuantity());
        existingPromotion.setExpiryDate(updatedPromotion.getExpiryDate());

        return promotionRepository.save(existingPromotion);
    }
}


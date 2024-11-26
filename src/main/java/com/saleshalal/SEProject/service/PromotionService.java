package com.saleshalal.SEProject.service;

import com.saleshalal.SEProject.config.CustomLogger;
import com.saleshalal.SEProject.model.Promotion;
import com.saleshalal.SEProject.model.Vendor;
import com.saleshalal.SEProject.repository.PromotionRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.hibernate.validator.internal.util.logging.Log_$logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PromotionService {

    private final CustomLogger logger = new CustomLogger(PromotionService.class);
    private PromotionRepository promotionRepository;

    @Autowired
    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    public PromotionService() {}

    @Autowired
    public void setPromotionRepository(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    /**
     * Get all active promotions by all vendors.
     *
     * @return a list of all active promotions.
     */
    public List<Promotion> getActivePromotions() {
        return promotionRepository.findByExpiryDateAfter(LocalDateTime.now());
    }

    /**
     * Get promotion for a vendor by vendor ID.
     *
     * @param vendorId the ID of the vendor to get promotions for
     * @return a list of promotions for the vendor
     */
    public List<Promotion> getPromotionsByVendorId(Long vendorId) {
        return promotionRepository.findByVendorId(vendorId);
    }

    /**
     * Get promotion by ID
     *
     * @param promotionId the ID of the promotion to retrieve
     * @return the promotion with the given ID
     */
    public Promotion getPromotionById(Long promotionId) {
        return promotionRepository.findById(promotionId)
                .orElseThrow(() -> new ResourceNotFoundException("Promotion not found"));
    }


    /**
     * Create a new promotion for a vendor.
     *
     * @param vendor    the vendor to create the promotion for
     * @param promotion the promotion to create
     * @return the created promotion
     */
    @Transactional
    public Promotion createPromotion(Vendor vendor, @Valid Promotion promotion) {
        promotion.setVendor(vendor);
        return promotionRepository.save(promotion);
    }

    /**
     * Save a promotion to the database without returning anything.
     *
     * @param promotion the promotion to save
     */
    @Transactional
    public void savePromotion(@Valid Promotion promotion) {
        promotionRepository.save(promotion);
    }

    /**
     * Delete a promotion.
     *
     * @param promotionId the ID of the promotion to delete
     */
    @Transactional
    public void deletePromotion(Long promotionId) {
        promotionRepository.deleteById(promotionId);
    }

    /**
     * Update a promotion.
     *
     * @param promotionId      the ID of the promotion to update
     * @param updatedPromotion the updated promotion
     * @return the updated promotion
     */
    @Transactional
    public Promotion updatePromotion(Long promotionId, @Valid Promotion updatedPromotion) {
        try {
            Promotion existingPromotion = promotionRepository.findById(promotionId)
                    .orElseThrow(() -> new ResourceNotFoundException("Promotion not found"));

            existingPromotion.setName(updatedPromotion.getName());
            existingPromotion.setDescription(updatedPromotion.getDescription());
            existingPromotion.setPrice(updatedPromotion.getPrice());
            existingPromotion.setAvailableQuantity(updatedPromotion.getAvailableQuantity());
            existingPromotion.setExpiryDate(updatedPromotion.getExpiryDate());

            return promotionRepository.save(existingPromotion);
        } catch (Exception e) {
            throw new RuntimeException("Error updating promotion", e);
        }
    }

}


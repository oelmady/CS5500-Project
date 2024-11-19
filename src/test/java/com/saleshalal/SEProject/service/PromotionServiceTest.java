package com.saleshalal.SEProject.service;

import com.saleshalal.SEProject.model.Promotion;
import com.saleshalal.SEProject.model.Vendor;
import com.saleshalal.SEProject.repository.PromotionRepository;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PromotionServiceTest {

    @Mock
    private PromotionRepository promotionRepository;

    @InjectMocks
    private PromotionService promotionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests that the getActivePromotions method returns the active promotions.
     */
    @Test
    void getActivePromotions_ReturnsActivePromotions() {
        Promotion promotion = new Promotion();
        promotion.setExpiryDate(LocalDateTime.now().plusDays(1));
        when(promotionRepository.findByExpiryDateAfter(any(LocalDateTime.class))).thenReturn(Collections.singletonList(promotion));

        List<Promotion> result = promotionService.getActivePromotions();

        assertEquals(1, result.size());
        assertEquals(promotion, result.get(0));
    }

    /**
     * Tests that the getPromotionsByVendorId method returns the promotions for the given vendor ID.
     */
    @Test
    void getPromotionsByVendorId_ReturnsPromotions() {
        Promotion promotion = new Promotion();
        when(promotionRepository.findByVendorId(1L)).thenReturn(Collections.singletonList(promotion));

        List<Promotion> result = promotionService.getPromotionsByVendorId(1L);

        assertEquals(1, result.size());
        assertEquals(promotion, result.get(0));
    }

    /**
     * Tests that the getPromotionById method returns the promotion with the given ID.
     */
    @Test
    void getPromotionById_PromotionExists_ReturnsPromotion() {
        Promotion promotion = new Promotion();
        when(promotionRepository.findById(1L)).thenReturn(Optional.of(promotion));

        Promotion result = promotionService.getPromotionById(1L);

        assertEquals(promotion, result);
    }

    /**
     * Tests that the getPromotionById method throws a ResourceNotFoundException
     */
    @Test
    void getPromotionById_PromotionDoesNotExist_ThrowsException() {
        when(promotionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> promotionService.getPromotionById(1L));
    }

    /**
     * Tests that the createPromotion method creates a new promotion for a vendor.
     */
    @Test
    void createPromotion_ValidPromotion_ReturnsCreatedPromotion() {
        Vendor vendor = new Vendor();
        Promotion promotion = new Promotion();
        when(promotionRepository.save(promotion)).thenReturn(promotion);

        Promotion result = promotionService.createPromotion(vendor, promotion);

        assertEquals(promotion, result);
        assertEquals(vendor, promotion.getVendor());
    }

    /**
     * Tests that the savePromotion method saves the promotion to the database.
     */
    @Test
    void savePromotion_ValidPromotion_SavesPromotion() {
        Promotion promotion = new Promotion();
        promotionService.savePromotion(promotion);

        verify(promotionRepository, times(1)).save(promotion);
    }

    /**
     * Tests that the deletePromotion method deletes the promotion with the given ID.
     */
    @Test
    void deletePromotion_PromotionExists_DeletesPromotion() {
        doNothing().when(promotionRepository).deleteById(1L);

        promotionService.deletePromotion(1L);

        verify(promotionRepository, times(1)).deleteById(1L);
    }

    /**
     * Tests that the updatePromotion method updates the promotion with the new values.
     */
    @Test
    void updatePromotion_PromotionExists_ReturnsUpdatedPromotion() {
        Promotion existingPromotion = new Promotion();
        Promotion updatedPromotion = new Promotion();
        updatedPromotion.setName("Updated Name");
        when(promotionRepository.findById(1L)).thenReturn(Optional.of(existingPromotion));
        when(promotionRepository.save(existingPromotion)).thenReturn(existingPromotion);

        Promotion result = promotionService.updatePromotion(1L, updatedPromotion);

        assertEquals(existingPromotion, result);
        assertEquals("Updated Name", existingPromotion.getName());
    }

    /**
     * Tests that the updatePromotion method throws a RuntimeException
     * when the promotion to be updated does not exist.
     */
    @Test
    void updatePromotion_PromotionDoesNotExist_ThrowsException() {
        Promotion updatedPromotion = new Promotion();
        when(promotionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> promotionService.updatePromotion(1L,
                updatedPromotion));
    }
}
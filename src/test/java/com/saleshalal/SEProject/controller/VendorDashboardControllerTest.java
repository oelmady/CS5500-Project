package com.saleshalal.SEProject.controller;

import com.saleshalal.SEProject.model.Promotion;
import com.saleshalal.SEProject.model.Vendor;
import com.saleshalal.SEProject.repository.PromotionRepository;
import com.saleshalal.SEProject.service.PromotionService;
import com.saleshalal.SEProject.repository.VendorRepository;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VendorDashboardControllerTest {

    @Mock
    private PromotionService promotionService;

    @Mock
    private VendorRepository vendorRepository;

    @Mock
    private Model model;

    @Mock
    private Principal principal;

    @InjectMocks
    private VendorDashboardController vendorDashboardController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests that the vendorDashboard method returns the dashboard view when the vendor exists.
     */
    @Test
    void vendorDashboard_VendorExists_ReturnsDashboardView() {
        // Create a vendor and mock the vendorRepository to return the vendor when findByEmail is called
        Vendor vendor = new Vendor();
        when(vendorRepository.findByEmail(anyString())).thenReturn(Optional.of(vendor));
        when(principal.getName()).thenReturn("vendor@example.com");


        String viewName = vendorDashboardController.vendorDashboard(model, principal);

        // Verify that the view name is correct and that the vendor and promotions were added to the model
        assertEquals("vendor/vendor-dashboard", viewName);
        verify(model, times(1)).addAttribute("vendor", vendor);
        verify(model, times(1)).addAttribute("promotions", vendor.getPromotions());
    }

    /**
     * Tests that the vendorDashboard method throws a ResourceNotFoundException when the vendor does not exist.
     */
    @Test
    void vendorDashboard_VendorDoesNotExist_ThrowsException() {
        // Mock the vendorRepository to return an empty optional when findByEmail is called
        when(vendorRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        // Mock the principal to return a vendor email
        when(principal.getName()).thenReturn("vendor@example.com");

        // Call the vendorDashboard method and verify that it throws a ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class, () -> vendorDashboardController.vendorDashboard(model, principal));
    }

    /**
     * Tests that the createPromotion method saves the promotion to the database and redirects to the dashboard.
     */
    @Test
    void createPromotion_ValidPromotion_RedirectsToDashboard() {
        Vendor vendor = new Vendor();
        String email = "vendor@example.com";
        vendor.setEmail(email);
        Promotion promotion = new Promotion();
        // Mock the vendorRepository to return the vendor when findByEmail is called with the email
        when(vendorRepository.findByEmail(email)).thenReturn(Optional.of(vendor));
        when(principal.getName()).thenReturn(email);

        // Call the createPromotion method
        String viewName = vendorDashboardController.createPromotion(promotion, principal);

        // Verify that the promotion was saved and the view name is correct
        assertEquals("redirect:/vendor-dashboard", viewName);
        verify(promotionService, times(1)).savePromotion(promotion);
    }

    /**
     * Tests that the showCreatePromotionForm method returns the create promotion view.
     */
    @Test
    void createPromotionForm_ReturnsCreatePromotionView() {
        String viewName = vendorDashboardController.showCreatePromotionForm(model);

        // Verify that the view name is correct and that the promotion attribute was added to the model
        assertEquals("vendor/create-promotion", viewName);
        verify(model, times(1)).addAttribute(eq("promotion"), any(Promotion.class));
    }

    /**
     * Tests that the showEditPromotionForm method returns the edit promotion view when the promotion exists.
     */
    @Test
    void showEditPromotionForm_PromotionExists_ReturnsEditPromotionView() {
        Promotion promotion = new Promotion();
        promotion.setVendor(new Vendor());
        // Mock the promotionService to return the promotion
        when(promotionService.getPromotionById(1L)).thenReturn(promotion);

        // Call the showEditPromotionForm method
        String viewName = vendorDashboardController.showEditPromotionForm(1L, model, principal);

        // Verify that the view name is correct and that the promotion attribute was added to the model
        assertEquals("vendor/edit-promotion", viewName);
        verify(model, times(1)).addAttribute("promotion", promotion);
    }

    /**
     * Tests that the showEditPromotionForm method throws a RuntimeException when the promotion does not exist.
     */
    @Test
    void showEditPromotionForm_PromotionDoesNotExist_ThrowsException() {
        Long promotionID = 1L;
        when(promotionService.getPromotionById(promotionID)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> vendorDashboardController.showEditPromotionForm(promotionID, model, principal));
    }

    /**
     * Tests that the showEditPromotionForm method throws a ResourceNotFoundException when the promotion does not belong to the principal.
     */
    @Test
    void showEditPromotionForm_PromotionDoesNotBelongToPrincipal_ThrowsException() {
        Promotion promotion = new Promotion();
        Vendor vendor = new Vendor();
        vendor.setName("otherVendor@example.com");
        promotion.setVendor(vendor);

        // Mock the promotionService to return the promotion
        when(promotionService.getPromotionById(1L)).thenReturn(promotion);
        // Mock the principal to return a different Vendor email
        when(principal.getName()).thenReturn("vendor@example.com");

        // Call the showEditPromotionForm method and verify that it throws a ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class, () -> vendorDashboardController.showEditPromotionForm(1L, model, principal));
    }

    /**
     * Tests that the editPromotion method saves the promotion to the database and redirects to the dashboard.
     */
    @Test
    void editPromotion_ValidPromotion_RedirectsToDashboard() {
        Promotion promotion = new Promotion();
        when(promotionService.getPromotionById(1L)).thenReturn(promotion);

        // Call the editPromotion method
        String viewName = vendorDashboardController.editPromotion(1L, promotion);

        assertEquals("redirect:/vendor-dashboard", viewName);
        verify(promotionService, times(1)).savePromotion(promotion);
    }
}
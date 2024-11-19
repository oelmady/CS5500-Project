package com.saleshalal.SEProject.controller;

import com.saleshalal.SEProject.model.Promotion;
import com.saleshalal.SEProject.model.Vendor;
import com.saleshalal.SEProject.repository.PromotionRepository;
import com.saleshalal.SEProject.repository.VendorRepository;
import com.saleshalal.SEProject.service.PromotionService;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Objects;

/*
Vendor Dashboard:

List all vendor's promotions
Create new promotions
Edit existing promotions
Delete promotions
Security checks to ensure vendors can only modify their own promotions
todo Thymeleaf templates for each view:
vendor/dashboard.html
vendor/create-promotion.html
vendor/edit-promotion.html
 */
@Controller
@RequestMapping("/vendor-dashboard")
public class VendorDashboardController {

    private final PromotionService promotionService;
    private final VendorRepository vendorRepository;

    @Autowired
    public VendorDashboardController(PromotionService promotionService, VendorRepository vendorRepository, PromotionRepository promotionRepository) {
        this.promotionService = promotionService;
        this.vendorRepository = vendorRepository;
    }

    /**
     * Displays the vendor dashboard.
     *
     * @param model     the vendor model to pass data to the view
     * @param principal the currently authenticated user
     * @return the vendor dashboard view
     */
    @GetMapping
    public String vendorDashboard(Model model, Principal principal) {
        Vendor vendor = vendorRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));

        model.addAttribute("vendor", vendor);
        model.addAttribute("promotions", vendor.getPromotions());
        return "vendor/vendor-dashboard";
    }

    /**
     * Displays the create promotion form.
     *
     * @param model the model to pass data to the view
     * @return the create promotion view
     */
    @GetMapping("/create-promotion")
    public String createPromotion(Model model) {
        model.addAttribute("promotion", new Promotion());
        return "vendor/create-promotion";
    }

    /**
     * Handles the create promotion form submission.
     *
     * @param model the model to pass data to the view
     * @return the create promotion view
     */
    @GetMapping("/create-promotion")
    public String showCreatePromotionForm(Model model) {
        model.addAttribute("promotion", new Promotion());
        return "vendor/create-promotion";
    }

    /**
     * Handles the create promotion form submission.
     *
     * @param promotion the promotion to create
     * @param principal the currently authenticated user
     * @return the vendor dashboard view
     */
    @PostMapping("/create-promotion")
    public String createPromotion(@ModelAttribute Promotion promotion, Principal principal) {
        Vendor vendor = vendorRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));
        promotion.setVendor(vendor);

        return "redirect:/vendor-dashboard";
    }

    /**
     * Displays the edit promotion form.
     *
     * @param id    the ID of the promotion to edit
     * @param model the model to pass data to the view
     * @return the edit promotion view
     */
    @GetMapping("/edit-promotion/{id}")
    public String showEditPromotionForm(@PathVariable Long id, Model model, Principal principal) {
        try {
            Promotion promotion = promotionService.getPromotionById(id);
            if (promotion == null || !Objects.equals(
                    promotion.getVendor().getName(),
                    principal.getName())) {
                throw new ResourceNotFoundException("Promotion not found");
            }
            model.addAttribute("promotion", promotion);
            return "vendor/edit-promotion";

        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Promotion not found");
        }
    }

    /**
     * Handles the edit promotion form submission.
     *
     * @param id               the ID of the promotion to update
     * @param promotionDetails the promotion details to update
     * @return the vendor dashboard view
     */
    @PostMapping("/edit-promotion/{id}")
    public String editPromotion(@PathVariable Long id, @ModelAttribute Promotion promotionDetails) {
        Promotion promotion = promotionService.getPromotionById(id);
        promotion.setName(promotionDetails.getName());
        promotion.setDescription(promotionDetails.getDescription());
        promotionService.savePromotion(promotion);
        return "redirect:/vendor-dashboard";
    }
}

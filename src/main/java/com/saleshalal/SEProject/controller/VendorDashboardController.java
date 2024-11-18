package com.saleshalal.SEProject.controller;

import com.saleshalal.SEProject.model.Vendor;
import com.saleshalal.SEProject.repository.VendorRepository;
import com.saleshalal.SEProject.service.PromotionService;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

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

    public VendorDashboardController(PromotionService promotionService, VendorRepository vendorRepository) {
        this.promotionService = promotionService;
        this.vendorRepository = vendorRepository;
    }

    @GetMapping
    public String vendorDashboard(Model model, Principal principal) {
        Vendor vendor = vendorRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));

        model.addAttribute("vendor", vendor);
        model.addAttribute("promotions", vendor.getPromotions());
        return "vendor/vendor-dashboard";
    }

    // @PostMapping("/create-promotion")

}

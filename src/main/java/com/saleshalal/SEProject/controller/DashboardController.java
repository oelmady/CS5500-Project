package com.saleshalal.SEProject.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @GetMapping
    public String redirectToDashboard(@AuthenticationPrincipal UserDetails userDetails) {
        // Get the user's role and redirect accordingly
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_VENDOR"))) {
            return "redirect:/dashboard/vendor";
        }
        return "redirect:/dashboard/customer";
    }

    @GetMapping("/vendor")
    @PreAuthorize("hasRole('VENDOR')")
    public String vendorDashboard(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        // Add vendor-specific dashboard data
        model.addAttribute("name", userDetails.getUsername());
        return "dashboard/vendor";
    }

    @GetMapping("/customer")
    @PreAuthorize("hasRole('CUSTOMER')")
    public String customerDashboard(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        // Add customer-specific dashboard data
        model.addAttribute("name", userDetails.getUsername());
        return "dashboard/customer";
    }
}

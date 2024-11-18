package com.saleshalal.SEProject.controller;

import com.saleshalal.SEProject.model.Customer;
import com.saleshalal.SEProject.model.Promotion;
import com.saleshalal.SEProject.model.ShoppingCart;
import com.saleshalal.SEProject.repository.CustomerRepository;
import com.saleshalal.SEProject.repository.PromotionRepository;
import com.saleshalal.SEProject.service.PromotionService;
import com.saleshalal.SEProject.service.ShoppingCartService;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

/*
Customer Dashboard:

View active promotions
Add promotions to cart
Remove items from cart
View cart details
Calculate cart total

todo create corresponding Thymeleaf templates for each view:

customer/dashboard.html
customer/cart.html
 */
@Controller
@RequestMapping("/customer-dashboard")
public class CustomerDashboardController {

    private final PromotionService promotionService;
    private final ShoppingCartService shoppingCartService;
    private final CustomerRepository customerRepository;

    public CustomerDashboardController(PromotionService promotionService, PromotionRepository promotionRepository, ShoppingCartService shoppingCartService, CustomerRepository customerRepository) {
        this.promotionService = promotionService;
        this.shoppingCartService = shoppingCartService;
        this.customerRepository = customerRepository;
    }

    @GetMapping
    public String customerDashboard(Model model, Principal principal) {
        Customer customer = customerRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        // Get customer's current shopping cart
        ShoppingCart cart = shoppingCartService.getCustomerCart(customer);

        model.addAttribute("customer", customer);
        model.addAttribute("cartItems", cart.getCartItems());
        model.addAttribute("cartTotal", cart.getTotalPrice());
        model.addAttribute("activePromotions", promotionService.getActivePromotions());
        return "customer/customer-dashboard";
    }

}
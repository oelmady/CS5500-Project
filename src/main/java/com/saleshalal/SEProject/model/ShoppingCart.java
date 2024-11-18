package com.saleshalal.SEProject.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

// Shopping Cart Entity
@Entity
@Table(name = "shopping_carts")
public class ShoppingCart {

    @OneToMany(mappedBy = "shoppingCart",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private final List<CartItem> cartItems =
            new ArrayList<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shopping_cart_id;
    @OneToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    public ShoppingCart(Customer customer) {
        this.customer = customer;
    }

    public ShoppingCart() {
    }

    // Constructors, getters, and setters
}
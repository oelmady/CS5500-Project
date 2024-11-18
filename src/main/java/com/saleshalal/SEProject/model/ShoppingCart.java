package com.saleshalal.SEProject.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// Shopping Cart Entity
@Getter
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

    /**
     * Adds an item to the shopping cart.
     * Creates a corresponding row in the database.
     *
     * @param item the item to be added
     */
    public void addCartItem(CartItem item) {
        item.setShoppingCart(this);
        this.cartItems.add(item);
    }

    /**
     * Removes an item from the shopping cart.
     * The item is removed from the database.
     *
     * @param item the item to be removed
     */
    public void removeCartItem(CartItem item) {
        this.cartItems.remove(item);
        item.setShoppingCart(null);
    }

    /**
     * Calculates the total price of all items in the shopping cart.
     *
     * @return the total price as a BigDecimal
     */
    public BigDecimal getTotalPrice() {
        return cartItems.stream()
                .map(item -> item.getPromotion().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
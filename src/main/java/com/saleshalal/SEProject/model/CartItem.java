package com.saleshalal.SEProject.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

// Cart Item Entity
@Getter
@Setter
@Entity
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cart_item_id;

    @ManyToOne
    @JoinColumn(name = "shopping_cart_id", nullable = false)
    private ShoppingCart shoppingCart;

    @OneToOne
    @JoinColumn(name = "promotion_id", nullable = false)
    private Promotion promotion;

    @Column(nullable = false)
    private Integer quantity;

    public CartItem(ShoppingCart shoppingCart, Promotion promotion, Integer quantity) {
        this.shoppingCart = shoppingCart;
        this.promotion = promotion;
        this.quantity = quantity;
    }

    public CartItem() {
    }

}

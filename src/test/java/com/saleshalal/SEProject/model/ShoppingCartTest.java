package com.saleshalal.SEProject.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartTest {

    @Test
    void addCartItem_ItemAddedToCart() {
        ShoppingCart cart = new ShoppingCart(new Customer());
        CartItem item = new CartItem();
        cart.addCartItem(item);

        assertTrue(cart.getCartItems().contains(item));
        assertEquals(cart, item.getShoppingCart());
    }

    @Test
    void removeCartItem_ItemRemovedFromCart() {
        ShoppingCart cart = new ShoppingCart(new Customer());
        CartItem item = new CartItem();
        cart.addCartItem(item);
        cart.removeCartItem(item);

        assertFalse(cart.getCartItems().contains(item));
        assertNull(item.getShoppingCart());
    }

    @Test
    void getTotalPrice_CalculatesTotalPriceCorrectly() {
        ShoppingCart cart = new ShoppingCart(new Customer());
        Promotion promotion1 = new Promotion();
        promotion1.setPrice(new BigDecimal("10.00"));
        CartItem item1 = new CartItem();
        item1.setPromotion(promotion1);
        item1.setQuantity(2);

        Promotion promotion2 = new Promotion();
        promotion2.setPrice(new BigDecimal("5.00"));
        CartItem item2 = new CartItem();
        item2.setPromotion(promotion2);
        item2.setQuantity(3);

        cart.addCartItem(item1);
        cart.addCartItem(item2);

        assertEquals(new BigDecimal("35.00"), cart.getTotalPrice());
    }

    @Test
    void getTotalPrice_EmptyCart_ReturnsZero() {
        ShoppingCart cart = new ShoppingCart(new Customer());

        assertEquals(BigDecimal.ZERO, cart.getTotalPrice());
    }
}
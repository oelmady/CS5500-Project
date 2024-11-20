package com.saleshalal.SEProject.service;

import static org.junit.jupiter.api.Assertions.*;

import com.saleshalal.SEProject.model.CartItem;
import com.saleshalal.SEProject.model.Customer;
import com.saleshalal.SEProject.model.Promotion;
import com.saleshalal.SEProject.model.ShoppingCart;
import com.saleshalal.SEProject.repository.CartItemRepository;
import com.saleshalal.SEProject.repository.PromotionRepository;
import com.saleshalal.SEProject.repository.ShoppingCartRepository;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;

class ShoppingCartServiceTest {

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private PromotionRepository promotionRepository;

    @InjectMocks
    private ShoppingCartService shoppingCartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests that the getCustomerCart method returns the customer's cart.
     */
    @Test
    void getCustomerCart_CartExists_ReturnsCart() {
        Customer customer = new Customer();
        ShoppingCart cart = new ShoppingCart();
        when(shoppingCartRepository.findByCustomer(customer)).thenReturn(Optional.of(cart));

        ShoppingCart result = shoppingCartService.getCustomerCart(customer);

        assertEquals(cart, result);
    }

    /**
     * Tests that the getCustomerCart method throws an exception when the cart does not exist.
     */
    @Test
    void getCustomerCart_CartDoesNotExist_ThrowsException() {
        Customer customer = new Customer();
        when(shoppingCartRepository.findByCustomer(customer)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> shoppingCartService.getCustomerCart(customer));
    }

    /**
     * Tests that the addToCart method adds a promotion to the cart if the promotion exists and has sufficient quantity for the transaction.
     */
    @Test
    void addToCart_PromotionExistsAndSufficientQuantity_AddsToCart() {
        Customer customer = new Customer();
        ShoppingCart cart = new ShoppingCart();
        Promotion promotion = new Promotion();
        promotion.setAvailableQuantity(10);
        CartItem cartItem = new CartItem();
        cartItem.setPromotion(promotion);
        cartItem.setQuantity(5);

        when(shoppingCartRepository.findByCustomer(customer)).thenReturn(Optional.of(cart));
        when(promotionRepository.findById(1L)).thenReturn(Optional.of(promotion));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);

        CartItem result = shoppingCartService.addToCart(customer, 1L, 5);

        assertEquals(cartItem, result);
        assertEquals(5, promotion.getAvailableQuantity());
    }

    /**
     * Tests that the addToCart method throws an exception when the promotion has insufficient quantity.
     */
    @Test
    void addToCart_PromotionExistsAndInsufficientQuantity_ThrowsException() {
        Customer customer = new Customer();
        ShoppingCart cart = new ShoppingCart();
        Promotion promotion = new Promotion();
        promotion.setAvailableQuantity(2);

        // Mock the shoppingCartRepository to return the cart when findByCustomer is called with the customer
        when(shoppingCartRepository.findByCustomer(customer)).thenReturn(Optional.of(cart));
        when(promotionRepository.findById(1L)).thenReturn(Optional.of(promotion));

        // Call the addToCart method and assert that it throws an exception because the promotion
        // has insufficient quantity
        assertThrows(RuntimeException.class, () -> shoppingCartService.addToCart(customer, 1L, 5));
    }

    /**
     * Tests that removing a cart item should remove the cart item from the cart.
     * Removing a cart item should restore the promotion's available quantity.
     */
    @Test
    void removeCartItem_CartItemExists_RemovesCartItem() {
        CartItem cartItem = new CartItem();
        Promotion promotion = new Promotion();
        promotion.setAvailableQuantity(5);
        cartItem.setPromotion(promotion);
        cartItem.setQuantity(3);

        // Mock the cartItemRepository to return the cart item when findById is called with the ID
        when(cartItemRepository.findById(1L)).thenReturn(Optional.of(cartItem));

        shoppingCartService.removeCartItem(1L);

        // Verify that the cart item was deleted and the promotion's available quantity was restored
        verify(cartItemRepository, times(1)).delete(cartItem);
        assertEquals(8, promotion.getAvailableQuantity());
    }

    /**
     * Tests that the removeCartItem method throws an exception when the cart item does not exist.
     */
    @Test
    void removeCartItem_CartItemDoesNotExist_ThrowsException() {
        // Mock the cartItemRepository to return an empty optional when findById is called with the ID
        when(cartItemRepository.findById(1L)).thenReturn(Optional.empty());

        // Call the removeCartItem method and assert that it throws an exception because the cart item does not exist
        assertThrows(ResourceNotFoundException.class, () -> shoppingCartService.removeCartItem(1L));
    }
}
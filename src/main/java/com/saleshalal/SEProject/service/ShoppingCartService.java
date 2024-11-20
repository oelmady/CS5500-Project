package com.saleshalal.SEProject.service;

import com.saleshalal.SEProject.exception.InsufficientQuantityException;
import com.saleshalal.SEProject.model.CartItem;
import com.saleshalal.SEProject.model.Customer;
import com.saleshalal.SEProject.model.Promotion;
import com.saleshalal.SEProject.model.ShoppingCart;
import com.saleshalal.SEProject.repository.CartItemRepository;
import com.saleshalal.SEProject.repository.PromotionRepository;
import com.saleshalal.SEProject.repository.ShoppingCartRepository;
import jakarta.transaction.Transactional;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;

    private final CartItemRepository cartItemRepository;

    private final PromotionRepository promotionRepository;

    @Autowired
    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, CartItemRepository cartItemRepository, PromotionRepository promotionRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.cartItemRepository = cartItemRepository;
        this.promotionRepository = promotionRepository;
    }

    @Transactional
    public ShoppingCart getCustomerCart(Customer customer) {
        return shoppingCartRepository.findByCustomer(customer)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
    }

    @Transactional
    public CartItem addToCart(Customer customer, Long promotionId, Integer quantity) {
        ShoppingCart cart = getCustomerCart(customer);

        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new ResourceNotFoundException("Promotion not found"));

        // Check availability
        if (promotion.getAvailableQuantity() < quantity) {
            throw new RuntimeException(new InsufficientQuantityException("Not enough items available"));
        }

        // Check if promotion already in cart
        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getPromotion().getId().equals(promotionId))
                .findFirst();

        CartItem cartItem;
        if (existingItem.isPresent()) {
            // Update quantity if item already in cart
            cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            // Create new cart item
            cartItem = new CartItem();
            cartItem.setShoppingCart(cart);
            cartItem.setPromotion(promotion);
            cartItem.setQuantity(quantity);
            cart.getCartItems().add(cartItem);
        }
        // Update promotion availability
        promotion.setAvailableQuantity(promotion.getAvailableQuantity() - quantity);
        promotionRepository.save(promotion);

        return cartItemRepository.save(cartItem);
    }


    @Transactional
    public void removeCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        // Restore promotion availability
        Promotion promotion = cartItem.getPromotion();
        promotion.setAvailableQuantity(
                promotion.getAvailableQuantity() + cartItem.getQuantity()
        );
        promotionRepository.save(promotion);

        // Remove cart item
        cartItemRepository.delete(cartItem);
    }

    /**
     * Update cart item quantity and promotion availability.
     *
     * @param cartItemId the ID of the cart item to update
     * @param quantity   the new quantity
     */
    @Transactional
    public void updateCartItemQuantity(Long cartItemId, Integer quantity) {
        //todo
    }
}

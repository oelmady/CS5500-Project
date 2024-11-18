package com.saleshalal.SEProject.repository;

import com.saleshalal.SEProject.model.CartItem;
import com.saleshalal.SEProject.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);
}

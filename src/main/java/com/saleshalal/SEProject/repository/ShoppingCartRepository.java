package com.saleshalal.SEProject.repository;

import com.saleshalal.SEProject.model.Customer;
import com.saleshalal.SEProject.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShoppingCartRepository
        extends JpaRepository<ShoppingCart, Long> {
    Optional<ShoppingCart> findByCustomer(Customer customer);
}

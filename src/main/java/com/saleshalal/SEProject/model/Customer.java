package com.saleshalal.SEProject.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "customers")
public class Customer extends AUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customer_id;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shopping_cart_id", nullable = false)
    private ShoppingCart cart = new ShoppingCart(this);

    public Customer(String email, String password, String name, UserRole role) {
        super(email, password, name, role);
    }

    public Customer() {
    }
}

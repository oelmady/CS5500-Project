package com.saleshalal.SEProject.model;

import com.saleshalal.SEProject.data.CustomerDTO;

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
    private Long id;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "customer_id", nullable = false)
    private ShoppingCart cart = new ShoppingCart(this);

    public Customer(String email, String password, String name, UserRole role) {
        super(email, password, name, role);
    }

    public Customer(CustomerDTO customerDTO) {
        super(customerDTO.getEmail(), customerDTO.getPassword(), customerDTO.getName(), UserRole.CUSTOMER);
    }

    public Customer() {
    }
}

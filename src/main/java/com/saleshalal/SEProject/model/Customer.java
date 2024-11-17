package com.saleshalal.SEProject.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "customers")
public class Customer extends User {

//    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
//    private List<Order> orders = new ArrayList<>();


    public Customer(String email, String password, String name, UserRole role) {
        super(email, password, name, role);
    }

    public Customer() {

    }
}

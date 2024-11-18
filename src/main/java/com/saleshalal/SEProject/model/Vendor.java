package com.saleshalal.SEProject.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "vendors")
public class Vendor extends AUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vendor_id;

    @NotBlank(message = "Business name is required")
    private String businessName;

    @NotBlank(message = "Business number is required")
    private String businessNumber;
    /**
     * Defines a one-to-many relationship between Vendor and Promotion.
     * Each Vendor can have multiple Promotion entities.
     * Changes to the Vendor entity are cascaded to the associated Promotion entities.
     */
    @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL)
    private List<Promotion> promotions = new ArrayList<>();

    public Vendor(
            String email,
            String password,
            String name,
            String businessName,
            String businessNumber) {
        super(email, password, name, UserRole.VENDOR);
        this.businessName = businessName;
        this.businessNumber = businessNumber;
    }

}

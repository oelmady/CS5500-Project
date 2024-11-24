package com.saleshalal.SEProject.model;

import com.saleshalal.SEProject.data.VendorDTO;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
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
    private Long id;

    @NotBlank(message = "Business name is required")
    private String businessName;

    @NotBlank(message = "Business number is required")
    private String businessNumber;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @Column(nullable = true)
    private String website;

    @Column(nullable = true)
    private String description;

    @Column(nullable = true)
    private String address;

    @Column(nullable = true)
    private String city;

    @Column(nullable = true)
    private String state;

    @Column(nullable = true)
    private String zip;

    @Column(nullable = true)
    private String businessType;

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
            String businessNumber,
            String phoneNumber,
            String website,
            String description,
            String address,
            String city,
            String state,
            String zip,
            String businessType) {
        super(email, password, name, UserRole.VENDOR);
        this.businessName = businessName;
        this.businessNumber = businessNumber;
        this.phoneNumber = phoneNumber;
        this.website = website;
        this.description = description;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.businessType = businessType;
    }

    public Vendor(VendorDTO vendorDTO) {
        super(vendorDTO.getEmail(), vendorDTO.getPassword(), vendorDTO.getName(), UserRole.VENDOR);
        this.businessName = vendorDTO.getBusinessName();
        this.businessNumber = vendorDTO.getBusinessNumber();
        this.phoneNumber = vendorDTO.getPhoneNumber();
        this.website = vendorDTO.getWebsite();
        this.description = vendorDTO.getDescription();
        this.address = vendorDTO.getAddress();
        this.city = vendorDTO.getCity();
        this.state = vendorDTO.getState();
        this.zip = vendorDTO.getZip();
        this.businessType = vendorDTO.getBusinessType();
    }

}

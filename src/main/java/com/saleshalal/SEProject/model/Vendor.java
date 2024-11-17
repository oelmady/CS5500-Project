package com.saleshalal.SEProject.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "vendors")
public class Vendor extends User {
    @Setter
    @NotBlank(message = "Business name is required")
    private String businessName;

    @NotBlank(message = "Business registration number is required")
    private String businessNumber;

    public Vendor(String email, String password, String name, String businessName, String businessNumber) {
        super(email, password, name, UserRole.VENDOR);
        this.businessName = businessName;
        this.businessNumber = businessNumber;
    }

//     @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL)
//     private List<Product> products = new ArrayList<>();

}

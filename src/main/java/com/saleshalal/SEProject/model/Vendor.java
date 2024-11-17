package com.saleshalal.SEProject.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vendors")
public class Vendor extends User {
    @NotBlank(message = "Business name is required")
    private String businessName;

    @NotBlank(message = "Business registration number is required")
    private String businessRegistration;

}

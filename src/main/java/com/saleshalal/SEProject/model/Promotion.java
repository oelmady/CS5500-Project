package com.saleshalal.SEProject.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "promotions")
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long promotion_id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer availableQuantity;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

// how to set expiry date and time in the form
// <label for="expiryDate">Select expiry date and time:</label>
//<input type="datetime-local" id="expiryDate" name="expiryDate">

    @ManyToOne
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    Promotion(Vendor vendor) {
        this.vendor = vendor;
    }

    public Promotion() {
    }

    public Promotion(
            String name,
            String description,
            BigDecimal price,
            Integer availableQuantity,
            LocalDateTime expiryDate,
            Vendor vendor) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.availableQuantity = availableQuantity;
        this.expiryDate = expiryDate;
        this.vendor = vendor;
    }
}

package com.merchant.services.offersservice.entity;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class Offer {

    @Id
    /*@GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")*/
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String description;

    @NotNull
    private LocalDateTime expirationDate;

    @NotNull
    @Min(0)
    private double price;

    @NotNull
    @Enumerated(EnumType.STRING)
    private OfferStatus status = OfferStatus.ACTIVE;

    @NotNull
    @Enumerated(EnumType.STRING)
    private MerchantCurrency currency = MerchantCurrency.GBP;

    public Offer(final String description, final LocalDateTime expirationDate, final double price) {
        this.description = description;
        this.expirationDate = expirationDate;
        this.price = price;
    }

    public Offer(final String description, final LocalDateTime expirationDate, final double price, final OfferStatus status) {
        this.description = description;
        this.expirationDate = expirationDate;
        this.price = price;
        this.status = status;
    }

    public Offer(final String description, final LocalDateTime expirationDate, final double price, final OfferStatus status, final MerchantCurrency currency) {
        this.description = description;
        this.expirationDate = expirationDate;
        this.price = price;
        this.status = status;
        this.currency = currency;
    }

    public Offer() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public OfferStatus getStatus() {
        return status;
    }

    public void setStatus(OfferStatus status) {
        this.status = status;
    }

    public MerchantCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(MerchantCurrency currency) {
        this.currency = currency;
    }
}

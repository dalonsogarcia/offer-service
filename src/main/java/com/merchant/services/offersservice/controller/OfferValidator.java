package com.merchant.services.offersservice.controller;

import com.merchant.services.offersservice.entity.Offer;
import com.merchant.services.offersservice.entity.OfferStatus;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * Validator for {@link Offer} objects
 */
public class OfferValidator implements ConstraintValidator<ValidOffer, Offer> {

    @Override
    public void initialize(final ValidOffer constraintAnnotation) {

    }

    @Override
    public boolean isValid(final Offer offer, final ConstraintValidatorContext context) {
        return !offer.getDescription().isEmpty() &&
                offer.getExpirationDate().isAfter(LocalDateTime.now(ZoneOffset.UTC));
    }
}

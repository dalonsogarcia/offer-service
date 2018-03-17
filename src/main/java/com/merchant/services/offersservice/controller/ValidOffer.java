package com.merchant.services.offersservice.controller;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = OfferValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidOffer {

    //The message to return when the instance fails the validation.
    String message() default "Invalid offer";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

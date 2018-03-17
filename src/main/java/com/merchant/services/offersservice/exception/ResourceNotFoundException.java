package com.merchant.services.offersservice.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(final String message) {
        super(String.format("{\"message\": \"%s\"}",message));
    }
}

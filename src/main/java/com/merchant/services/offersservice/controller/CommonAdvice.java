package com.merchant.services.offersservice.controller;

import com.merchant.services.offersservice.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

@ControllerAdvice
public class CommonAdvice {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundException(final ResourceNotFoundException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleConstraintViolationException(final ConstraintViolationException exception) {
        return format("Constraint violation: [%s]", ofNullable(exception).map(Throwable::getMessage).orElse("null"));
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, TransactionSystemException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleValidationEXception(){
        return format("{\"message\":\"Invalid request\"}");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public String handleMethodNotSupportedException() {
        return format("{\"message\":\"Method not allowed\"}");
    }
}

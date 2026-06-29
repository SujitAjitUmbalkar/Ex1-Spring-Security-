package com.example.demo4.SecurityApp.advice;

import com.example.demo4.SecurityApp.exceptions.NoSuchResourcesException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(NoSuchResourcesException.class)
    public ResponseEntity<ApiError> noSuchResourcesException(NoSuchResourcesException ex)
    {
        ApiError apiError = new ApiError(ex.getLocalizedMessage() , HttpStatus.NOT_FOUND);
        return new  ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

}

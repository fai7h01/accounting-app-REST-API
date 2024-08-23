package com.cydeo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionWrapper> userNotFoundExceptionHandler(UserNotFoundException exception, HttpServletRequest request){
        exception.printStackTrace();
        String message = exception.getMessage();
        ExceptionWrapper exceptionWrapper = new ExceptionWrapper(HttpStatus.NOT_FOUND.value(), message, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionWrapper);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ExceptionWrapper> userAlreadyExistsExceptionHandler(UserAlreadyExistsException exception, HttpServletRequest request){
        exception.printStackTrace();
        String message = exception.getMessage();
        ExceptionWrapper exceptionWrapper = new ExceptionWrapper(HttpStatus.CONFLICT.value(), message, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionWrapper);
    }

    @ExceptionHandler(UserDoesNotExistsException.class)
    public ResponseEntity<ExceptionWrapper> userDoesNotExistExceptionHandler(UserDoesNotExistsException exception, HttpServletRequest request){
        exception.printStackTrace();
        String message = exception.getMessage();
        ExceptionWrapper exceptionWrapper = new ExceptionWrapper(HttpStatus.NOT_FOUND.value(), message, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionWrapper);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ExceptionWrapper> productNotFoundExceptionHandler(ProductNotFoundException exception, HttpServletRequest request){
        exception.printStackTrace();
        String message = exception.getMessage();
        ExceptionWrapper exceptionWrapper = new ExceptionWrapper(HttpStatus.NOT_FOUND.value(), message, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionWrapper);
    }

    @ExceptionHandler(ProductLowLimitAlertException.class)
    public ResponseEntity<ExceptionWrapper> productLowLimitAlertExceptionHandler(ProductLowLimitAlertException exception, HttpServletRequest request){
        exception.printStackTrace();
        String message = exception.getMessage();
        ExceptionWrapper exceptionWrapper = new ExceptionWrapper(HttpStatus.OK.value(), message, request.getRequestURI());
        return ResponseEntity.ok(exceptionWrapper);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ExceptionWrapper> categoryNotFoundExceptionHandler(CategoryNotFoundException exception, HttpServletRequest request){
        exception.printStackTrace();
        String message = exception.getMessage();
        ExceptionWrapper exceptionWrapper = new ExceptionWrapper(HttpStatus.NOT_FOUND.value(), message, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionWrapper);
    }

    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public ResponseEntity<ExceptionWrapper> categoryAlreadyExistsExceptionHandler(CategoryAlreadyExistsException exception, HttpServletRequest request){
        exception.printStackTrace();
        String message = exception.getMessage();
        ExceptionWrapper exceptionWrapper = new ExceptionWrapper(HttpStatus.CONFLICT.value(), message, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionWrapper);
    }

    @ExceptionHandler(CategoryCantBeDeletedException.class)
    public ResponseEntity<ExceptionWrapper> categoryCantBeDeletedExceptionHandler(CategoryCantBeDeletedException exception, HttpServletRequest request){
        exception.printStackTrace();
        String message = exception.getMessage();
        ExceptionWrapper exceptionWrapper = new ExceptionWrapper(HttpStatus.CONFLICT.value(), message, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionWrapper);
    }

    @ExceptionHandler(CompanyNotFoundException.class)
    public ResponseEntity<ExceptionWrapper> companyNotFoundExceptionHandler(CompanyNotFoundException exception, HttpServletRequest request){
        exception.printStackTrace();
        String message = exception.getMessage();
        ExceptionWrapper exceptionWrapper = new ExceptionWrapper(HttpStatus.NOT_FOUND.value(), message, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionWrapper);
    }
}

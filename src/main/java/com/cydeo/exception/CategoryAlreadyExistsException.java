package com.cydeo.exception;

public class CategoryAlreadyExistsException extends RuntimeException{

    public CategoryAlreadyExistsException(String message){
        super(message);
    }
}

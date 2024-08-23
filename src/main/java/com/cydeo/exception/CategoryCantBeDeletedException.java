package com.cydeo.exception;

public class CategoryCantBeDeletedException extends RuntimeException{

    public CategoryCantBeDeletedException(String message){
        super(message);
    }
}

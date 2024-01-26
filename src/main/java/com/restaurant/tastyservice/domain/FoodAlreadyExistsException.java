package com.restaurant.tastyservice.domain;

public class FoodAlreadyExistsException extends RuntimeException{

    public FoodAlreadyExistsException(String ref){
        super("Food with ref "+ ref + " already exists");
    }
}

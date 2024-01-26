package com.restaurant.tastyservice.domain;

public class FoodNotFoundException extends RuntimeException{
    public FoodNotFoundException(String ref){
        super("The food with ref " + ref + " is not found");
    }
}

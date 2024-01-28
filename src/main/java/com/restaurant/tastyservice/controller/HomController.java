package com.restaurant.tastyservice.controller;

import com.restaurant.tastyservice.config.RestaurantProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class HomController {

    private final RestaurantProperties restaurantProperties;

    public HomController(RestaurantProperties restaurantProperties) {
        this.restaurantProperties = restaurantProperties;
    }

    @RequestMapping("/")
    public String sayGreetings() {
        return restaurantProperties.getGreeting();
    }
}

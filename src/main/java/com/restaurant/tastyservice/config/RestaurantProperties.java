package com.restaurant.tastyservice.config;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "restaurant")
public class RestaurantProperties {

    /**
     * A Greeting Message for the restaurant
     */
    private String greeting;

    public String getGreeting() {
        return greeting;
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }
}

package com.restaurant.tastyservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class HomController {

    @RequestMapping("/")
    public String sayGreetings(){
        return "Welcome to our Restaurant App ! ";
    }
}

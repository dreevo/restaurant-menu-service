package com.restaurant.tastyservice.controller;

import com.restaurant.tastyservice.domain.Food;
import com.restaurant.tastyservice.domain.FoodService;
import javax.validation.Valid;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("food")
public class FoodController {

    private final FoodService foodService;

    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @GetMapping
    public Iterable<Food> get() {
        return foodService.viewFoodList();
    }

    @GetMapping("{ref}")
    public Food getByRef(@PathVariable String ref) {
        return foodService.viewFoodDetails(ref);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Food post(@Valid @RequestBody Food food) {
        return foodService.addFoodToMenu(food);
    }

    @DeleteMapping("{ref}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String ref) {
        foodService.deleteFoodFromMenu(ref);
    }

    @PutMapping("{ref}")
    public Food put(@PathVariable String ref, @Valid @RequestBody Food food) {
        return foodService.editFoodDetails(food, ref);
    }
}

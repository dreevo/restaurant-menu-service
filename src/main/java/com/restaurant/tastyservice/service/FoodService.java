package com.restaurant.tastyservice.service;


import com.restaurant.tastyservice.domain.Food;
import com.restaurant.tastyservice.domain.FoodAlreadyExistsException;
import com.restaurant.tastyservice.domain.FoodNotFoundException;
import com.restaurant.tastyservice.domain.FoodRepository;
import org.springframework.stereotype.Service;

@Service
public class FoodService {

    private final FoodRepository foodRepository;

    public FoodService(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    public Iterable<Food> viewFoodList() {
        return foodRepository.findAll();
    }

    public Food viewFoodDetails(String ref) {
        return foodRepository.findByRef(ref).orElseThrow(() -> new FoodNotFoundException(ref));
    }

    public Food addFoodToMenu(Food food) {
        if (foodRepository.existsByRef(food.ref())) {
            throw new FoodAlreadyExistsException(food.ref());
        } else {
            return foodRepository.save(food);
        }
    }

    public void deleteFoodFromMenu(String ref) {
        foodRepository.deleteByRef(ref);
    }

    public Food editFoodDetails(Food food, String ref) {
        return foodRepository.findByRef(ref).map(foundFood ->
                new Food(foundFood.ref(), food.description(), food.price())
        ).orElseThrow(() -> new FoodNotFoundException(ref));
    }
}

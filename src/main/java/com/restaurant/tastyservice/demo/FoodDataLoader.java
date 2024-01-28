package com.restaurant.tastyservice.demo;


import com.restaurant.tastyservice.domain.Food;
import com.restaurant.tastyservice.domain.FoodRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("testdata")
public class FoodDataLoader {

    private final FoodRepository foodRepository;

    public FoodDataLoader(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadFoodTestData() {
        foodRepository.deleteAll();
        var food1 =
                Food.of("4546745467", "desc1", 5.5);
        var food2 =
                Food.of("6598989798", "desc2", 8.5);
        foodRepository.saveAll(List.of(food1, food2));
    }

}

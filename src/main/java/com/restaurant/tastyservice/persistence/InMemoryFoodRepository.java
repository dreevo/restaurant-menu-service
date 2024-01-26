package com.restaurant.tastyservice.persistence;

import com.restaurant.tastyservice.domain.Food;
import com.restaurant.tastyservice.domain.FoodRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryFoodRepository implements FoodRepository {

    private static final Map<String, Food> food = new ConcurrentHashMap<>();

    @Override
    public Iterable<Food> findAll() {
        return food.values();
    }

    @Override
    public Optional<Food> findByRef(String ref) {
        return existsByRef(ref) ? Optional.of(food.get(ref)) : Optional.empty();
    }

    @Override
    public boolean existsByRef(String ref) {
        return food.containsKey(ref) && food.get(ref) != null;
    }

    @Override
    public Food save(Food food) {
        InMemoryFoodRepository.food.put(food.ref(), food);
        return food;
    }

    @Override
    public void deleteByRef(String ref) {
        food.remove(ref);
    }
}

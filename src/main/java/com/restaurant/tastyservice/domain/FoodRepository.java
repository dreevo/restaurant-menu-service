package com.restaurant.tastyservice.domain;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FoodRepository {
    Iterable<Food> findAll();
    Optional<Food> findByRef(String ref);
    boolean existsByRef(String ref);
    Food save(Food food);
    void deleteByRef(String ref);

}

package com.restaurant.tastyservice.domain;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface FoodRepository extends CrudRepository<Food, Long> {

    Optional<Food> findByRef(String ref);
    boolean existsByRef(String ref);

    @Modifying
    @Transactional
    @Query("delete from Food where ref = :ref")
    void deleteByRef(String ref);

}

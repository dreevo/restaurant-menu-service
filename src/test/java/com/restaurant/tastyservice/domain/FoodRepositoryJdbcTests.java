package com.restaurant.tastyservice.domain;


import com.restaurant.tastyservice.config.DataConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Import(DataConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJdbcTest
public class FoodRepositoryJdbcTests {
    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private JdbcAggregateTemplate jdbcAggregateTemplate;

    @Test
    void findAllFood() {
        var food1 = Food.of("4546745467", "test desc", 4.6);
        var food2 = Food.of("4534235467", "test desc 2", 4.3);
        jdbcAggregateTemplate.insert(food1);
        jdbcAggregateTemplate.insert(food2);

        Iterable<Food> actualFood = foodRepository.findAll();

        Assertions.assertThat(StreamSupport.stream(actualFood.spliterator(), true)
                .filter(food -> food.ref().equals(food1.ref()) || food.ref().equals(food2.ref()))
                .collect(Collectors.toList())).hasSize(2);
    }

    @Test
    void findFoodByRefWhenExisting() {
        var foodRef = "4546745467";
        var food = Food.of(foodRef, "test desc", 4.6);
        jdbcAggregateTemplate.insert(food);

        Optional<Food> actualFood = foodRepository.findByRef(foodRef);

        Assertions.assertThat(actualFood).isPresent();
        Assertions.assertThat(actualFood.get().ref()).isEqualTo(food.ref());
    }

    @Test
    void findFoodByRefWhenNotExisting() {
        Optional<Food> actualFood = foodRepository.findByRef("4546745467");
        Assertions.assertThat(actualFood).isEmpty();
    }

    @Test
    void existsByRefWhenExisting() {
        var foodRef = "1234561239";
        var foodToCreate = Food.of(foodRef, "test desc", 4.6);
        jdbcAggregateTemplate.insert(foodToCreate);

        boolean existing = foodRepository.existsByRef(foodRef);

        Assertions.assertThat(existing).isTrue();
    }

    @Test
    void existsByRefWhenNotExisting() {
        boolean existing = foodRepository.existsByRef("4546745467");
        Assertions.assertThat(existing).isFalse();
    }

    @Test
    void deleteByRef() {
        var foodRef = "4546745467";
        var foodToCreate = Food.of(foodRef, "test desc", 4.6);
        var persistedFood = jdbcAggregateTemplate.insert(foodToCreate);

        foodRepository.deleteByRef(foodRef);

        Assertions.assertThat(jdbcAggregateTemplate.findById(persistedFood.id(), Food.class)).isNull();
    }


}

package com.restaurant.tastyservice.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class FoodValidationTests {

    private static Validator validator;


    @BeforeAll
    static void setUp(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAllFieldsCorrectThenValidationSucceeds(){
        var food = new Food("4546745467", "desc", 5.5);
        Set<ConstraintViolation<Food>> violations = validator.validate(food);
        Assertions.assertThat(violations).isEmpty();
    }

    @Test
    void whenRefDefinedButIncorrectThenValidationFails(){
        var food = new Food("45467454", "desc", 5.5);
        Set<ConstraintViolation<Food>> violations = validator.validate(food);
        Assertions.assertThat(violations).hasSize(1);
        Assertions.assertThat(violations.iterator().next().getMessage()).isEqualTo("The ref format must be valid.");
    }
}

package com.restaurant.tastyservice.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FoodValidationTests {

    private static Validator validator;


    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAllFieldsCorrectThenValidationSucceeds() {
        var food = Food.of("4546745467", "desc", 5.5);
        Set<ConstraintViolation<Food>> violations = validator.validate(food);
        Assertions.assertThat(violations).isEmpty();
    }

    @Test
    void whenRefNotDefinedThenValidationFails() {
        var food = Food.of("", "desc", 5.5);
        Set<ConstraintViolation<Food>> violations = validator.validate(food);
        Assertions.assertThat(violations).hasSize(2);
        List<String> constraintViolationMessages = violations.stream()
                .map(ConstraintViolation::getMessage).collect(Collectors.toList());
        Assertions.assertThat(constraintViolationMessages)
                .contains("The food ref must be defined.")
                .contains("The ref format must be valid.");
    }

    @Test
    void whenDescriptionNotDefinedThenValidationFails() {
        var food = Food.of("4546745467", "", 5.5);
        Set<ConstraintViolation<Food>> violations = validator.validate(food);
        Assertions.assertThat(violations).hasSize(1);
        List<String> constraintViolationMessages = violations.stream()
                .map(ConstraintViolation::getMessage).collect(Collectors.toList());
        Assertions.assertThat(constraintViolationMessages)
                .contains("The food description must be defined.");
    }

    @Test
    void whenPriceNotDefinedThenValidationFails() {
        var food = Food.of("4546745467", "desc", null);
        Set<ConstraintViolation<Food>> violations = validator.validate(food);
        Assertions.assertThat(violations).hasSize(1);
        List<String> constraintViolationMessages = violations.stream()
                .map(ConstraintViolation::getMessage).collect(Collectors.toList());
        Assertions.assertThat(constraintViolationMessages)
                .contains("The food price must be defined.");
    }

    @Test
    void whenPriceDefinedButNotPositiveThenValidationFails() {
        var food = Food.of("4546745467", "desc", -5.5);
        Set<ConstraintViolation<Food>> violations = validator.validate(food);
        Assertions.assertThat(violations).hasSize(1);
        List<String> constraintViolationMessages = violations.stream()
                .map(ConstraintViolation::getMessage).collect(Collectors.toList());
        Assertions.assertThat(constraintViolationMessages)
                .contains("The food price must be greater than zero.");
    }


    @Test
    void whenRefDefinedButIncorrectThenValidationFails() {
        var food = Food.of("45467454", "desc", 5.5);
        Set<ConstraintViolation<Food>> violations = validator.validate(food);
        Assertions.assertThat(violations).hasSize(1);
        Assertions.assertThat(violations.iterator().next().getMessage()).isEqualTo("The ref format must be valid.");
    }
}

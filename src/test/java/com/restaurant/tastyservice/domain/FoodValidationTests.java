package com.restaurant.tastyservice.domain;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(violations).isEmpty();
    }

    @Test
    void whenRefNotDefinedThenValidationFails() {
        var food = Food.of("", "desc", 5.5);
        Set<ConstraintViolation<Food>> violations = validator.validate(food);
        assertThat(violations).hasSize(2);
        List<String> constraintViolationMessages = violations.stream()
                .map(ConstraintViolation::getMessage).collect(Collectors.toList());
        assertThat(constraintViolationMessages)
                .contains("The food ref must be defined.")
                .contains("The ref format must be valid.");
    }

    @Test
    void whenDescriptionNotDefinedThenValidationFails() {
        var food = Food.of("4546745467", "", 5.5);
        Set<ConstraintViolation<Food>> violations = validator.validate(food);
        assertThat(violations).hasSize(1);
        List<String> constraintViolationMessages = violations.stream()
                .map(ConstraintViolation::getMessage).collect(Collectors.toList());
        assertThat(constraintViolationMessages)
                .contains("The food description must be defined.");
    }

    @Test
    void whenPriceNotDefinedThenValidationFails() {
        var food = Food.of("4546745467", "desc", null);
        Set<ConstraintViolation<Food>> violations = validator.validate(food);
        assertThat(violations).hasSize(1);
        List<String> constraintViolationMessages = violations.stream()
                .map(ConstraintViolation::getMessage).collect(Collectors.toList());
        assertThat(constraintViolationMessages)
                .contains("The food price must be defined.");
    }

    @Test
    void whenPriceDefinedButNotPositiveThenValidationFails() {
        var food = Food.of("4546745467", "desc", -5.5);
        Set<ConstraintViolation<Food>> violations = validator.validate(food);
        assertThat(violations).hasSize(1);
        List<String> constraintViolationMessages = violations.stream()
                .map(ConstraintViolation::getMessage).collect(Collectors.toList());
        assertThat(constraintViolationMessages)
                .contains("The food price must be greater than zero.");
    }


    @Test
    void whenRefDefinedButIncorrectThenValidationFails() {
        var food = Food.of("45467454", "desc", 5.5);
        Set<ConstraintViolation<Food>> violations = validator.validate(food);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("The ref format must be valid.");
    }
}

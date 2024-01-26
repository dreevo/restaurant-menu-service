package com.restaurant.tastyservice.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record Food(

        @NotBlank(message = "The food ref must be defined.")
        @Pattern(regexp = "^([0-9]{10}|[0-9]{13})$",
                message = "The ref format must be valid."
        )
        String ref,
        @NotBlank(
                message = "The food description must be defined."
        )
        String description,
        @NotNull(message = "The food price must be defined.")
        @Positive(
                message = "The food price must be greater than zero."
        )
        Double price

) {
}

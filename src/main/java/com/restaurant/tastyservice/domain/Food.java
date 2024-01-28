package com.restaurant.tastyservice.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

import java.time.Instant;

public record Food(

        @Id
        Long id,

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
        Double price,

        String chef,

        @Version
        int version,

        @CreatedDate
        Instant createdDate,
        @LastModifiedDate
        Instant lastModifiedDate


) {

    public static Food of(
            String ref, String description, Double price
    ) {
        return new Food(
                null, ref, description, price, "MrChef", 0,null, null
        );
    }

}

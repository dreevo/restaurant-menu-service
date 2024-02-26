package com.restaurant.tastyservice.domain;

import org.springframework.data.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
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
        Instant lastModifiedDate,

        @CreatedBy
        String createdBy,

        @LastModifiedBy
        String lastModifiedBy


) {

    public static Food of(
            String ref, String description, Double price
    ) {
        return new Food(
                null, ref, description, price, "MrChef", 0, null, null, null, null
        );
    }

}

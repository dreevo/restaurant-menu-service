package com.restaurant.tastyservice.controller;

import com.restaurant.tastyservice.domain.Food;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.time.Instant;

@JsonTest
public class FoodJsonTests {

    @Autowired
    private JacksonTester<Food> json;


    @Test
    void testSerialize() throws Exception {
        Instant now = Instant.now();
        var expectedFood = new Food(6598L, "4546745467", "desc", 5.5, "MrChef", 24, now, now);
        var jsonContent = json.write(expectedFood);
        Assertions.assertThat(jsonContent).extractingJsonPathNumberValue("@.price")
                .isEqualTo(expectedFood.price());
        Assertions.assertThat(jsonContent).extractingJsonPathValue("@.ref")
                .isEqualTo(expectedFood.ref());
        Assertions.assertThat(jsonContent).extractingJsonPathValue("@.description")
                .isEqualTo(expectedFood.description());
        Assertions.assertThat(jsonContent).extractingJsonPathValue("@.chef")
                .isEqualTo(expectedFood.chef());
        Assertions.assertThat(jsonContent).extractingJsonPathNumberValue("@.id")
                .isEqualTo(expectedFood.id().intValue());
        Assertions.assertThat(jsonContent).extractingJsonPathValue("@.createdDate")
                .isEqualTo(expectedFood.createdDate().toString());
        Assertions.assertThat(jsonContent).extractingJsonPathValue("@.lastModifiedDate")
                .isEqualTo(expectedFood.lastModifiedDate().toString());
    }

    @Test
    void testDeserialize() throws Exception {
        Instant createdAt = Instant.parse("2023-03-05T23:40:32.145029Z");
        Instant lastModifiedAt = Instant.parse("2023-03-05T23:40:32.145029Z");
        var content = """
                {
                "id": 566,
                "ref" : "4546745467",
                "description" : "this is a description",
                "price" : 5.2,
                "chef" : "MrChef",
                "createdDate": "2023-03-05T23:40:32.145029Z",
                "lastModifiedDate": "2023-03-05T23:40:32.145029Z",
                "version": 23
                }
                """;
        Assertions.assertThat(json.parse(content)).usingRecursiveComparison()
                .isEqualTo(new Food(566L, "4546745467",
                        "this is a description", 5.2, "MrChef",
                        23, createdAt, lastModifiedAt));
    }
}

package com.restaurant.tastyservice.controller;

import com.restaurant.tastyservice.domain.Food;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

@JsonTest
public class FoodJsonTests {

    @Autowired
    private JacksonTester<Food> json;


    @Test
    void testSerialize() throws Exception {
        var expectedFood = new Food("4546745467", "desc", 5.5);
        var jsonContent = json.write(expectedFood);
        Assertions.assertThat(jsonContent).extractingJsonPathNumberValue("@.price")
                .isEqualTo(expectedFood.price());
        Assertions.assertThat(jsonContent).extractingJsonPathValue("@.ref")
                .isEqualTo(expectedFood.ref());
        Assertions.assertThat(jsonContent).extractingJsonPathValue("@.description")
                .isEqualTo(expectedFood.description());
    }

    @Test
    void testDeserialize() throws Exception {
        var content = """
                {
                "ref" : "4546745467",
                "description" : "this is a description",
                "price" : 5.2
                }
                """;
        Assertions.assertThat(json.parse(content)).usingRecursiveComparison()
                .isEqualTo(new Food("4546745467", "this is a description", 5.2));
    }
}

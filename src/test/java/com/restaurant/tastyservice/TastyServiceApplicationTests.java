package com.restaurant.tastyservice;

import com.restaurant.tastyservice.domain.Food;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TastyServiceApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void whenPostRequestThenFoodCreated() {
        var expectedFood = new Food("4546745467", "desc", 5.5);
        webTestClient.post().uri("/food").bodyValue(expectedFood)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Food.class).value(result -> {
                    Assertions.assertThat(result).isNotNull();
                    Assertions.assertThat(result.ref()).isEqualTo(expectedFood.ref());
                });
    }


}

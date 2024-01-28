package com.restaurant.tastyservice;

import com.restaurant.tastyservice.domain.Food;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Profile("integration")
class TastyServiceApplicationTests {

    @Autowired
    private WebTestClient webTestClient;


    @Test
    void whenGetRequestWithRefThenFoodReturned() {
        var foodRef = "4546745461";
        var foodToCreate = Food.of(foodRef, "desc", 5.5);
        Food expectedFood = webTestClient
                .post()
                .uri("/food")
                .bodyValue(foodToCreate)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Food.class).value(food -> Assertions.assertThat(food).isNotNull())
                .returnResult().getResponseBody();

        webTestClient
                .get()
                .uri("/food/" + foodRef)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Food.class).value(actualFood -> {
                    Assertions.assertThat(actualFood).isNotNull();
                    Assertions.assertThat(actualFood.ref()).isEqualTo(expectedFood.ref());
                });
    }

    @Test
    void whenPostRequestThenFoodCreated() {
        var expectedFood = Food.of("4546745462", "desc", 5.5);
        webTestClient.post().uri("/food").bodyValue(expectedFood)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Food.class).value(result -> {
                    Assertions.assertThat(result).isNotNull();
                    Assertions.assertThat(result.ref()).isEqualTo(expectedFood.ref());
                });
    }


    @Test
    void whenPutRequestThenFoodUpdated() {
        var foodRef = "4546745463";
        var foodToCreate = Food.of(foodRef, "desc", 5.5);
        Food createdFood = webTestClient
                .post()
                .uri("/food")
                .bodyValue(foodToCreate)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Food.class).value(food -> Assertions.assertThat(food).isNotNull())
                .returnResult().getResponseBody();
        var foodToUpdate = new Food(createdFood.id(), createdFood.ref(), createdFood.description(), 7.5,
                createdFood.version(), createdFood.createdDate(), createdFood.lastModifiedDate());

        webTestClient
                .put()
                .uri("/food/" + foodRef)
                .bodyValue(foodToUpdate)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Food.class).value(actualFood -> {
                    Assertions.assertThat(actualFood).isNotNull();
                    Assertions.assertThat(actualFood.price()).isEqualTo(foodToUpdate.price());
                });

    }

    @Test
    void whenDeleteRequestThenFoodDeleted() {
        var foodRef = "4546745464";
        var foodToCreate = Food.of(foodRef, "desc", 5.5);
        webTestClient
                .post()
                .uri("/food")
                .bodyValue(foodToCreate)
                .exchange()
                .expectStatus().isCreated();

        webTestClient
                .delete()
                .uri("/food/" + foodRef)
                .exchange()
                .expectStatus().isNoContent();

        webTestClient
                .get()
                .uri("/food/" + foodRef)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class).value(errorMessage ->
                        Assertions.assertThat(errorMessage).isEqualTo("The food with ref " + foodRef + " is not found")
                );
    }


}

package com.restaurant.tastyservice;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.restaurant.tastyservice.domain.Food;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.google.common.net.HttpHeaders;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
@Testcontainers
class TastyServiceApplicationTests {

    @Autowired
    private WebTestClient webTestClient;
    private static KeycloakToken johnTokens;
    private static KeycloakToken willTokens;


    @Container
    private static final KeycloakContainer keycloakContainer =
            new KeycloakContainer("quay.io/keycloak/keycloak:19.0")
                    .withRealmImportFile("test-realm-config.json");

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri",
                () -> keycloakContainer.getAuthServerUrl() + "realms/restaurant");
    }


    @BeforeAll
    static void generateAccessTokens() {
        WebClient webClient = WebClient.builder()
                .baseUrl(keycloakContainer.getAuthServerUrl()
                        + "realms/restaurant/protocol/openid-connect/token")
                .defaultHeader(HttpHeaders.CONTENT_TYPE,
                        MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();

        johnTokens = authenticateWith(
                "john", "password", webClient);
        willTokens = authenticateWith(
                "will", "password", webClient);
    }

    private static KeycloakToken authenticateWith(
            String username, String password, WebClient webClient
    ) {
        return webClient
                .post()
                .body(
                        BodyInserters.fromFormData("grant_type", "password")
                                .with("client_id", "restaurant-test")
                                .with("username", username)
                                .with("password", password)
                )
                .retrieve()
                .bodyToMono(KeycloakToken.class)
                .block();
    }

    private record KeycloakToken(String accessToken) {
        @JsonCreator
        private KeycloakToken(
                @JsonProperty("access_token") final String accessToken
        ) {
            this.accessToken = accessToken;
        }
    }


    @Test
    void whenGetRequestWithRefThenFoodReturned() {
        var foodRef = "4546745440";
        var foodToCreate = Food.of(foodRef, "desc", 5.5);
        Food expectedFood = webTestClient
                .post()
                .uri("/food")
                .headers(headers ->
                        headers.setBearerAuth(willTokens.accessToken()))
                .bodyValue(foodToCreate)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Food.class).value(food -> Assertions.assertThat(food).isNotNull())
                .returnResult().getResponseBody();

        webTestClient
                .get()
                .uri("/food/" + foodRef)
                .headers(headers ->
                        headers.setBearerAuth(willTokens.accessToken()))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Food.class).value(actualFood -> {
                    Assertions.assertThat(actualFood).isNotNull();
                    Assertions.assertThat(actualFood.ref()).isEqualTo(expectedFood.ref());
                });
    }

    @Test
    void whenPostRequestThenFoodCreated() {
        var expectedFood = Food.of("4546745430", "desc", 5.5);
        webTestClient.post().uri("/food")
                .headers(headers ->
                        headers.setBearerAuth(willTokens.accessToken()))
                .bodyValue(expectedFood)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Food.class).value(result -> {
                    Assertions.assertThat(result).isNotNull();
                    Assertions.assertThat(result.ref()).isEqualTo(expectedFood.ref());
                });
    }

    @Test
    void whenPostRequestUnauthenticatedThen401() {
        var expectedFood = Food.of("4546745430", "desc", 5.5);

        webTestClient
                .post()
                .uri("/food")
                .bodyValue(expectedFood)
                .exchange()
                .expectStatus().isUnauthorized();
    }


    @Test
    void whenPostRequestUnauthorizedThen403() {
        var expectedFood = Food.of("4546745430", "desc", 5.5);

        webTestClient
                .post()
                .uri("/food")
                .headers(headers -> headers.setBearerAuth(johnTokens.accessToken()))
                .bodyValue(expectedFood)
                .exchange()
                .expectStatus().isForbidden();
    }


    @Test
    void whenPutRequestThenFoodUpdated() {
        var foodRef = "4546745420";
        var foodToCreate = Food.of(foodRef, "desc", 5.5);
        Food createdFood = webTestClient
                .post()
                .uri("/food")
                .headers(headers ->
                        headers.setBearerAuth(willTokens.accessToken()))
                .bodyValue(foodToCreate)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Food.class).value(food -> Assertions.assertThat(food).isNotNull())
                .returnResult().getResponseBody();
        var foodToUpdate = new Food(createdFood.id(), createdFood.ref(), createdFood.description(), 7.5, createdFood.chef(),
                createdFood.version(), createdFood.createdDate(), createdFood.lastModifiedDate(), createdFood.createdBy(), createdFood.lastModifiedBy());

        webTestClient
                .put()
                .uri("/food/" + foodRef)
                .headers(headers ->
                        headers.setBearerAuth(willTokens.accessToken()))
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
        var foodRef = "4546745410";
        var foodToCreate = Food.of(foodRef, "desc", 5.5);
        webTestClient
                .post()
                .uri("/food")
                .headers(headers ->
                        headers.setBearerAuth(willTokens.accessToken()))
                .bodyValue(foodToCreate)
                .exchange()
                .expectStatus().isCreated();

        webTestClient
                .delete()
                .uri("/food/" + foodRef)
                .headers(headers ->
                        headers.setBearerAuth(willTokens.accessToken()))
                .exchange()
                .expectStatus().isNoContent();

        webTestClient
                .get()
                .uri("/food/" + foodRef)
                .headers(headers ->
                        headers.setBearerAuth(willTokens.accessToken()))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class).value(errorMessage ->
                        Assertions.assertThat(errorMessage).isEqualTo("The food with ref " + foodRef + " was not found.")
                );
    }


}

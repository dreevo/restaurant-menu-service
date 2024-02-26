package com.restaurant.tastyservice.controller;

import com.restaurant.tastyservice.config.SecurityConfig;
import com.restaurant.tastyservice.domain.Food;
import com.restaurant.tastyservice.domain.FoodNotFoundException;
import com.restaurant.tastyservice.domain.FoodService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(FoodController.class)
@Import(SecurityConfig.class)
public class FoodControllerMVCTests {

    private static final String ROLE_EMPLOYEE = "ROLE_employee";
    private static final String ROLE_CUSTOMER = "ROLE_customer";

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    FoodService foodService;

    @MockBean
    JwtDecoder jwtDecoder;


    @Test
    void whenGetFoodExistingAndAuthenticatedThenShouldReturn200() throws Exception {
        var ref = "658778787";
        var expectedFood = Food.of("658778787", "desc", 5.5);
        given(foodService.viewFoodDetails(ref)).willReturn(expectedFood);
        mockMvc
                .perform(get("/food/" + ref)
                        .with(jwt()))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetFoodExistingAndNotAuthenticatedThenShouldReturn200() throws Exception {
        var ref = "658778787";
        var expectedFood = Food.of("658778787", "desc", 5.5);
        given(foodService.viewFoodDetails(ref)).willReturn(expectedFood);
        mockMvc
                .perform(get("/food/" + ref))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetFoodNotExistingAndAuthenticatedThenShouldReturn404() throws Exception {
        var ref = "658778787";
        given(foodService.viewFoodDetails(ref)).willThrow(FoodNotFoundException.class);
        mockMvc
                .perform(get("/food/" + ref)
                        .with(jwt()))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGetFoodNotExistingAndNotAuthenticatedThenShouldReturn404() throws Exception {
        var ref = "658778787";
        given(foodService.viewFoodDetails(ref)).willThrow(FoodNotFoundException.class);
        mockMvc
                .perform(get("/food/" + ref))
                .andExpect(status().isNotFound());
    }


    @Test
    void whenDeleteFoodWithEmployeeRoleThenShouldReturn204()
            throws Exception {
        var ref = "658778787";
        mockMvc
                .perform(MockMvcRequestBuilders.delete("/food/" + ref)
                        .with(jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_employee"))))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void whenDeleteFoodWithCustomerRoleThenShouldReturn403()
            throws Exception {
        var ref = "658778787";
        mockMvc
                .perform(MockMvcRequestBuilders.delete("/food/" + ref)
                        .with(jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_customer"))))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenDeleteFoodNotAuthenticatedThenShouldReturn401() throws Exception {
        var ref = "658778787";
        mockMvc
                .perform(delete("/food/" + ref))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenPostFoodWithEmployeeRoleThenShouldReturn201() throws Exception {
        var ref = "4546745417";
        var foodToCreate = Food.of(ref, "desc", 5.5);
        given(foodService.addFoodToMenu(foodToCreate)).willReturn(foodToCreate);
        mockMvc
                .perform(post("/food")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(foodToCreate))
                        .with(jwt().authorities(new SimpleGrantedAuthority(ROLE_EMPLOYEE))))
                .andExpect(status().isCreated());
    }

    @Test
    void whenPostFoodWithCustomerRoleThenShouldReturn403() throws Exception {
        var ref = "658778785";
        var foodToCreate = Food.of(ref, "desc", 5.5);
        given(foodService.addFoodToMenu(foodToCreate)).willReturn(foodToCreate);
        mockMvc
                .perform(post("/food")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(foodToCreate))
                        .with(jwt().authorities(new SimpleGrantedAuthority(ROLE_CUSTOMER))))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenPostFoodAndNotAuthenticatedThenShouldReturn403() throws Exception {
        var ref = "658778782";
        var foodToCreate = Food.of(ref, "desc", 5.5);
        given(foodService.addFoodToMenu(foodToCreate)).willReturn(foodToCreate);
        mockMvc
                .perform(post("/food")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(foodToCreate)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenPutFoodWithEmployeeRoleThenShouldReturn200() throws Exception {
        var ref = "4546745412";
        var foodToCreate = Food.of(ref, "desc", 5.5);
        given(foodService.addFoodToMenu(foodToCreate)).willReturn(foodToCreate);
        mockMvc
                .perform(put("/food/" + ref)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(foodToCreate))
                        .with(jwt().authorities(new SimpleGrantedAuthority(ROLE_EMPLOYEE))))
                .andExpect(status().isOk());
    }

    @Test
    void whenPutFoodWithCustomerRoleThenShouldReturn403() throws Exception {
        var ref = "658778786";
        var foodToCreate = Food.of(ref, "desc", 5.5);
        given(foodService.addFoodToMenu(foodToCreate)).willReturn(foodToCreate);
        mockMvc
                .perform(put("/food/" + ref)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(foodToCreate))
                        .with(jwt().authorities(new SimpleGrantedAuthority(ROLE_CUSTOMER))))
                .andExpect(status().isForbidden());
    }

}

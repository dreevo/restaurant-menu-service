package com.restaurant.tastyservice.controller;

import com.restaurant.tastyservice.domain.FoodNotFoundException;
import com.restaurant.tastyservice.domain.FoodService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.BDDMockito.given;

@WebMvcTest(FoodController.class)
public class FoodControllerMVCTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FoodService foodService;

    @Test
    void whenGetFoodNotExistingThenShouldReturn404() throws Exception {
        String ref = "658778787";
        given(foodService.viewFoodDetails(ref)).willThrow(FoodNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/food/" + ref))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}

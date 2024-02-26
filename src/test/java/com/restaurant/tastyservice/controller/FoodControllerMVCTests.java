package com.restaurant.tastyservice.controller;

import com.restaurant.tastyservice.domain.FoodService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(FoodController.class)
public class FoodControllerMVCTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FoodService foodService;



    @Test
    void whenDeleteFoodWithEmployeeRoleThenShouldReturn204()
            throws Exception {
        var ref = "658778787";
        mockMvc
                .perform(MockMvcRequestBuilders.delete("/food/" + ref)
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_employee"))))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

}

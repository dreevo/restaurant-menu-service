package com.restaurant.tastyservice.domain;

import com.restaurant.tastyservice.service.FoodService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FoodServiceTests {

    @Mock
    private FoodRepository foodRepository;
    @InjectMocks
    private FoodService foodService;


    @Test
    void whenFoodCreateButAlreadyExistsThenThrows() {
        var ref = "4546745467";
        var foodToCreate = Food.of("4546745467", "desc", 5.5);
        when(foodRepository.existsByRef(ref)).thenReturn(true);
        Assertions.assertThatThrownBy(() -> foodService.addFoodToMenu(foodToCreate))
                .isInstanceOf(FoodAlreadyExistsException.class)
                .hasMessage("Food with ref " + ref + " already exists");

    }

    @Test
    void whenFoodReadButDoesNotExistThenThrows() {
        var ref = "4546745467";
        when(foodRepository.findByRef(ref)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> foodService.viewFoodDetails(ref))
                .isInstanceOf(FoodAlreadyExistsException.class)
                .hasMessage("The food with ref " + ref + " was not found.");
    }
}

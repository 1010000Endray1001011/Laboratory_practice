package ru.restaurant.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDto {
    private String visitorName;
    private LocalDateTime orderStarted;
    private LocalDateTime orderEnded;
    private double orderTotal;
    private List<OrderDishDto> orderDishes;

    @Data
    public static class OrderDishDto {
        private int ordDishId;
        private int menuDish;
    }
}
package ru.restaurant.dto;

import lombok.Data;
import java.util.List;

@Data
public class MenuDto {
    private int menuDishId;
    private int menuDishCard;
    private double menuDishPrice;
    private boolean menuDishActive;
}
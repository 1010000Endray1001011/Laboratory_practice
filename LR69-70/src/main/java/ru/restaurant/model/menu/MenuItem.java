package ru.restaurant.model.menu;

import lombok.Data;
import java.util.List;

@Data
public class MenuItem {
    private int id;
    private int cardId;
    private String name;
    private double price;
    private boolean active;
    private double preparationTime;
    private List<Integer> requiredProductTypes;
    private List<Integer> requiredEquipmentTypes;
}
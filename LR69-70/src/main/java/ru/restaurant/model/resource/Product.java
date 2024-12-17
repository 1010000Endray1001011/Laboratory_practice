package ru.restaurant.model.resource;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Product {
    private int productId;
    private String name;
    private String company;
    private String unit;
    private double quantity;
    private double cost;
    private LocalDateTime deliveredAt;
    private LocalDateTime validUntil;
    private double reservedQuantity;
    private boolean isFood;

    public boolean canReserve(double amount) {
        return quantity - reservedQuantity >= amount;
    }

    public void reserve(double amount) {
        if (canReserve(amount)) {
            reservedQuantity += amount;
        } else {
            throw new IllegalStateException("Невозможно зарезервировать больше, чем доступно");
        }
    }

    public void release(double amount) {
        if (reservedQuantity >= amount) {
            reservedQuantity -= amount;
        } else {
            throw new IllegalStateException("Невозможно освободить больше, чем зарезервировано");
        }
    }

    public double getAvailableQuantity() {
        return quantity - reservedQuantity;
    }
}
package ru.restaurant.model.process;

import lombok.Data;
import java.util.List;

@Data
public class Operation {
    private int id;
    private int operType;
    private double operTime;
    private int operAsyncPoint;
    private boolean completed;
    private List<ProductRequirement> operProducts;

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Data
    public static class ProductRequirement {
        private int prodType;
        private double prodQuantity;
    }
}
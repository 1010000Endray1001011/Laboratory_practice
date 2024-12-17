package ru.restaurant.model.resource;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Cook {
    private int cookId;
    private String cookName;
    private boolean active;
    private double workloadToday;
    private LocalDateTime lastOperationFinished;
}
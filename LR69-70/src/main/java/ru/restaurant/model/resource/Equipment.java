package ru.restaurant.model.resource;

import lombok.Data;

@Data
public class Equipment {
    private int equipType;
    private String equipName;
    private boolean active;
    private boolean inUse;
    private Integer currentOperationId;
}
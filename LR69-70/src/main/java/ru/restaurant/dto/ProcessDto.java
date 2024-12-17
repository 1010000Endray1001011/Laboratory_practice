package ru.restaurant.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProcessDto {
    private int procId;
    private int ordDish;
    private LocalDateTime procStarted;
    private LocalDateTime procEnded;
    private boolean procActive;
    private List<ProcessOperationDto> procOperations;

    @Data
    public static class ProcessOperationDto {
        private int procOper;
        private int operType;
        private double operTime;
        private int operAsyncPoint;
    }
}
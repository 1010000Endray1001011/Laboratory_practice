package com.kitisgang.lpz66466.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

public class InventoryDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotNull(message = "Название товара обязательно")
        private String name;

        @NotNull(message = "Количество обязательно")
        @PositiveOrZero(message = "Количество не может быть отрицательным")
        private Integer quantity;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String name;
        private Integer quantity;
        private LocalDateTime lastUpdated;
        private Boolean lowStock;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        private String name;
        @PositiveOrZero(message = "Количество не может быть отрицательным")
        private Integer quantity;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdjustQuantityRequest {
        @NotNull(message = "Значение корректировки обязательно")
        private Integer adjustment;
    }
}

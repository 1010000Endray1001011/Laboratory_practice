package com.kitisgang.lpz66466.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

public class FuelDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotNull(message = "Название топлива обязательно")
        private String name;

        @NotNull(message = "Цена за литр обязательна")
        @Positive(message = "Цена должна быть положительной")
        private Double costPerLiter;

        @NotNull(message = "Октановое число обязательно")
        @Positive(message = "Октановое число должно быть положительным")
        private Double octaneNumber;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String name;
        private Double costPerLiter;
        private Double octaneNumber;
        private Integer availablePumpsCount;
        private Double totalAvailableQuantity;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        private String name;
        @Positive(message = "Цена должна быть положительной")
        private Double costPerLiter;
        @Positive(message = "Октановое число должно быть положительным")
        private Double octaneNumber;
    }
}

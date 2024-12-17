package com.kitisgang.lpz66466.dto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

public class FuelPumpDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotNull(message = "Номер колонки обязателен")
        private String pumpNumber;

        @NotNull(message = "ID топлива обязательно")
        private Long fuelId;

        @NotNull(message = "Статус колонки обязателен")
        private Boolean status;

        @NotNull(message = "Количество топлива обязательно")
        @PositiveOrZero(message = "Количество топлива не может быть отрицательным")
        private Double fuelQuantity;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String pumpNumber;
        private String fuelName;
        private Double fuelCostPerLiter;
        private Boolean status;
        private Double fuelQuantity;
        private Boolean needsRefill;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        private String pumpNumber;
        private Long fuelId;
        private Boolean status;
        @PositiveOrZero(message = "Количество топлива не может быть отрицательным")
        private Double fuelQuantity;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RefillRequest {
        @NotNull(message = "Количество для пополнения обязательно")
        @Positive(message = "Количество для пополнения должно быть положительным")
        private Double amount;
    }

}

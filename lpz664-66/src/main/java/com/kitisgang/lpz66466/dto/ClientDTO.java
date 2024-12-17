package com.kitisgang.lpz66466.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

public class ClientDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotNull(message = "ФИО клиента обязательно")
        private String fullName;

        @NotNull(message = "Номер телефона обязателен")
        @Pattern(regexp = "^\\+?[1-9][0-9]{7,14}$", message = "Неверный формат номера телефона")
        private String phone;

        @Email(message = "Неверный формат email")
        private String email;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String fullName;
        private String phone;
        private String email;
        private LocalDateTime registrationDate;
        private Integer bonusBalance;
        private String clientStatus; // например: "REGULAR", "VIP", "NEW" в зависимости от бонусов
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        private String fullName;

        @Pattern(regexp = "^\\+?[1-9][0-9]{7,14}$", message = "Неверный формат номера телефона")
        private String phone;

        @Email(message = "Неверный формат email")
        private String email;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BonusPointsRequest {
        @NotNull(message = "Количество бонусных баллов обязательно")
        @PositiveOrZero(message = "Количество бонусных баллов не может быть отрицательным")
        private Integer points;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BonusHistoryResponse {
        private LocalDateTime date;
        private Integer points;
        private String operation; // "ADD" или "USE"
        private String description;
    }
}

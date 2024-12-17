package com.kitisgang.lpz66466.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "data_clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "full_name")
    private String fullName;

    @NotNull
    @Pattern(regexp = "^\\+?[1-9][0-9]{7,14}$")
    private String phone;

    @Email
    private String email;

    @Column(name = "date_registration")
    private LocalDateTime registrationDate;

    @PositiveOrZero
    @Column(name = "bonus_balance")
    private Integer bonusBalance;

    @PrePersist
    protected void onCreate() {
        registrationDate = LocalDateTime.now();
    }
}

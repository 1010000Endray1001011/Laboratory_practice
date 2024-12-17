package com.kitisgang.lpz66466.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "fuel_pumps")
public class FuelPump {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "pump_number")
    private String pumpNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fuel_id")
    @JsonBackReference
    private Fuel fuel;

    @NotNull
    private Boolean status;

    @NotNull
    @PositiveOrZero
    @Column(name = "fuel_quantity")
    private Double fuelQuantity;
}
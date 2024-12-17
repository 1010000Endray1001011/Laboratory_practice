package com.kitisgang.lpz66466.entity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "fuels")
public class Fuel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String name;

    @NotNull
    @Positive
    @Column(name = "cost_per_liter")
    private Double costPerLiter;

    @NotNull
    @Positive
    @Column(name = "octane_number")
    private Double octaneNumber;

    @OneToMany(mappedBy = "fuel", cascade = CascadeType.ALL) // Изменено здесь - mappedBy указывает на поле fuel в FuelPump
    @JsonManagedReference
    private List<FuelPump> fuelPumps;
}
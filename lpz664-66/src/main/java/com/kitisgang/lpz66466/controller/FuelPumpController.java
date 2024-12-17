package com.kitisgang.lpz66466.controller;

import com.kitisgang.lpz66466.dto.FuelPumpDTO;
import com.kitisgang.lpz66466.service.FuelPumpService;
import com.kitisgang.lpz66466.entity.FuelPump;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fuel-pumps")
@RequiredArgsConstructor
public class FuelPumpController {

    private final FuelPumpService fuelPumpService;

    @GetMapping
    public ResponseEntity<List<FuelPump>> getAllFuelPumps() {
        return ResponseEntity.ok(fuelPumpService.getAllFuelPumps());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuelPump> getFuelPumpById(@PathVariable Long id) {
        return ResponseEntity.ok(fuelPumpService.getFuelPumpById(id));
    }

    @PostMapping
    public ResponseEntity<FuelPump> createFuelPump(@Valid @RequestBody FuelPumpDTO.Request request) {
        return new ResponseEntity<>(fuelPumpService.createFuelPump(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FuelPump> updateFuelPump(@PathVariable Long id, @Valid @RequestBody FuelPump fuelPump) {
        return ResponseEntity.ok(fuelPumpService.updateFuelPump(id, fuelPump));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFuelPump(@PathVariable Long id) {
        fuelPumpService.deleteFuelPump(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/refill")
    public ResponseEntity<FuelPump> refillPump(
            @PathVariable Long id,
            @RequestParam Double amount) {
        return ResponseEntity.ok(fuelPumpService.refillPump(id, amount));
    }

    @GetMapping("/low-fuel")
    public ResponseEntity<List<FuelPump>> getPumpsWithLowFuel() {
        return ResponseEntity.ok(fuelPumpService.getPumpsWithLowFuel());
    }
}

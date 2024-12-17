package com.kitisgang.lpz66466.controller;

import com.kitisgang.lpz66466.entity.Fuel;
import com.kitisgang.lpz66466.service.FuelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/fuels")
@RequiredArgsConstructor
public class FuelController {

    private final FuelService fuelService;

    @GetMapping
    public ResponseEntity<List<Fuel>> getAllFuels() {
        return ResponseEntity.ok(fuelService.getAllFuels());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Fuel> getFuelById(@PathVariable Long id) {
        return ResponseEntity.ok(fuelService.getFuelById(id));
    }

    @PostMapping
    public ResponseEntity<Fuel> createFuel(@Valid @RequestBody Fuel fuel) {
        return new ResponseEntity<>(fuelService.createFuel(fuel), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Fuel> updateFuel(@PathVariable Long id, @Valid @RequestBody Fuel fuel) {
        return ResponseEntity.ok(fuelService.updateFuel(id, fuel));
    }

    @GetMapping("/by-octane")
    public ResponseEntity<List<Fuel>> getFuelsByOctaneNumber(@RequestParam Double octaneNumber) {
        return ResponseEntity.ok(fuelService.getFuelsByOctaneNumber(octaneNumber));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFuel(@PathVariable Long id) {
        fuelService.deleteFuel(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<Fuel>> getFuelsByPriceRange(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice) {
        return ResponseEntity.ok(fuelService.getFuelsByPriceRange(minPrice, maxPrice));
    }

    @GetMapping("/inventory-status")
    public ResponseEntity<Map<String, Double>> getFuelInventoryStatus() {
        return ResponseEntity.ok(fuelService.getFuelInventoryStatus());
    }
}

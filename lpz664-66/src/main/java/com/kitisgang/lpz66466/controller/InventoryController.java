package com.kitisgang.lpz66466.controller;

import com.kitisgang.lpz66466.service.InventoryService;
import com.kitisgang.lpz66466.entity.Inventory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<List<Inventory>> getAllInventory() {
        return ResponseEntity.ok(inventoryService.getAllInventory());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventory> getInventoryById(@PathVariable Long id) {
        return ResponseEntity.ok(inventoryService.getInventoryById(id));
    }

    @PostMapping
    public ResponseEntity<Inventory> createInventory(@Valid @RequestBody Inventory inventory) {
        return new ResponseEntity<>(inventoryService.createInventory(inventory), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inventory> updateInventory(@PathVariable Long id, @Valid @RequestBody Inventory inventory) {
        return ResponseEntity.ok(inventoryService.updateInventory(id, inventory));
    }

    @PatchMapping("/{id}/adjust")
    public ResponseEntity<Inventory> adjustQuantity(
            @PathVariable Long id,
            @RequestParam int adjustment) {
        return ResponseEntity.ok(inventoryService.adjustQuantity(id, adjustment));
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<Inventory>> getLowStockItems() {
        return ResponseEntity.ok(inventoryService.getLowStockItems());
    }

    @GetMapping("/recent-updates")
    public ResponseEntity<List<Inventory>> getRecentlyUpdated(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime since) {
        return ResponseEntity.ok(inventoryService.getRecentlyUpdated(since));
    }
}
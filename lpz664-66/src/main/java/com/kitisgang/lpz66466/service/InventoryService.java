package com.kitisgang.lpz66466.service;

import com.kitisgang.lpz66466.entity.Inventory;
import com.kitisgang.lpz66466.repository.InventoryRepository;
import com.kitisgang.lpz66466.exeption.ResourceNotFoundException;
import com.kitisgang.lpz66466.exeption.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private static final int MIN_QUANTITY_THRESHOLD = 10;

    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    public Inventory getInventoryById(Long id) {
        return inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Товар не найден с ID: " + id));
    }

    @Transactional
    public Inventory createInventory(Inventory inventory) {
        if (inventoryRepository.existsByName(inventory.getName())) {
            throw new BusinessException("Товар с таким названием уже существует");
        }
        return inventoryRepository.save(inventory);
    }

    @Transactional
    public Inventory updateInventory(Long id, Inventory inventoryDetails) {
        Inventory inventory = getInventoryById(id);

        if (!inventory.getName().equals(inventoryDetails.getName()) &&
                inventoryRepository.existsByName(inventoryDetails.getName())) {
            throw new BusinessException("Товар с таким названием уже существует");
        }

        inventory.setName(inventoryDetails.getName());
        inventory.setQuantity(inventoryDetails.getQuantity());

        return inventoryRepository.save(inventory);
    }

    @Transactional
    public Inventory adjustQuantity(Long id, int adjustment) {
        Inventory inventory = getInventoryById(id);
        int newQuantity = inventory.getQuantity() + adjustment;

        if (newQuantity < 0) {
            throw new BusinessException("Количество товара не может быть отрицательным");
        }

        inventory.setQuantity(newQuantity);
        return inventoryRepository.save(inventory);
    }

    public List<Inventory> getLowStockItems() {
        return inventoryRepository.findItemsNeedingRestock(MIN_QUANTITY_THRESHOLD);
    }

    public List<Inventory> getRecentlyUpdated(LocalDateTime since) {
        return inventoryRepository.findByLastUpdatedAfter(since);
    }
}

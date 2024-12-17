package com.kitisgang.lpz66466.repository;


import com.kitisgang.lpz66466.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    // Поиск по названию товара
    Optional<Inventory> findByName(String name);
    // Поиск товаров с количеством меньше указанного
    List<Inventory> findByQuantityLessThan(Integer quantity);
    // Поиск товаров, обновленных после указанной даты
    List<Inventory> findByLastUpdatedAfter(LocalDateTime date);
    // Поиск товаров с нулевым количеством
    List<Inventory> findByQuantityEquals(Integer quantity);
    // Получение товаров, требующих пополнения (меньше минимального количества)
    @Query("SELECT i FROM Inventory i WHERE i.quantity < :minQuantity")
    List<Inventory> findItemsNeedingRestock(@Param("minQuantity") Integer minQuantity);
    // Проверка существования товара по имени
    boolean existsByName(String name);
}

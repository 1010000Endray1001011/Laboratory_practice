package com.kitisgang.lpz66466.repository;


import com.kitisgang.lpz66466.entity.FuelPump;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FuelPumpRepository extends JpaRepository<FuelPump, Long> {
    // Поиск колонок по идентификатору топлива
    List<FuelPump> findByFuelId(Long fuelId);
    // Поиск работающих колонок
    List<FuelPump> findByStatus(Boolean status);
    // Поиск колонок по номеру
    Optional<FuelPump> findByPumpNumber(String pumpNumber);
    // Поиск колонок с количеством топлива меньше указанного
    List<FuelPump> findByFuelQuantityLessThan(Double quantity);
    // Подсчет количества работающих колонок для конкретного типа топлива
    @Query("SELECT COUNT(fp) FROM FuelPump fp WHERE fp.fuel.id = :fuelId AND fp.status = true")
    Long countActivePumpsByFuelId(@Param("fuelId") Long fuelId);
    // Получение колонок с критически низким уровнем топлива
    @Query("SELECT fp FROM FuelPump fp WHERE fp.fuelQuantity < :threshold AND fp.status = true")
    List<FuelPump> findPumpsWithLowFuel(@Param("threshold") Double threshold);
}

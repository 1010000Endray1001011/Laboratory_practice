package com.kitisgang.lpz66466.repository;

import com.kitisgang.lpz66466.entity.Fuel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FuelRepository extends JpaRepository<Fuel, Long> {

    Optional<Fuel> findById(Long id);
    // Поиск топлива по названию
    Optional<Fuel> findByName(String name);
    // Поиск топлива по октановому числу
    List<Fuel> findByOctaneNumber(Double octaneNumber);
    // Поиск топлива дешевле указанной цены
    List<Fuel> findByCostPerLiterLessThan(Double price);
    // Поиск топлива в диапазоне цен
    List<Fuel> findByCostPerLiterBetween(Double minPrice, Double maxPrice);
    // Пользовательский запрос для получения топлива с количеством на всех колонках
    @Query("SELECT f, SUM(fp.fuelQuantity) as totalQuantity " +
            "FROM Fuel f LEFT JOIN f.fuelPumps fp " +
            "GROUP BY f.id")
    List<Object[]> findFuelsWithTotalQuantity();
    // Проверка существования топлива по имени
    boolean existsByName(String name);
}

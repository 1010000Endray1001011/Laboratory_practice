package com.kitisgang.lpz66466.service;

import com.kitisgang.lpz66466.entity.Fuel;
import com.kitisgang.lpz66466.repository.FuelRepository;
import com.kitisgang.lpz66466.exeption.ResourceNotFoundException;
import com.kitisgang.lpz66466.exeption.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FuelService {

    private final FuelRepository fuelRepository;

    public List<Fuel> getAllFuels() {
        return fuelRepository.findAll();
    }

    public List<Fuel> getFuelsByOctaneNumber(Double octaneNumber) {
        return fuelRepository.findByOctaneNumber(octaneNumber);
    }


    public Fuel getFuelById(Long id) {
        return fuelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Топливо не найдено с ID: " + id));
    }

    @Transactional
    public Fuel createFuel(Fuel fuel) {
        if (fuelRepository.existsByName(fuel.getName())) {
            throw new BusinessException("Топливо с таким названием уже существует");
        }
        return fuelRepository.save(fuel);
    }

    @Transactional
    public Fuel updateFuel(Long id, Fuel fuelDetails) {
        Fuel fuel = getFuelById(id);

        if (!fuel.getName().equals(fuelDetails.getName()) &&
                fuelRepository.existsByName(fuelDetails.getName())) {
            throw new BusinessException("Топливо с таким названием уже существует");
        }

        fuel.setName(fuelDetails.getName());
        fuel.setCostPerLiter(fuelDetails.getCostPerLiter());
        fuel.setOctaneNumber(fuelDetails.getOctaneNumber());

        return fuelRepository.save(fuel);
    }

    @Transactional
    public void deleteFuel(Long id) {
        Fuel fuel = getFuelById(id);
        if (!fuel.getFuelPumps().isEmpty()) {
            throw new BusinessException("Невозможно удалить топливо, которое используется в колонках");
        }
        fuelRepository.delete(fuel);
    }

    public List<Fuel> getFuelsByPriceRange(Double minPrice, Double maxPrice) {
        return fuelRepository.findByCostPerLiterBetween(minPrice, maxPrice);
    }

    public Map<String, Double> getFuelInventoryStatus() {
        return fuelRepository.findFuelsWithTotalQuantity().stream()
                .collect(Collectors.toMap(
                        obj -> ((Fuel)obj[0]).getName(),
                        obj -> (Double)obj[1]
                ));
    }
}

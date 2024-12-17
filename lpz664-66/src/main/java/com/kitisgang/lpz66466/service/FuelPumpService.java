package com.kitisgang.lpz66466.service;
import com.kitisgang.lpz66466.dto.FuelPumpDTO;
import com.kitisgang.lpz66466.entity.Fuel;
import com.kitisgang.lpz66466.entity.FuelPump;
import com.kitisgang.lpz66466.repository.FuelPumpRepository;
import com.kitisgang.lpz66466.exeption.ResourceNotFoundException;
import com.kitisgang.lpz66466.exeption.BusinessException;
import com.kitisgang.lpz66466.repository.FuelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FuelPumpService {

    private final FuelRepository fuelRepository;
    private final FuelPumpRepository fuelPumpRepository;
    private final FuelService fuelService;

    private static final double LOW_FUEL_THRESHOLD = 100.0; // литров

    public List<FuelPump> getAllFuelPumps() {
        return fuelPumpRepository.findAll();
    }

    public FuelPump getFuelPumpById(Long id) {
        return fuelPumpRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Топливная колонка не найдена с ID: " + id));
    }

    @Transactional
    public FuelPump createFuelPump(FuelPumpDTO.Request request) {
        Fuel fuel = fuelRepository.findById(request.getFuelId())
                .orElseThrow(() -> new RuntimeException("Топливо не найдено"));

        FuelPump fuelPump = new FuelPump();
        fuelPump.setPumpNumber(request.getPumpNumber());
        fuelPump.setFuel(fuel);
        fuelPump.setStatus(request.getStatus());
        fuelPump.setFuelQuantity(request.getFuelQuantity());

        return fuelPumpRepository.save(fuelPump);
    }

    @Transactional
    public FuelPump updateFuelPump(Long id, FuelPump pumpDetails) {
        FuelPump pump = getFuelPumpById(id);
        validateFuelPump(pumpDetails);

        pump.setPumpNumber(pumpDetails.getPumpNumber());
        pump.setFuel(pumpDetails.getFuel());
        pump.setStatus(pumpDetails.getStatus());
        pump.setFuelQuantity(pumpDetails.getFuelQuantity());

        return fuelPumpRepository.save(pump);
    }

    @Transactional
    public void deleteFuelPump(Long id) {
        FuelPump pump = getFuelPumpById(id);
        fuelPumpRepository.delete(pump);
    }

    @Transactional
    public FuelPump refillPump(Long id, Double amount) {
        FuelPump pump = getFuelPumpById(id);
        pump.setFuelQuantity(pump.getFuelQuantity() + amount);
        return fuelPumpRepository.save(pump);
    }

    public List<FuelPump> getPumpsWithLowFuel() {
        return fuelPumpRepository.findPumpsWithLowFuel(LOW_FUEL_THRESHOLD);
    }

    private void validateFuelPump(FuelPump pump) {
        if (pump.getFuelQuantity() < 0) {
            throw new BusinessException("Количество топлива не может быть отрицательным");
        }
        fuelService.getFuelById(pump.getFuel().getId()); // проверяем существование топлива
    }
}
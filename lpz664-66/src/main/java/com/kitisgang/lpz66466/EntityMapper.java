package com.kitisgang.lpz66466;

import com.kitisgang.lpz66466.dto.InventoryDTO;
import com.kitisgang.lpz66466.dto.ClientDTO;
import com.kitisgang.lpz66466.dto.FuelDTO;
import com.kitisgang.lpz66466.dto.FuelPumpDTO;
import com.kitisgang.lpz66466.entity.Fuel;
import com.kitisgang.lpz66466.entity.Client;
import com.kitisgang.lpz66466.entity.FuelPump;
import com.kitisgang.lpz66466.entity.Inventory;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EntityMapper {

    // Fuel mappings
    Fuel toEntity(FuelDTO.Request dto);

    @Mapping(target = "availablePumpsCount", expression = "java(fuel.getFuelPumps().size())")
    @Mapping(target = "totalAvailableQuantity", expression = "java(fuel.getFuelPumps().stream().mapToDouble(FuelPump::getFuelQuantity).sum())")
    FuelDTO.Response toDto(Fuel fuel);

    void updateFuelFromDto(FuelDTO.UpdateRequest dto, @MappingTarget Fuel fuel);

    // FuelPump mappings
    @Mapping(target = "fuel.id", source = "fuelId")
    FuelPump toEntity(FuelPumpDTO.Request dto);

    @Mapping(target = "fuelName", source = "fuel.name")
    @Mapping(target = "fuelCostPerLiter", source = "fuel.costPerLiter")
    @Mapping(target = "needsRefill", expression = "java(fuelPump.getFuelQuantity() < 100.0)")
    FuelPumpDTO.Response toDto(FuelPump fuelPump);

    void updateFuelPumpFromDto(FuelPumpDTO.UpdateRequest dto, @MappingTarget FuelPump fuelPump);

    // Inventory mappings
    Inventory toEntity(InventoryDTO.Request dto);

    @Mapping(target = "lowStock", expression = "java(inventory.getQuantity() < 10)")
    InventoryDTO.Response toDto(Inventory inventory);

    //Client mapping
    void updateInventoryFromDto(InventoryDTO.UpdateRequest dto, @MappingTarget Inventory inventory);
    Client toEntity(ClientDTO.Request dto);

    @Mapping(target = "clientStatus", expression = "java(determineClientStatus(client.getBonusBalance()))")
    ClientDTO.Response toDto(Client client);

    void updateClientFromDto(ClientDTO.UpdateRequest dto, @MappingTarget Client client);

    // Helper method for determining client status
    default String determineClientStatus(Integer bonusBalance) {
        if (bonusBalance == null) return "NEW";
        if (bonusBalance >= 1000) return "VIP";
        if (bonusBalance >= 100) return "REGULAR";
        return "NEW";
    }
}

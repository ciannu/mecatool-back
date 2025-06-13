package com.svf.mecatool.integration.mappers;

import com.svf.mecatool.integration.model.Vehicle;
import com.svf.mecatool.presentation.dto.ClientDTO;
import com.svf.mecatool.presentation.dto.VehicleDTO;

public class VehicleMapper {

    public static Vehicle toEntity(VehicleDTO dto) {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(dto.getId());
        vehicle.setLicensePlate(dto.getLicensePlate());
        vehicle.setBrand(dto.getBrand());
        vehicle.setModel(dto.getModel());
        vehicle.setYear(dto.getYear());
        vehicle.setFuelType(dto.getFuelType());
        vehicle.setVin(dto.getVin());
        vehicle.setColor(dto.getColor());
        vehicle.setMileage(dto.getMileage());
        vehicle.setCreatedAt(dto.getCreatedAt());
        vehicle.setUpdatedAt(dto.getUpdatedAt());

        return vehicle;
    }

    public static VehicleDTO toDTO(Vehicle entity) {
        VehicleDTO dto = new VehicleDTO();
        dto.setId(entity.getId());
        dto.setLicensePlate(entity.getLicensePlate());
        dto.setBrand(entity.getBrand());
        dto.setModel(entity.getModel());
        dto.setYear(entity.getYear());
        dto.setFuelType(entity.getFuelType());
        dto.setVin(entity.getVin());
        dto.setColor(entity.getColor());
        dto.setMileage(entity.getMileage());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        if (entity.getClient() != null) {
            ClientDTO clientDTO = new ClientDTO();
            clientDTO.setId(entity.getClient().getId());
            clientDTO.setName(entity.getClient().getFirstName());
            dto.setClient(clientDTO);
        }

        return dto;
    }
}

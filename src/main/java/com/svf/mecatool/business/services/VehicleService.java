package com.svf.mecatool.business.services;

import com.svf.mecatool.integration.model.Vehicle;
import com.svf.mecatool.presentation.dto.VehicleDTO;

import java.util.List;
import java.util.Optional;

public interface VehicleService {
    VehicleDTO createVehicle(VehicleDTO vehicleDTO);
    Optional<VehicleDTO> getVehicleById(Long id);
    List<VehicleDTO> getAllVehicles();
    boolean deleteVehicle(Long id);
    VehicleDTO updateVehicle(Long id, VehicleDTO dto);
    List<VehicleDTO> getVehiclesByClient(Long clientId);
    List<VehicleDTO> searchVehicles(String query);
    long getTotalVehicles();
    Optional<VehicleDTO> getLatestVehicle();
    // get vehicles by client id and search vehicles methods
}

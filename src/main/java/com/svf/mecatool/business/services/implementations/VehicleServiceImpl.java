package com.svf.mecatool.business.services.implementations;

import com.svf.mecatool.business.services.VehicleService;
import com.svf.mecatool.integration.mappers.VehicleMapper;
import com.svf.mecatool.integration.model.Client;
import com.svf.mecatool.integration.model.Vehicle;
import com.svf.mecatool.integration.repositories.ClientRepository;
import com.svf.mecatool.integration.repositories.VehicleRepository;
import com.svf.mecatool.presentation.dto.VehicleDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VehicleServiceImpl implements VehicleService {
    private final VehicleRepository vehicleRepository;
    private final ClientRepository clientRepository;

    public VehicleServiceImpl(VehicleRepository vehicleRepository, ClientRepository clientRepository) {
        this.vehicleRepository = vehicleRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public VehicleDTO createVehicle(VehicleDTO dto) {
        Vehicle vehicle = VehicleMapper.toEntity(dto);

        Long clientId = dto.getClient().getId();
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        vehicle.setClient(client);

        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        return VehicleMapper.toDTO(savedVehicle);
    }

    @Override
    public Optional<VehicleDTO> getVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .map(VehicleMapper::toDTO);
    }

    @Override
    public List<VehicleDTO> getAllVehicles() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        return vehicles.stream()
                .map(VehicleMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteVehicle(Long id) {
        if (!vehicleRepository.existsById(id)) {
            return false;
        }
        vehicleRepository.deleteById(id);
        return true;
    }

    @Override
    public VehicleDTO updateVehicle(Long id, VehicleDTO dto) {
        Vehicle existingVehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        Long clientId = dto.getClient().getId();
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        existingVehicle.setClient(client);
        existingVehicle.setLicensePlate(dto.getLicensePlate());
        existingVehicle.setBrand(dto.getBrand());
        existingVehicle.setModel(dto.getModel());
        existingVehicle.setYear(dto.getYear());
        existingVehicle.setFuelType(dto.getFuelType());
        existingVehicle.setVin(dto.getVin());
        existingVehicle.setColor(dto.getColor());
        existingVehicle.setMileage(dto.getMileage());

        Vehicle savedVehicle = vehicleRepository.save(existingVehicle);

        return VehicleMapper.toDTO(savedVehicle);
    }

    @Override
    public List<VehicleDTO> getVehiclesByClient(Long clientId) {
        if (!clientRepository.existsById(clientId)) {
            throw new RuntimeException("Client not found");
        }
        List<Vehicle> vehicles = vehicleRepository.findByClientId(clientId);
        return vehicles.stream()
                .map(VehicleMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<VehicleDTO> searchVehicles(String query) {
        List<Vehicle> vehicles = vehicleRepository.searchVehicles(query);
        return vehicles.stream()
                .map(VehicleMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public long getTotalVehicles() {
        return vehicleRepository.count();
    }

    @Override
    public Optional<VehicleDTO> getLatestVehicle() {
        return vehicleRepository.findTopByOrderByIdDesc()
                .map(VehicleMapper::toDTO);
    }
}

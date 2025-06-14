package com.svf.mecatool.presentation.controller;

import com.svf.mecatool.business.services.VehicleService;
import com.svf.mecatool.integration.model.Vehicle;
import com.svf.mecatool.presentation.dto.VehicleDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.security.access.prepost.PreAuthorize;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public ResponseEntity<List<VehicleDTO>> getAllVehicles() {
        List<VehicleDTO> vehicles = vehicleService.getAllVehicles();
        return ResponseEntity.ok(vehicles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleDTO> getVehicleById(@PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }

        Optional<VehicleDTO> vehicle = vehicleService.getVehicleById(id);
        return vehicle.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody VehicleDTO vehicleDTO, UriComponentsBuilder ucb) {
        VehicleDTO createdVehicle = vehicleService.createVehicle(vehicleDTO);
        Long id = createdVehicle.getId();
        URI uri = ucb.path("/vehicles/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleDTO> updateVehicle(@PathVariable Long id, @RequestBody VehicleDTO vehicleDTO) {
        System.out.println("PUT id path: " + id);
        System.out.println("PUT id body: " + vehicleDTO.getId());


        if (id == null || !id.equals(vehicleDTO.getId())) {
            return ResponseEntity.badRequest().build();
        }

        try {
            VehicleDTO updatedVehicle = vehicleService.updateVehicle(id, vehicleDTO);
            return ResponseEntity.ok(updatedVehicle);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }

        boolean deleted = vehicleService.deleteVehicle(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/by-client/{clientId}")
    public ResponseEntity<List<VehicleDTO>> getVehiclesByClient(@PathVariable Long clientId) {
        if (clientId == null) {
            return ResponseEntity.badRequest().build();
        }

        List<VehicleDTO> vehicles = vehicleService.getVehiclesByClient(clientId);
        return ResponseEntity.ok(vehicles);
    }

    @GetMapping("/search")
    public ResponseEntity<List<VehicleDTO>> searchVehicles(@RequestParam(required = false) String query) {
        List<VehicleDTO> vehicles = vehicleService.searchVehicles(query != null ? query : "");
        return ResponseEntity.ok(vehicles);
    }

    @GetMapping("/total")
    @PreAuthorize("hasAnyRole('ADMIN', 'MECHANIC')")
    public ResponseEntity<Long> getTotalVehicles() {
        long totalVehicles = vehicleService.getTotalVehicles();
        return ResponseEntity.ok(totalVehicles);
    }

    @GetMapping("/latest")
    @PreAuthorize("hasAnyRole('ADMIN', 'MECHANIC')")
    public ResponseEntity<VehicleDTO> getLatestVehicle() {
        Optional<VehicleDTO> latestVehicle = vehicleService.getLatestVehicle();
        return latestVehicle.map(ResponseEntity::ok)
                            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}

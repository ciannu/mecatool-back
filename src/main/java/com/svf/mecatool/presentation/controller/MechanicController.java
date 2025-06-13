package com.svf.mecatool.presentation.controller;

import com.svf.mecatool.business.services.MechanicService;
import com.svf.mecatool.integration.model.Mechanic;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mechanics")
@CrossOrigin
public class MechanicController {

    private final MechanicService mechanicService;

    public MechanicController(MechanicService mechanicService) {
        this.mechanicService = mechanicService;
    }

    @GetMapping
    public List<Mechanic> getAllMechanics() {
        return mechanicService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mechanic> getMechanicById(@PathVariable Long id) {
        return mechanicService.getMechanicById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Mechanic> createMechanic(@RequestBody Mechanic mechanic) {
        Mechanic created = mechanicService.create(mechanic);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mechanic> updateMechanic(@PathVariable Long id, @RequestBody Mechanic mechanicDetails) {
        return mechanicService.getMechanicById(id)
                .map(existing -> {
                    existing.setFirstName(mechanicDetails.getFirstName());
                    existing.setLastName(mechanicDetails.getLastName());
                    existing.setEmail(mechanicDetails.getEmail());
                    existing.setPhone(mechanicDetails.getPhone());
                    return ResponseEntity.ok(mechanicService.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMechanic(@PathVariable Long id) {
        mechanicService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/by-ids")
    public ResponseEntity<List<Mechanic>> getMechanicsByIds(@RequestBody List<Long> ids) {
        List<Mechanic> mechanics = mechanicService.getMechanicsByIds(ids);
        return ResponseEntity.ok(mechanics);
    }
}

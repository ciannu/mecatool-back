package com.svf.mecatool.presentation.controller;

import com.svf.mecatool.business.services.InventoryItemService;
import com.svf.mecatool.presentation.dto.InventoryItemDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/inventory-items")
@CrossOrigin
public class InventoryItemController {

    private final InventoryItemService inventoryItemService;

    public InventoryItemController(InventoryItemService inventoryItemService) {
        this.inventoryItemService = inventoryItemService;
    }

    @GetMapping
    public List<InventoryItemDTO> getAll() {
        return inventoryItemService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryItemDTO> getById(@PathVariable Long id) {
        Optional<InventoryItemDTO> dto = inventoryItemService.getById(id);
        return ResponseEntity.of(dto);
    }

    @PostMapping
    public ResponseEntity<InventoryItemDTO> create(@Valid @RequestBody InventoryItemDTO dto) {
        InventoryItemDTO created = inventoryItemService.create(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryItemDTO> update(@PathVariable Long id, @Valid @RequestBody InventoryItemDTO dto) {
        if (!id.equals(dto.getId())) {
            return ResponseEntity.badRequest().build();
        }
        InventoryItemDTO updated = inventoryItemService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        inventoryItemService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/decrease-stock")
    public ResponseEntity<?> decreaseStock(
            @PathVariable Long id,
            @RequestParam Integer quantity
    ) {
        try {
            InventoryItemDTO updated = inventoryItemService.decreaseStock(id, quantity);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
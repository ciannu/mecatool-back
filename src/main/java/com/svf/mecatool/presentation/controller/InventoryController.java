package com.svf.mecatool.presentation.controller;

import com.svf.mecatool.business.services.InventoryService;
import com.svf.mecatool.presentation.dto.InventoryItemDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "http://localhost:4200")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<List<InventoryItemDTO>> getAllItems() {
        System.out.println("Getting all inventory items");
        return ResponseEntity.ok(inventoryService.getAllItems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryItemDTO> getItemById(@PathVariable Long id) {
        System.out.println("Getting inventory item with ID: " + id);
        InventoryItemDTO item = inventoryService.getItemById(id);
        if (item == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(item);
    }

    @PostMapping
    public ResponseEntity<InventoryItemDTO> createItem(@Valid @RequestBody InventoryItemDTO itemDTO) {
        System.out.println("Creating new inventory item: " + itemDTO);
        return ResponseEntity.ok(inventoryService.createItem(itemDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryItemDTO> updateItem(@PathVariable Long id, @Valid @RequestBody InventoryItemDTO itemDTO) {
        System.out.println("Updating inventory item with ID: " + id);
        System.out.println("Received DTO: " + itemDTO);
        try {
            InventoryItemDTO updatedItem = inventoryService.updateItem(id, itemDTO);
            System.out.println("Updated item: " + updatedItem);
            return ResponseEntity.ok(updatedItem);
        } catch (Exception e) {
            System.err.println("Error updating item: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        System.out.println("Deleting inventory item with ID: " + id);
        inventoryService.deleteItem(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<InventoryItemDTO>> getItemsByCategory(@PathVariable String category) {
        System.out.println("Getting items by category: " + category);
        return ResponseEntity.ok(inventoryService.getItemsByCategory(category));
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<InventoryItemDTO>> getLowStockItems() {
        System.out.println("Getting low stock items");
        return ResponseEntity.ok(inventoryService.getLowStockItems());
    }
} 
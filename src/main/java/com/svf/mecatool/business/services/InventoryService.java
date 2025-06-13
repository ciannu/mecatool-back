package com.svf.mecatool.business.services;

import com.svf.mecatool.integration.model.InventoryItem;
import com.svf.mecatool.presentation.dto.InventoryItemDTO;

import java.util.List;

public interface InventoryService {
    List<InventoryItemDTO> getAllItems();
    InventoryItemDTO getItemById(Long id);
    InventoryItemDTO createItem(InventoryItemDTO item);
    InventoryItemDTO updateItem(Long id, InventoryItemDTO item);
    void deleteItem(Long id);
    List<InventoryItemDTO> getItemsByCategory(String category);
    List<InventoryItemDTO> getLowStockItems();
    
    // New methods for stock management
    boolean hasEnoughStock(Long itemId, int quantity);
    void decreaseStock(Long itemId, int quantity);
    void increaseStock(Long itemId, int quantity);
    int getAvailableStock(Long itemId);
} 
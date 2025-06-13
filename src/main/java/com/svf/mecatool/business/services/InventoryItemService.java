package com.svf.mecatool.business.services;

import com.svf.mecatool.presentation.dto.InventoryItemDTO;

import java.util.List;
import java.util.Optional;

public interface InventoryItemService {
    List<InventoryItemDTO> getAll();
    Optional<InventoryItemDTO> getById(Long id);
    InventoryItemDTO create(InventoryItemDTO dto);
    InventoryItemDTO update(Long id, InventoryItemDTO dto);
    void delete(Long id);

    InventoryItemDTO decreaseStock(Long id, Integer quantity);
}

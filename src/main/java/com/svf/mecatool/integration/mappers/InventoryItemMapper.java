package com.svf.mecatool.integration.mappers;

import com.svf.mecatool.integration.model.InventoryItem;
import com.svf.mecatool.presentation.dto.InventoryItemDTO;

public class InventoryItemMapper {

    public static InventoryItem toEntity(InventoryItemDTO dto) {
        if (dto == null) {
            return null;
        }
        InventoryItem entity = new InventoryItem();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setPrice(dto.getPrice());
        entity.setQuantity(dto.getQuantity());
        entity.setDescription(dto.getDescription());
        entity.setCategory(dto.getCategory());
        entity.setMinStock(dto.getMinStock());
        return entity;
    }

    public static InventoryItemDTO toDTO(InventoryItem entity) {
        if (entity == null) {
            return null;
        }
        InventoryItemDTO dto = new InventoryItemDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setPrice(entity.getPrice());
        dto.setQuantity(entity.getQuantity());
        dto.setDescription(entity.getDescription());
        dto.setCategory(entity.getCategory());
        dto.setMinStock(entity.getMinStock());
        return dto;
    }
} 
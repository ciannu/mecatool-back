package com.svf.mecatool.integration.mappers;

import com.svf.mecatool.integration.model.WorkOrderItem;
import com.svf.mecatool.integration.model.InventoryItem;
import com.svf.mecatool.presentation.dto.WorkOrderItemDTO;
import com.svf.mecatool.presentation.dto.InventoryItemDTO;

public class WorkOrderItemMapper {

    public static WorkOrderItem toEntity(WorkOrderItemDTO dto) {
        if (dto == null) {
            return null;
        }
        WorkOrderItem entity = new WorkOrderItem();
        entity.setId(dto.getId());
        entity.setQuantity(dto.getQuantity());
        entity.setPrice(dto.getPrice());

        if (dto.getInventoryItem() != null) {
            entity.setInventoryItem(InventoryItemMapper.toEntity(dto.getInventoryItem()));
        }

        return entity;
    }

    public static WorkOrderItemDTO toDTO(WorkOrderItem entity) {
        if (entity == null) {
            return null;
        }
        WorkOrderItemDTO dto = new WorkOrderItemDTO();
        dto.setId(entity.getId());
        dto.setQuantity(entity.getQuantity());
        dto.setPrice(entity.getPrice());
        dto.setWorkOrderId(entity.getWorkOrder() != null ? entity.getWorkOrder().getId() : null);

        if (entity.getInventoryItem() != null) {
            dto.setInventoryItem(InventoryItemMapper.toDTO(entity.getInventoryItem()));
        }

        return dto;
    }
} 
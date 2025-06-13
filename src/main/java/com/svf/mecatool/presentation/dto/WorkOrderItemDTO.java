package com.svf.mecatool.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkOrderItemDTO {
    private Long id;
    private Long workOrderId;
    private Long inventoryItemId;
    private Integer quantity;
    private BigDecimal price;
    private InventoryItemDTO inventoryItem;
} 
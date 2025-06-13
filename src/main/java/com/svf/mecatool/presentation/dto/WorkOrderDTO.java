package com.svf.mecatool.presentation.dto;

import com.svf.mecatool.integration.model.WorkOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkOrderDTO {
    private Long id;
    private Long vehicleId;
    private String description;
    private WorkOrderStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal total;
    private List<Long> mechanicIds;
    private List<WorkOrderItemDTO> workOrderItems;
}

package com.svf.mecatool.business.services;

import com.svf.mecatool.integration.model.WorkOrderStatus;
import com.svf.mecatool.presentation.dto.WorkOrderDTO;
import com.svf.mecatool.presentation.dto.WorkOrderItemDTO;

import java.util.List;

public interface WorkOrderService {

    WorkOrderDTO createWorkOrder(WorkOrderDTO dto);
    WorkOrderDTO updateWorkOrder(Long id, WorkOrderDTO dto);
    void deleteWorkOrder(Long id);
    List<WorkOrderDTO> getAllWorkOrders();
    WorkOrderDTO getWorkOrderById(Long id);
    List<WorkOrderDTO> filterWorkOrders(WorkOrderStatus status, Long vehicleId, Long mechanicId);

    WorkOrderDTO updateStatus(Long id, WorkOrderStatus newStatus);
    WorkOrderDTO assignMechanics(Long id, List<Long> mechanicIds);

    // New method for client history
    List<WorkOrderDTO> getWorkOrdersByClientId(Long clientId);

    // Work Order Items methods
    List<WorkOrderItemDTO> getWorkOrderItems(Long workOrderId);
    WorkOrderItemDTO addWorkOrderItem(Long workOrderId, WorkOrderItemDTO item);
    WorkOrderItemDTO updateWorkOrderItem(Long workOrderId, Long itemId, WorkOrderItemDTO item);
    void deleteWorkOrderItem(Long workOrderId, Long itemId);
}

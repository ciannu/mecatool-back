package com.svf.mecatool.presentation.controller;

import com.svf.mecatool.business.services.WorkOrderService;
import com.svf.mecatool.integration.model.WorkOrderStatus;
import com.svf.mecatool.presentation.dto.WorkOrderDTO;
import com.svf.mecatool.presentation.dto.WorkOrderItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/work-orders")
@CrossOrigin
public class WorkOrderController {

    @Autowired
    private WorkOrderService workOrderService;

    @GetMapping
    public List<WorkOrderDTO> getAll() {
        return workOrderService.getAllWorkOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkOrderDTO> getById(@PathVariable Long id) {
        return ResponseEntity.of(Optional.ofNullable(workOrderService.getWorkOrderById(id)));
    }

    @PostMapping
    public ResponseEntity<WorkOrderDTO> create(@RequestBody WorkOrderDTO dto) {
        return ResponseEntity.ok(workOrderService.createWorkOrder(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkOrderDTO> update(@PathVariable Long id, @RequestBody WorkOrderDTO dto) {
        if (!id.equals(dto.getId())) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(workOrderService.updateWorkOrder(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        workOrderService.deleteWorkOrder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<List<WorkOrderDTO>> filterWorkOrders(
            @RequestParam(required = false) WorkOrderStatus status,
            @RequestParam(required = false) Long vehicleId,
            @RequestParam(required = false) Long mechanicId
    ) {
        List<WorkOrderDTO> filtered = workOrderService.filterWorkOrders(status, vehicleId, mechanicId);
        return ResponseEntity.ok(filtered);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<WorkOrderDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam WorkOrderStatus status
    ) {
        return ResponseEntity.ok(workOrderService.updateStatus(id, status));
    }

    @PatchMapping("/{id}/mechanics")
    public ResponseEntity<WorkOrderDTO> assignMechanics(
            @PathVariable Long id,
            @RequestBody List<Long> mechanicIds
    ) {
        return ResponseEntity.ok(workOrderService.assignMechanics(id, mechanicIds));
    }

    // Work Order Items endpoints
    @GetMapping("/{id}/items")
    public ResponseEntity<List<WorkOrderItemDTO>> getWorkOrderItems(@PathVariable Long id) {
        return ResponseEntity.ok(workOrderService.getWorkOrderItems(id));
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<WorkOrderItemDTO> addWorkOrderItem(
            @PathVariable Long id,
            @RequestBody WorkOrderItemDTO item
    ) {
        return ResponseEntity.ok(workOrderService.addWorkOrderItem(id, item));
    }

    @PutMapping("/{id}/items/{itemId}")
    public ResponseEntity<WorkOrderItemDTO> updateWorkOrderItem(
            @PathVariable Long id,
            @PathVariable Long itemId,
            @RequestBody WorkOrderItemDTO item
    ) {
        if (!itemId.equals(item.getId())) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(workOrderService.updateWorkOrderItem(id, itemId, item));
    }

    @DeleteMapping("/{id}/items/{itemId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MECHANIC')")
    public ResponseEntity<Void> deleteWorkOrderItem(
            @PathVariable Long id,
            @PathVariable Long itemId
    ) {
        workOrderService.deleteWorkOrderItem(id, itemId);
        return ResponseEntity.noContent().build();
    }

}

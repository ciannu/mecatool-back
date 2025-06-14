package com.svf.mecatool.presentation.controller;

import com.svf.mecatool.business.services.WorkOrderService;
import com.svf.mecatool.integration.model.WorkOrderStatus;
import com.svf.mecatool.presentation.dto.WorkOrderDTO;
import com.svf.mecatool.presentation.dto.WorkOrderItemDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/work-orders")
@RequiredArgsConstructor
@CrossOrigin
public class WorkOrderController {

    private final WorkOrderService workOrderService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MECHANIC', 'CLIENT')")
    public List<WorkOrderDTO> getAll() {
        return workOrderService.getAllWorkOrders();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MECHANIC', 'CLIENT')")
    public ResponseEntity<WorkOrderDTO> getById(@PathVariable Long id) {
        return ResponseEntity.of(Optional.ofNullable(workOrderService.getWorkOrderById(id)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MECHANIC')")
    public ResponseEntity<WorkOrderDTO> create(@RequestBody WorkOrderDTO dto) {
        WorkOrderDTO newWorkOrder = workOrderService.createWorkOrder(dto);
        return new ResponseEntity<>(newWorkOrder, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MECHANIC')")
    public ResponseEntity<WorkOrderDTO> update(@PathVariable Long id, @RequestBody WorkOrderDTO dto) {
        if (!id.equals(dto.getId())) return ResponseEntity.badRequest().build();
        WorkOrderDTO updatedWorkOrder = workOrderService.updateWorkOrder(id, dto);
        return ResponseEntity.ok(updatedWorkOrder);
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

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'MECHANIC')")
    public ResponseEntity<WorkOrderDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam WorkOrderStatus status
    ) {
        WorkOrderDTO updated = workOrderService.updateStatus(id, status);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{id}/assign-mechanics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MECHANIC')")
    public ResponseEntity<WorkOrderDTO> assignMechanics(
            @PathVariable Long id,
            @RequestBody List<Long> mechanicIds
    ) {
        WorkOrderDTO updated = workOrderService.assignMechanics(id, mechanicIds);
        return ResponseEntity.ok(updated);
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
        WorkOrderItemDTO addedItem = workOrderService.addWorkOrderItem(id, item);
        return new ResponseEntity<>(addedItem, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/items/{itemId}")
    public ResponseEntity<WorkOrderItemDTO> updateWorkOrderItem(
            @PathVariable Long id,
            @PathVariable Long itemId,
            @RequestBody WorkOrderItemDTO item
    ) {
        if (!itemId.equals(item.getId())) return ResponseEntity.badRequest().build();
        WorkOrderItemDTO updatedItem = workOrderService.updateWorkOrderItem(id, itemId, item);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/{id}/items/{itemId}")
    public ResponseEntity<Void> deleteWorkOrderItem(
            @PathVariable Long id,
            @PathVariable Long itemId
    ) {
        workOrderService.deleteWorkOrderItem(id, itemId);
        return ResponseEntity.noContent().build();
    }

    // New endpoint for client history
    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserClient(authentication, #clientId)")
    public ResponseEntity<List<WorkOrderDTO>> getWorkOrdersByClientId(@PathVariable Long clientId) {
        List<WorkOrderDTO> workOrders = workOrderService.getWorkOrdersByClientId(clientId);
        return ResponseEntity.ok(workOrders);
    }

    @GetMapping("/total")
    @PreAuthorize("hasAnyRole('ADMIN', 'MECHANIC')")
    public ResponseEntity<Long> getTotalWorkOrders() {
        long totalWorkOrders = workOrderService.getTotalWorkOrders();
        return ResponseEntity.ok(totalWorkOrders);
    }

    @GetMapping("/latest")
    @PreAuthorize("hasAnyRole('ADMIN', 'MECHANIC')")
    public ResponseEntity<WorkOrderDTO> getLatestWorkOrder() {
        Optional<WorkOrderDTO> latestWorkOrder = workOrderService.getLatestWorkOrder();
        return latestWorkOrder.map(ResponseEntity::ok)
                              .orElseGet(() -> ResponseEntity.notFound().build());
    }

}

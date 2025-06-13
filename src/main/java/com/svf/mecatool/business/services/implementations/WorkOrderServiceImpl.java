package com.svf.mecatool.business.services.implementations;

import com.svf.mecatool.business.services.WorkOrderService;
import com.svf.mecatool.integration.model.*;
import com.svf.mecatool.integration.repositories.MechanicRepository;
import com.svf.mecatool.integration.repositories.VehicleRepository;
import com.svf.mecatool.integration.repositories.WorkOrderItemRepository;
import com.svf.mecatool.integration.repositories.WorkOrderRepository;
import com.svf.mecatool.presentation.dto.WorkOrderDTO;
import com.svf.mecatool.presentation.dto.WorkOrderItemDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.svf.mecatool.integration.repositories.InventoryItemRepository;
import com.svf.mecatool.presentation.dto.InventoryItemDTO;
import com.svf.mecatool.integration.mappers.InventoryItemMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class WorkOrderServiceImpl implements WorkOrderService {

    private final WorkOrderRepository workOrderRepository;
    private final VehicleRepository vehicleRepository;
    private final MechanicRepository mechanicRepository;
    private final WorkOrderItemRepository workOrderItemRepository;
    private final InventoryItemRepository inventoryItemRepository;

    private void calculateAndSetWorkOrderTotal(WorkOrder workOrder) {
        BigDecimal total = workOrder.getItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        workOrder.setTotal(total);
        workOrderRepository.save(workOrder);
    }

    @Override
    public WorkOrderDTO createWorkOrder(WorkOrderDTO dto) {
        Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId())
                .orElseThrow(() -> new EntityNotFoundException("Vehicle not found"));

        List<Mechanic> mechanics = mechanicRepository.findAllById(dto.getMechanicIds());

        WorkOrder workOrder = new WorkOrder();
        workOrder.setVehicle(vehicle);
        workOrder.setDescription(dto.getDescription());
        workOrder.setStatus(dto.getStatus() != null ? dto.getStatus() : WorkOrderStatus.PENDING);
        workOrder.setStartDate(dto.getStartDate());
        workOrder.setEndDate(dto.getEndDate());
        workOrder.setMechanics(mechanics);

        WorkOrder saved = workOrderRepository.save(workOrder);
        return mapToDTO(saved);
    }

    @Override
    public WorkOrderDTO updateWorkOrder(Long id, WorkOrderDTO dto) {
        WorkOrder existing = workOrderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Work order not found"));

        if (!existing.getVehicle().getId().equals(dto.getVehicleId())) {
            Vehicle newVehicle = vehicleRepository.findById(dto.getVehicleId())
                    .orElseThrow(() -> new EntityNotFoundException("Vehicle not found"));
            existing.setVehicle(newVehicle);
        }

        List<Mechanic> mechanics = mechanicRepository.findAllById(dto.getMechanicIds());

        existing.setDescription(dto.getDescription());
        existing.setStatus(dto.getStatus());
        existing.setStartDate(dto.getStartDate());
        existing.setEndDate(dto.getEndDate());
        existing.setMechanics(mechanics);

        WorkOrder updated = workOrderRepository.save(existing);
        return mapToDTO(updated);
    }

    @Override
    public void deleteWorkOrder(Long id) {
        if (!workOrderRepository.existsById(id)) {
            throw new EntityNotFoundException("Work order not found");
        }
        workOrderRepository.deleteById(id);
    }

    @Override
    public List<WorkOrderDTO> getAllWorkOrders() {
        return workOrderRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public WorkOrderDTO getWorkOrderById(Long id) {
        WorkOrder workOrder = workOrderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Work order not found"));
        return mapToDTO(workOrder);
    }

    @Override
    public List<WorkOrderDTO> filterWorkOrders(WorkOrderStatus status, Long vehicleId, Long mechanicId) {
        List<WorkOrder> result;

        if (status != null) {
            result = workOrderRepository.findByStatus(status);
        } else if (vehicleId != null) {
            result = workOrderRepository.findByVehicle_Id(vehicleId);
        } else if (mechanicId != null) {
            result = workOrderRepository.findByMechanicId(mechanicId);
        } else {
            result = workOrderRepository.findAll();
        }

        return result.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private WorkOrderDTO mapToDTO(WorkOrder entity) {
        WorkOrderDTO dto = new WorkOrderDTO();
        dto.setId(entity.getId());
        dto.setVehicleId(entity.getVehicle().getId());
        dto.setDescription(entity.getDescription());
        dto.setStatus(entity.getStatus());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setTotal(entity.getTotal());
        dto.setMechanicIds(entity.getMechanics().stream()
                .map(Mechanic::getId)
                .collect(Collectors.toList()));
        return dto;
    }

    @Override
    public WorkOrderDTO updateStatus(Long id, WorkOrderStatus newStatus) {
        WorkOrder workOrder = workOrderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Work order not found"));
        
        workOrder.setStatus(newStatus);
        
        if (newStatus == WorkOrderStatus.COMPLETED && workOrder.getEndDate() == null) {
            workOrder.setEndDate(LocalDate.now());
        }
        
        WorkOrder updated = workOrderRepository.save(workOrder);
        return mapToDTO(updated);
    }

    @Override
    public WorkOrderDTO assignMechanics(Long id, List<Long> mechanicIds) {
        WorkOrder workOrder = workOrderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Work order not found"));
        
        List<Mechanic> mechanics = mechanicRepository.findAllById(mechanicIds);
        if (mechanics.size() != mechanicIds.size()) {
            throw new EntityNotFoundException("One or more mechanics not found");
        }
        
        workOrder.setMechanics(mechanics);
        
        if (workOrder.getStatus() == WorkOrderStatus.PENDING && !mechanics.isEmpty()) {
            workOrder.setStatus(WorkOrderStatus.IN_PROGRESS);
        }
        
        WorkOrder updated = workOrderRepository.save(workOrder);
        return mapToDTO(updated);
    }

    @Override
    public List<WorkOrderItemDTO> getWorkOrderItems(Long workOrderId) {
        WorkOrder workOrder = workOrderRepository.findById(workOrderId)
                .orElseThrow(() -> new EntityNotFoundException("Work order not found"));

        return workOrder.getItems().stream()
                .map(this::mapToItemDTO)
                .collect(Collectors.toList());
    }

    @Override
    public WorkOrderItemDTO addWorkOrderItem(Long workOrderId, WorkOrderItemDTO itemDTO) {
        WorkOrder workOrder = workOrderRepository.findById(workOrderId)
                .orElseThrow(() -> new EntityNotFoundException("Work order not found"));

        InventoryItem inventoryItem = inventoryItemRepository.findById(itemDTO.getInventoryItemId())
                .orElseThrow(() -> new EntityNotFoundException("Inventory item not found"));

        // Decrease inventory stock
        if (inventoryItem.getQuantity() < itemDTO.getQuantity()) {
            throw new IllegalArgumentException("Not enough stock for inventory item: " + inventoryItem.getName());
        }
        inventoryItem.setQuantity(inventoryItem.getQuantity() - itemDTO.getQuantity());
        inventoryItemRepository.save(inventoryItem); // Save updated inventory item

        WorkOrderItem item = new WorkOrderItem();
        item.setWorkOrder(workOrder);
        item.setInventoryItem(inventoryItem);
        item.setQuantity(itemDTO.getQuantity());
        item.setPrice(itemDTO.getPrice());

        WorkOrderItem saved = workOrderItemRepository.save(item);

        // Recalculate and update work order total
        calculateAndSetWorkOrderTotal(workOrder);

        return mapToItemDTO(saved);
    }

    @Override
    public WorkOrderItemDTO updateWorkOrderItem(Long workOrderId, Long itemId, WorkOrderItemDTO itemDTO) {
        WorkOrder workOrder = workOrderRepository.findById(workOrderId)
                .orElseThrow(() -> new EntityNotFoundException("Work order not found"));

        WorkOrderItem existingItem = workOrderItemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Work order item not found"));

        if (!existingItem.getWorkOrder().getId().equals(workOrderId)) {
            throw new IllegalArgumentException("Item does not belong to the specified work order");
        }

        InventoryItem inventoryItem = inventoryItemRepository.findById(itemDTO.getInventoryItemId())
                .orElseThrow(() -> new EntityNotFoundException("Inventory item not found"));

        // Adjust inventory stock based on quantity change
        int oldQuantity = existingItem.getQuantity();
        int newQuantity = itemDTO.getQuantity();
        int quantityDifference = newQuantity - oldQuantity;

        if (quantityDifference > 0) { // Quantity increased, decrease stock
            if (inventoryItem.getQuantity() < quantityDifference) {
                throw new IllegalArgumentException("Not enough stock for inventory item: " + inventoryItem.getName());
            }
            inventoryItem.setQuantity(inventoryItem.getQuantity() - quantityDifference);
        } else if (quantityDifference < 0) { // Quantity decreased, return stock
            inventoryItem.setQuantity(inventoryItem.getQuantity() + Math.abs(quantityDifference));
        }
        inventoryItemRepository.save(inventoryItem); // Save updated inventory item

        existingItem.setInventoryItem(inventoryItem);
        existingItem.setQuantity(itemDTO.getQuantity());
        existingItem.setPrice(itemDTO.getPrice());

        WorkOrderItem updated = workOrderItemRepository.save(existingItem);

        // Recalculate and update work order total
        calculateAndSetWorkOrderTotal(workOrder);

        return mapToItemDTO(updated);
    }

    @Override
    public void deleteWorkOrderItem(Long workOrderId, Long itemId) {
        WorkOrder workOrder = workOrderRepository.findById(workOrderId)
                .orElseThrow(() -> new EntityNotFoundException("Work order not found"));

        WorkOrderItem itemToDelete = workOrderItemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Work order item not found"));

        if (!itemToDelete.getWorkOrder().getId().equals(workOrderId)) {
            throw new IllegalArgumentException("Item does not belong to the specified work order");
        }

        // Return stock to inventory
        InventoryItem inventoryItem = itemToDelete.getInventoryItem();
        inventoryItem.setQuantity(inventoryItem.getQuantity() + itemToDelete.getQuantity());
        inventoryItemRepository.save(inventoryItem);

        workOrderItemRepository.delete(itemToDelete);

        // Recalculate and update work order total
        calculateAndSetWorkOrderTotal(workOrder);
    }

    private WorkOrderItemDTO mapToItemDTO(WorkOrderItem item) {
        WorkOrderItemDTO dto = new WorkOrderItemDTO();
        dto.setId(item.getId());
        dto.setWorkOrderId(item.getWorkOrder().getId());
        dto.setInventoryItemId(item.getInventoryItem().getId());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());
        // Map the full InventoryItem to InventoryItemDTO for frontend display
        dto.setInventoryItem(InventoryItemMapper.toDTO(item.getInventoryItem()));
        return dto;
    }
}

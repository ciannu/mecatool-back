package com.svf.mecatool.business.services.implementations;

import com.svf.mecatool.business.services.InventoryService;
import com.svf.mecatool.integration.model.InventoryItem;
import com.svf.mecatool.integration.repositories.InventoryItemRepository;
import com.svf.mecatool.presentation.dto.InventoryItemDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.svf.mecatool.business.services.NotificationService;
import com.svf.mecatool.integration.model.Notification.NotificationType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    @Autowired
    private NotificationService notificationService;

    @PersistenceContext
    private EntityManager entityManager;

    private static final int LOW_STOCK_THRESHOLD = 5; // Define your low stock threshold

    @Override
    public List<InventoryItemDTO> getAllItems() {
        return inventoryItemRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public InventoryItemDTO getItemById(Long id) {
        return inventoryItemRepository.findById(id)
                .map(this::mapToDTO)
                .orElse(null);
    }

    @Override
    @Transactional
    public InventoryItemDTO createItem(InventoryItemDTO itemDTO) {
        System.out.println("Creating new inventory item in service.");
        System.out.println("Received DTO for creation: " + itemDTO);
        InventoryItem item = mapToEntity(itemDTO);
        System.out.println("Mapped entity before saving: " + item);
        InventoryItem savedItem = inventoryItemRepository.save(item);
        System.out.println("Saved item from repository: " + savedItem);
        return mapToDTO(savedItem);
    }

    @Override
    @Transactional
    public InventoryItemDTO updateItem(Long id, InventoryItemDTO itemDTO) {
        System.out.println("Starting update for item with ID: " + id);
        System.out.println("Received DTO: " + itemDTO);
        
        InventoryItem existingItem = inventoryItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));
        
        System.out.println("Found existing item: " + existingItem);
        
        // Store old quantity to check for stock changes
        int oldQuantity = existingItem.getQuantity();

        // Update all fields
        existingItem.setName(itemDTO.getName());
        existingItem.setCategory(itemDTO.getCategory());
        existingItem.setQuantity(itemDTO.getQuantity());
        existingItem.setMinStock(itemDTO.getMinStock());
        existingItem.setPrice(itemDTO.getPrice());
        existingItem.setDescription(itemDTO.getDescription());
        
        System.out.println("Updated item before save: " + existingItem);
        
        // Force a flush to ensure changes are detected
        entityManager.flush();
        
        // Save and flush to ensure immediate persistence
        InventoryItem savedItem = inventoryItemRepository.saveAndFlush(existingItem);
        System.out.println("Saved item after update: " + savedItem);

        // Check for low stock after update
        checkAndNotifyLowStock(savedItem);
        
        return mapToDTO(savedItem);
    }

    @Override
    @Transactional
    public void deleteItem(Long id) {
        inventoryItemRepository.deleteById(id);
    }

    @Override
    public List<InventoryItemDTO> getItemsByCategory(String category) {
        return inventoryItemRepository.findByCategory(category).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<InventoryItemDTO> getLowStockItems() {
        return inventoryItemRepository.findByQuantityLessThanEqual(0).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private InventoryItemDTO mapToDTO(InventoryItem item) {
        InventoryItemDTO dto = new InventoryItemDTO();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setCategory(item.getCategory());
        dto.setQuantity(item.getQuantity());
        dto.setMinStock(item.getMinStock());
        dto.setPrice(item.getPrice());
        dto.setDescription(item.getDescription());
        return dto;
    }

    private InventoryItem mapToEntity(InventoryItemDTO dto) {
        InventoryItem item = new InventoryItem();
        if (dto.getId() != null) {
            item.setId(dto.getId());
        }
        item.setName(dto.getName());
        item.setCategory(dto.getCategory());
        item.setQuantity(dto.getQuantity());
        item.setMinStock(dto.getMinStock());
        item.setPrice(dto.getPrice());
        item.setDescription(dto.getDescription());
        return item;
    }

    @Override
    public boolean hasEnoughStock(Long itemId, int quantity) {
        return inventoryItemRepository.findById(itemId)
                .map(item -> item.getQuantity() >= quantity)
                .orElse(false);
    }

    @Override
    public void decreaseStock(Long itemId, int quantity) {
        InventoryItem item = inventoryItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Inventory item not found"));

        if (item.getQuantity() < quantity) {
            throw new IllegalStateException("Not enough stock available");
        }

        item.setQuantity(item.getQuantity() - quantity);
        InventoryItem savedItem = inventoryItemRepository.save(item);
        checkAndNotifyLowStock(savedItem);
    }

    @Override
    public void increaseStock(Long itemId, int quantity) {
        InventoryItem item = inventoryItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Inventory item not found"));

        item.setQuantity(item.getQuantity() + quantity);
        InventoryItem savedItem = inventoryItemRepository.save(item);
        checkAndNotifyLowStock(savedItem);
    }

    @Override
    public int getAvailableStock(Long itemId) {
        return inventoryItemRepository.findById(itemId)
                .map(InventoryItem::getQuantity)
                .orElse(0);
    }

    private void checkAndNotifyLowStock(InventoryItem item) {
        if (item.getQuantity() <= item.getMinStock()) {
            String message = String.format("Low stock alert: Item '%s' (ID: %d) quantity is %d, below minimum stock of %d.", 
                                            item.getName(), item.getId(), item.getQuantity(), item.getMinStock());
            Integer systemUserId = 1; 
            notificationService.createNotification(NotificationType.LOW_STOCK, message, systemUserId, item.getId(), "InventoryItem");
        }
    }
} 
package com.svf.mecatool.business.services.implementations;

import com.svf.mecatool.business.services.InventoryItemService;
import com.svf.mecatool.integration.model.InventoryItem;
import com.svf.mecatool.integration.repositories.InventoryItemRepository;
import com.svf.mecatool.presentation.dto.InventoryItemDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class InventoryItemServiceImpl implements InventoryItemService {

    private final InventoryItemRepository repository;

    public InventoryItemServiceImpl(InventoryItemRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<InventoryItemDTO> getAll() {
        return repository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<InventoryItemDTO> getById(Long id) {
        return repository.findById(id).map(this::mapToDTO);
    }

    @Override
    public InventoryItemDTO create(InventoryItemDTO dto) {
        InventoryItem entity = new InventoryItem();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setQuantity(dto.getQuantity());
        entity.setPrice(dto.getPrice());
        entity.setCategory(dto.getCategory());
        entity.setMinStock(dto.getMinStock());

        InventoryItem saved = repository.save(entity);
        return mapToDTO(saved);
    }

    @Override
    public InventoryItemDTO update(Long id, InventoryItemDTO dto) {
        InventoryItem existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Inventory item not found"));
        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setQuantity(dto.getQuantity());
        existing.setPrice(dto.getPrice());
        existing.setCategory(dto.getCategory());
        existing.setMinStock(dto.getMinStock());

        InventoryItem updated = repository.save(existing);
        return mapToDTO(updated);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Inventory item not found");
        }
        repository.deleteById(id);
    }

    private InventoryItemDTO mapToDTO(InventoryItem entity) {
        InventoryItemDTO dto = new InventoryItemDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setQuantity(entity.getQuantity());
        dto.setPrice(entity.getPrice());
        dto.setCategory(entity.getCategory());
        dto.setMinStock(entity.getMinStock());
        return dto;
    }

    @Override
    public InventoryItemDTO decreaseStock(Long id, Integer quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        InventoryItem item = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Inventory item not found"));

        if (item.getQuantity() < quantity) {
            throw new IllegalStateException(
                    String.format("Insufficient stock. Available: %d, Requested: %d",
                            item.getQuantity(), quantity)
            );
        }

        item.setQuantity(item.getQuantity() - quantity);
        InventoryItem updated = repository.save(item);
        return mapToDTO(updated);
    }
}
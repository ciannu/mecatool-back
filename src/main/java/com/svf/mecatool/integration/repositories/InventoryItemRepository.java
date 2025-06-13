package com.svf.mecatool.integration.repositories;

import com.svf.mecatool.integration.model.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
    List<InventoryItem> findByCategory(String category);
    List<InventoryItem> findByQuantityLessThanEqual(Integer minStock);
}

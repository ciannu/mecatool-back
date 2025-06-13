package com.svf.mecatool.integration.repositories;

import com.svf.mecatool.integration.model.WorkOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkOrderItemRepository extends JpaRepository<WorkOrderItem, Long> {
} 
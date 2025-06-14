package com.svf.mecatool.integration.repositories;

import com.svf.mecatool.integration.model.WorkOrder;
import com.svf.mecatool.integration.model.WorkOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {

    List<WorkOrder> findByStatus(WorkOrderStatus status);

    List<WorkOrder> findByVehicle_Id(Long vehicleId);

    List<WorkOrder> findByVehicle_IdIn(List<Long> vehicleIds);

    @Query("SELECT w FROM WorkOrder w JOIN w.mechanics m WHERE m.id = :mechanicId")
    List<WorkOrder> findByMechanicId(@Param("mechanicId") Long mechanicId);

    Optional<WorkOrder> findTopByOrderByStartDateDesc();
}

package com.svf.mecatool.integration.repositories;

import com.svf.mecatool.integration.model.Client;
import com.svf.mecatool.integration.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    List<Vehicle> findByClientId(Long clientId);

    @Query("SELECT v FROM Vehicle v WHERE LOWER(v.brand) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(v.model) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(v.licensePlate) LIKE LOWER(CONCAT('%', :query, '%')) OR CAST(v.year AS string) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(v.client.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(v.client.lastName) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Vehicle> searchVehicles(@Param("query") String query);
}

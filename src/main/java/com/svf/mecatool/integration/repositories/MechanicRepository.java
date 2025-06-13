package com.svf.mecatool.integration.repositories;

import com.svf.mecatool.integration.model.Mechanic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MechanicRepository extends JpaRepository<Mechanic, Long> {
}
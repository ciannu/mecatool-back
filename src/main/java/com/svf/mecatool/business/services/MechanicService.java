package com.svf.mecatool.business.services;

import com.svf.mecatool.integration.model.Mechanic;

import java.util.List;
import java.util.Optional;

public interface MechanicService {

    List<Mechanic> getAll();
    Optional<Mechanic> getMechanicById(Long id);
    Mechanic create(Mechanic mechanic);
    Mechanic save(Mechanic mechanic);
    void deleteById(Long id);
    List<Mechanic> getMechanicsByIds(List<Long> ids);
}

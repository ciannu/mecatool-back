package com.svf.mecatool.business.services.implementations;

import com.svf.mecatool.business.services.MechanicService;
import com.svf.mecatool.integration.model.Mechanic;
import com.svf.mecatool.integration.repositories.MechanicRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MechanicServiceImpl implements MechanicService {

    private final MechanicRepository mechanicRepository;

    public MechanicServiceImpl(MechanicRepository mechanicRepository) {
        this.mechanicRepository = mechanicRepository;
    }

    @Override
    public List<Mechanic> getAll() {
        return mechanicRepository.findAll();
    }

    @Override
    public Optional<Mechanic> getMechanicById(Long id) {
        return mechanicRepository.findById(id);
    }

    @Override
    public Mechanic create(Mechanic mechanic) {
        mechanic.setId(null);
        return mechanicRepository.save(mechanic);
    }

    @Override
    public Mechanic save(Mechanic mechanic) {
        return mechanicRepository.save(mechanic);
    }

    @Override
    public void deleteById(Long id) {
        mechanicRepository.deleteById(id);
    }

    @Override
    public List<Mechanic> getMechanicsByIds(List<Long> ids) {
        return mechanicRepository.findAllById(ids);
    }
}

package com.svf.mecatool.business.services.implementations;

import com.svf.mecatool.business.services.RoleService;
import com.svf.mecatool.integration.mappers.RoleMapper;
import com.svf.mecatool.integration.repositories.RoleRepository;
import com.svf.mecatool.presentation.dto.RoleDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(RoleMapper::toDTO)
                .collect(Collectors.toList());
    }
} 
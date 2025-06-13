package com.svf.mecatool.integration.mappers;

import com.svf.mecatool.integration.model.Role;
import com.svf.mecatool.presentation.dto.RoleDTO;

public class RoleMapper {

    public static Role toEntity(RoleDTO dto) {
        if (dto == null) {
            return null;
        }
        Role entity = new Role();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        return entity;
    }

    public static RoleDTO toDTO(Role entity) {
        if (entity == null) {
            return null;
        }
        RoleDTO dto = new RoleDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        return dto;
    }
} 
package com.svf.mecatool.integration.mappers;

import com.svf.mecatool.integration.model.User;
import com.svf.mecatool.presentation.dto.UserDTO;

public class UserMapper {

    public static User toEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }
        User entity = new User();
        entity.setId(dto.getId());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());
        // Password and Role are set in the service layer, not directly mapped here
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }

    public static UserDTO toDTO(User entity) {
        if (entity == null) {
            return null;
        }
        UserDTO dto = new UserDTO();
        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setEmail(entity.getEmail());
        if (entity.getRole() != null) {
            dto.setRoleId(entity.getRole().getId());
            dto.setRole(RoleMapper.toDTO(entity.getRole()));
        }
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
} 
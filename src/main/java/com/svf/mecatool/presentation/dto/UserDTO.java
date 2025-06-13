package com.svf.mecatool.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Long roleId; // Only send role ID from frontend
    private RoleDTO role; // For sending full role object to frontend
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 
package com.svf.mecatool.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MechanicDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}

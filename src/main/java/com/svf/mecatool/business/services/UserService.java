package com.svf.mecatool.business.services;

import com.svf.mecatool.presentation.dto.UserDTO;
import com.svf.mecatool.presentation.dto.UserPasswordUpdateDTO;
import com.svf.mecatool.presentation.dto.UserProfileUpdateDTO;
import com.svf.mecatool.presentation.dto.UserRegisterDTO;

import java.util.List;

public interface UserService {
    UserDTO createUser(UserRegisterDTO userRegisterDTO);
    List<UserDTO> getAllUsers();
    UserDTO getUserById(Long id);
    UserDTO updateUser(Long id, UserDTO userDTO);
    void deleteUser(Long id);
    UserDTO findByEmail(String email);
    UserDTO updateProfile(Long id, UserProfileUpdateDTO userProfileUpdateDTO);
    void changePassword(Long id, UserPasswordUpdateDTO userPasswordUpdateDTO);
} 
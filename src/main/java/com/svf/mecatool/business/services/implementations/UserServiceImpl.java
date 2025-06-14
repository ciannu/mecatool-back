package com.svf.mecatool.business.services.implementations;

import com.svf.mecatool.business.services.UserService;
import com.svf.mecatool.integration.mappers.UserMapper;
import com.svf.mecatool.integration.model.Role;
import com.svf.mecatool.integration.model.User;
import com.svf.mecatool.integration.repositories.RoleRepository;
import com.svf.mecatool.integration.repositories.UserRepository;
import com.svf.mecatool.presentation.dto.UserDTO;
import com.svf.mecatool.presentation.dto.UserPasswordUpdateDTO;
import com.svf.mecatool.presentation.dto.UserProfileUpdateDTO;
import com.svf.mecatool.presentation.dto.UserRegisterDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDTO createUser(UserRegisterDTO userRegisterDTO) {
        if (userRepository.existsByEmail(userRegisterDTO.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        Role role = roleRepository.findById(userRegisterDTO.getRoleId())
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));

        User user = new User();
        user.setFirstName(userRegisterDTO.getFirstName());
        user.setLastName(userRegisterDTO.getLastName());
        user.setEmail(userRegisterDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        user.setRole(role);

        User savedUser = userRepository.save(user);
        return UserMapper.toDTO(savedUser);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return UserMapper.toDTO(user);
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        existingUser.setFirstName(userDTO.getFirstName());
        existingUser.setLastName(userDTO.getLastName());
        existingUser.setEmail(userDTO.getEmail());

        if (userDTO.getRoleId() != null) {
            Role newRole = roleRepository.findById(userDTO.getRoleId())
                    .orElseThrow(() -> new EntityNotFoundException("Role not found"));
            existingUser.setRole(newRole);
        }

        User updatedUser = userRepository.save(existingUser);
        return UserMapper.toDTO(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserDTO findByEmail(String email) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return UserMapper.toDTO(user);
    }

    @Override
    public UserDTO updateProfile(Long id, UserProfileUpdateDTO userProfileUpdateDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        existingUser.setFirstName(userProfileUpdateDTO.getFirstName());
        existingUser.setLastName(userProfileUpdateDTO.getLastName());
        existingUser.setEmail(userProfileUpdateDTO.getEmail());

        User updatedUser = userRepository.save(existingUser);
        return UserMapper.toDTO(updatedUser);
    }

    @Override
    public void changePassword(Long id, UserPasswordUpdateDTO userPasswordUpdateDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!passwordEncoder.matches(userPasswordUpdateDTO.getCurrentPassword(), existingUser.getPassword())) {
            throw new IllegalArgumentException("Current password does not match");
        }

        existingUser.setPassword(passwordEncoder.encode(userPasswordUpdateDTO.getNewPassword()));
        userRepository.save(existingUser);
    }
} 
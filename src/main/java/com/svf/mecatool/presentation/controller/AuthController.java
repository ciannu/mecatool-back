package com.svf.mecatool.presentation.controller;

import com.svf.mecatool.business.services.UserService;
import com.svf.mecatool.presentation.dto.AuthResponseDTO;
import com.svf.mecatool.presentation.dto.UserLoginDTO;
import com.svf.mecatool.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO loginRequest) {
        try {
            log.info("\n=== Login Attempt ===");
            log.info("Email: {}", loginRequest.getEmail());
            log.info("Password length: {}", loginRequest.getPassword().length());
            
            try {
                Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                    )
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                String jwt = jwtService.generateToken(userDetails);
                
                log.info("=== Authentication Successful ===");
                log.info("Username: {}", userDetails.getUsername());
                log.info("Authorities: {}", userDetails.getAuthorities());
                log.info("JWT token generated");

                AuthResponseDTO response = new AuthResponseDTO();
                response.setToken(jwt);
                response.setUser(userService.findByEmail(userDetails.getUsername()));

                return ResponseEntity.ok(response);
            } catch (BadCredentialsException e) {
                log.error("\n=== Authentication Failed ===");
                log.error("Error type: BadCredentialsException");
                log.error("Error message: {}", e.getMessage());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid email or password");
            } catch (Exception e) {
                log.error("\n=== Authentication Error ===");
                log.error("Error type: {}", e.getClass().getSimpleName());
                log.error("Error message: {}", e.getMessage());
                log.error("Stack trace:", e);
                throw e; // Re-throw to be caught by outer try-catch
            }
        } catch (Exception e) {
            log.error("\n=== Login Error ===");
            log.error("Error type: {}", e.getClass().getSimpleName());
            log.error("Error message: {}", e.getMessage());
            log.error("Stack trace:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during login");
        }
    }
} 
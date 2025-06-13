package com.svf.mecatool.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collections;

public class AuthTest {
    public static void main(String[] args) {
        // 1. Test password encoding
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
        String rawPassword = "admin123";
        String storedHash = "$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW";
        
        System.out.println("=== Password Encoding Test ===");
        System.out.println("Raw password: " + rawPassword);
        System.out.println("Stored hash: " + storedHash);
        System.out.println("Password matches? " + encoder.matches(rawPassword, storedHash));
        
        // 2. Generate new hash
        String newHash = encoder.encode(rawPassword);
        System.out.println("\nNew hash generated: " + newHash);
        System.out.println("New hash matches raw password? " + encoder.matches(rawPassword, newHash));
        
        // 3. Test with UserDetails
        UserDetails userDetails = new User(
            "admin@mecatool.com",
            storedHash,
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
        
        System.out.println("\n=== UserDetails Test ===");
        System.out.println("Username: " + userDetails.getUsername());
        System.out.println("Password: " + userDetails.getPassword());
        System.out.println("Authorities: " + userDetails.getAuthorities());
        System.out.println("Password matches? " + encoder.matches(rawPassword, userDetails.getPassword()));
    }
} 
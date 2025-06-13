package com.svf.mecatool;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "admin123";
        String encodedPassword = "$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi";

        System.out.println("Testing password match:");
        System.out.println("Raw password: " + rawPassword);
        System.out.println("Encoded password: " + encodedPassword);
        
        boolean matches = encoder.matches(rawPassword, encodedPassword);
        System.out.println("Password matches? " + matches);

        // Generate a new hash for comparison
        String newHash = encoder.encode(rawPassword);
        System.out.println("\nNew hash generated for 'admin123': " + newHash);
        System.out.println("New hash matches raw password? " + encoder.matches(rawPassword, newHash));

        // Test with different strength
        BCryptPasswordEncoder encoder10 = new BCryptPasswordEncoder(10);
        String newHash10 = encoder10.encode(rawPassword);
        System.out.println("\nNew hash with strength 10: " + newHash10);
        System.out.println("New hash with strength 10 matches? " + encoder10.matches(rawPassword, newHash10));
    }
} 
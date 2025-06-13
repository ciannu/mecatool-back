package com.svf.mecatool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class MecatoolApplication {

	public static void main(String[] args) {
		SpringApplication.run(MecatoolApplication.class, args);

		// Temporal code to hash password
		System.out.println("\n--- Hashed Password ---");
		System.out.println(new BCryptPasswordEncoder(10).encode("admin123"));
		System.out.println("-------------------------");
	}

}

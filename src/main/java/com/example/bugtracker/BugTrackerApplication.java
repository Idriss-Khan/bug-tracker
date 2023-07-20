package com.example.bugtracker;

import com.example.bugtracker.model.Role;
import com.example.bugtracker.model.User;
import com.example.bugtracker.repository.RoleRepository;
import com.example.bugtracker.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class BugTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BugTrackerApplication.class, args);
	}

	@Bean
	CommandLineRunner run(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			if (roleRepository.findByName("ADMIN") != null) return;

			// CREATE ROLES
			List<Role> roles = Arrays.asList(
					new Role(1, "ADMIN"), // Set the ID of the ADMIN role to 1
					new Role("DEVELOPER"),
					new Role("PROJECT Manager"),
					new Role("USER")
			);

			roleRepository.saveAll(roles);

			Role adminRole = roleRepository.findById(1).orElse(null); // Retrieve the ADMIN role by ID

			// Create admin user with role of ADMIN
			User admin = new User(1, "admin", passwordEncoder.encode("password"), "admin", "admin", Set.of(adminRole));

			userRepository.save(admin);
		};
	}

}

package com.smartlms.backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.smartlms.backend.entity.Role;
import com.smartlms.backend.repository.RoleRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializeRoles(RoleRepository roleRepository) {
        return args -> {

            if (roleRepository.findByName("STUDENT").isEmpty()) {
                roleRepository.save(new Role("STUDENT"));
            }

            if (roleRepository.findByName("INSTRUCTOR").isEmpty()) {
                roleRepository.save(new Role("INSTRUCTOR"));
            }

            if (roleRepository.findByName("ADMIN").isEmpty()) {
                roleRepository.save(new Role("ADMIN"));
            }

            System.out.println("SmartLMS roles initialized successfully!");
        };
    }
}
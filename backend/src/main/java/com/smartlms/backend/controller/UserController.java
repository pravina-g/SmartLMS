package com.smartlms.backend.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartlms.backend.dto.LoginRequest;
import com.smartlms.backend.dto.LoginResponse;
import com.smartlms.backend.entity.Role;
import com.smartlms.backend.entity.User;
import com.smartlms.backend.repository.RoleRepository;
import com.smartlms.backend.security.JwtService;
import com.smartlms.backend.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;

    public UserController(UserService userService,
                          RoleRepository roleRepository,
                          JwtService jwtService) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {

        if (userService.existsByEmail(user.getEmail())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Email already registered");
        }

        Optional<Role> studentRole = roleRepository.findByName("STUDENT");

        if (studentRole.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("STUDENT role not found");
        }

        user.getRoles().add(studentRole.get());

        User savedUser = userService.saveUser(user);

        Map<String, Object> response = new HashMap<>();
        response.put("id", savedUser.getId());
        response.put("name", savedUser.getName());
        response.put("email", savedUser.getEmail());
        response.put("role", "STUDENT");
        response.put("message", "User registered successfully");

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request) {

        Optional<User> userOptional =
                userService.findByEmail(request.getEmail());

        if (userOptional.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid email or password");
        }

        User user = userOptional.get();

        boolean passwordMatches =
                userService.verifyPassword(
                        request.getPassword(),
                        user.getPassword()
                );

        if (!passwordMatches) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid email or password");
        }

        String role = user.getRoles()
                .stream()
                .findFirst()
                .map(Role::getName)
                .orElse("STUDENT");

        String token = jwtService.generateToken(
                user.getEmail(),
                role
        );

        LoginResponse response = new LoginResponse(
                "Login successful",
                user.getName(),
                user.getEmail(),
                role
        );

        return ResponseEntity.ok(
                Map.of(
                        "message", response.getMessage(),
                        "name", response.getName(),
                        "email", response.getEmail(),
                        "role", response.getRole(),
                        "token", token
                )
        );
    }
}
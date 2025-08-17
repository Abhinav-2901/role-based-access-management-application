package com.abhinav.rbac.api.auth;

import com.abhinav.rbac.application.users.UserService;
import com.abhinav.rbac.domain.model.User;
import com.abhinav.rbac.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        try {
            // Authenticate user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            // Load user details
            User user = userService.getUserByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Set<String> roles = user.getRoles().stream()
                    .map(role -> "ROLE_" + role.getName()) // Add ROLE_ prefix
                    .collect(Collectors.toSet());

            // Generate JWT
            String token = jwtUtil.generateToken(user.getUsername(), roles);

            return new AuthResponse(token);
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid username or password");
        }
    }
    
    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
    	if (userService.usernameExists(request.getUsername())) {
            return "Username already exists";
        }

        if (userService.emailExists(request.getEmail())) {
            return "Email already exists";
        }
    	
    	String encodedPassword = passwordEncoder.encode(request.getPassword());
    	
    	
    	Set<String> roles = new HashSet<>();
        roles.add("USER");

        // Create user
        userService.createUser(request.getUsername(), encodedPassword, request.getEmail(), roles);

        return "User registered successfully";
    }
}
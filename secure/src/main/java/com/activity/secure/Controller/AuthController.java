package com.activity.secure.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.activity.secure.Model.User;
import com.activity.secure.Service.AuthService;
import com.activity.secure.Service.TokenBlacklistService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api")
@Tag(name = "Authentication", description = "APIs for user registration and authentication")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @Operation(summary = "Register a new user", description = "Registers a new user with a username, password, and role") // Swagger
                                                                                                                          // operation
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or username already taken", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            // Validate role
            if (user.getRole() == null || 
                (!user.getRole().equalsIgnoreCase("ADMIN") && 
                !user.getRole().equalsIgnoreCase("USER"))) {
                return ResponseEntity.badRequest().body("Invalid role. Must be ADMIN or USER");
            }
            
            String message = authService.registerUser(user);
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @Operation(summary = "Authenticate a user", description = "Authenticates a user and returns a JWT token") // Swagger
                                                                                                              // operation
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid username or password", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody User user) {
        try {
            String jwt = authService.authenticateUser(user.getUsername(), user.getPassword());
            return ResponseEntity.ok(jwt);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Logout user", description = "Invalidates the current JWT token")
    @SecurityRequirement(name = "bearerAuth") // Requires valid JWT token
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String token = extractToken(request);
        if (token != null) {
            tokenBlacklistService.blacklistToken(token);
            return ResponseEntity.ok("Logged out successfully");
        }
        return ResponseEntity.badRequest().body("No valid token found");
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}

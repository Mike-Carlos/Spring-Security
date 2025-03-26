package com.activity.secure.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.activity.secure.Model.User;
import com.activity.secure.Service.AuthService;
import com.activity.secure.Service.TokenBlacklistService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "APIs for user authentication and authorization")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @Operation(summary = "Register new user", description = "Registers a new user with username, password, and role (ADMIN only)", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class), examples = @ExampleObject(value = "{\"username\": \"newuser\", \"password\": \"securePassword123\", \"role\": \"USER\"}"))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class), examples = @ExampleObject(value = "\"User registered successfully\""))),
            @ApiResponse(responseCode = "400", description = "Invalid input or username already taken", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class), examples = @ExampleObject(value = "\"Username already taken\""))),
            @ApiResponse(responseCode = "403", description = "Forbidden - only ADMIN can register users")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {
        try {
            // Validate role
            if (user.getRole() == null ||
                    (!user.getRole().equalsIgnoreCase("ADMIN") &&
                            !user.getRole().equalsIgnoreCase("USER"))) {
                return ResponseEntity.badRequest().body("Invalid role. Must be ADMIN or USER");
            }

            String message = authService.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(message);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Authenticate user", description = "Authenticates user and returns JWT token", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class), examples = @ExampleObject(value = "{\"username\": \"admin\", \"password\": \"admin\"}"))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful", content = @Content(schema = @Schema(implementation = String.class), examples = @ExampleObject(value = "\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\""))),
            @ApiResponse(responseCode = "400", description = "Invalid username or password", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class), examples = @ExampleObject(value = "\"Invalid credentials\""))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - authentication failed")
    })
    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody User user) {
        try {
            String jwt = authService.authenticateUser(user.getUsername(), user.getPassword());
            return ResponseEntity.ok(jwt);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @Operation(summary = "Logout user", description = "Invalidates the current JWT token")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logout successful", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class), examples = @ExampleObject(value = "\"Logged out successfully\""))),
            @ApiResponse(responseCode = "400", description = "No valid token found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class), examples = @ExampleObject(value = "\"No valid token found\""))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing token")
    })
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String token = extractToken(request);
        if (token != null && !tokenBlacklistService.isTokenBlacklisted(token)) {
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
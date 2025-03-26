package com.activity.secure.Service;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.activity.secure.Model.User;
import com.activity.secure.Repository.UserRepository;
import com.activity.secure.Security.JwtUtil;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Registers a new user.
     *
     * @param user The user to register.
     * @return A message indicating success or failure.
     */
    public String registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Error: Username is already taken!");
        }

        // Set default role if not provided
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER");
        }

        // Validate role
        if (!user.getRole().equals("USER") && !user.getRole().equals("ADMIN")) {
            throw new RuntimeException("Error: Invalid role. Role must be either USER or ADMIN.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return "User registered successfully with role: " + user.getRole();

    }

    /**
     * Authenticates a user and generates a JWT token.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @return The generated JWT token.
     * @throws AuthenticationException If authentication fails.
     */
    public String authenticateUser(String username, String password) throws AuthenticationException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
        } catch (AuthenticationException e) {
            throw new RuntimeException("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return jwtUtil.generateToken(userDetails.getUsername());
    }

}

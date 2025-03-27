// package com.activity.secure.Security;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.context.annotation.Profile;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.access.channel.ChannelProcessingFilter;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// @Configuration
// @Profile("dev")
// public class DevSecurityConfig {
    
//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http
//             .addFilterBefore(ipWhitelistFilter, ChannelProcessingFilter.class) // Add IP filter
//             .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Enable CORS
//             .csrf(csrf -> csrf.disable()) // Disable CSRF for WebSocket connections
//             .authorizeHttpRequests(auth -> auth
//                 .requestMatchers("/api/auth/authenticate").permitAll()
//                 .requestMatchers(
//                     "/swagger-ui/**", // Swagger UI
//                     "/v3/api-docs/**", // OpenAPI JSON
//                     "/swagger-resources/**", // Swagger resources
//                     "/webjars/**" // WebJars
//                 ).permitAll() // Allow Swagger UI
//                 .requestMatchers("/api/auth/register/**").hasRole("ADMIN")
//                 .requestMatchers("/api/inventory/**").authenticated()
//                 .anyRequest().authenticated()                
//             )
//             .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//             .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

//         return http.build();
//     }
// }

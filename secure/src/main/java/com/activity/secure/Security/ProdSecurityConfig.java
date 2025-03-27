package com.activity.secure.Security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;

@Configuration
@EnableWebSecurity
@Profile("prod") // This config only activates with 'prod' profile
public class ProdSecurityConfig {

    private final JwtFilter jwtFilter;
    private final UserDetailsService userDetailsService;
    private final IpWhitelistFilter ipWhitelistFilter;

    public ProdSecurityConfig(JwtFilter jwtFilter,
            UserDetailsService userDetailsService,
            IpWhitelistFilter ipWhitelistFilter) {
        this.jwtFilter = jwtFilter;
        this.userDetailsService = userDetailsService;
        this.ipWhitelistFilter = ipWhitelistFilter;
    }

    // Reuse your existing beans with production-specific tweaks
    @Bean("prodPasswordEncoder")
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12); // Stronger hashing for production
    }

    @Bean("prodAuthenticationmanager")
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        provider.setHideUserNotFoundExceptions(false); // Better security practice
        return new ProviderManager(List.of(provider));
    }

    @Bean("prodSecurityFilterChain")
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Production-specific IP filtering
            .addFilterBefore(ipWhitelistFilter, ChannelProcessingFilter.class)

            // Strict CORS for production
            .cors(cors -> cors.configurationSource(prodCorsConfigurationSource()))

            // Security headers
            .headers(headers -> headers
                .contentSecurityPolicy(csp -> csp.policyDirectives(
                    "default-src 'self'; " +
                    "script-src 'self'; " +
                    "style-src 'self' 'unsafe-inline'; " +
                    "img-src 'self' data:; " +
                    "connect-src 'self'"
                ))
                .httpStrictTransportSecurity(hsts -> hsts
                    .includeSubDomains(true)
                    .maxAgeInSeconds(31536000) // 1 year
                    .preload(true)
                )
                .frameOptions(frame -> frame.deny())
                .xssProtection(xss -> xss
            .headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK)            
                )
            )
            // Authorization
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/authenticate").permitAll()
                .requestMatchers("/api/auth/register/**").hasRole("ADMIN")
                .requestMatchers("/api/inventory/**").authenticated()
                // Disable Swagger in production
                .requestMatchers(
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/swagger-resources/**"
                ).denyAll()
                .anyRequest().authenticated()
            )
            .csrf(csrf -> csrf.disable()) // Still disabled for API
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean("prodCorsConfiguration")
    public CorsConfigurationSource prodCorsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "http://127.0.0.1:5173",
                "http://[::1]:5173",
                "http://0:0:0:0:0:0:0:1:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setExposedHeaders(List.of("X-Request-ID"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
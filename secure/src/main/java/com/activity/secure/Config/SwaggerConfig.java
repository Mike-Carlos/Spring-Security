package com.activity.secure.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * This class is responsible for configuring Swagger in the Spring Boot application.
 * It creates an OpenAPI object with custom components and info.
 */
@Configuration
public class SwaggerConfig {

    /**
     * This method creates and returns an OpenAPI object with custom components and info.
     * It defines a security scheme named "bearerAuth" of type HTTP, scheme "bearer", and bearer format "JWT".
     *
     * @return OpenAPI object with custom components and info
     */
    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
            .components(new Components()
                .addSecuritySchemes("bearerAuth", new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
                    )
            )
            .info(new Info()
                .title("SecureApplication")
                .version("1.0.0")
                .description("Sample API for secure applications")
                );
    }
}


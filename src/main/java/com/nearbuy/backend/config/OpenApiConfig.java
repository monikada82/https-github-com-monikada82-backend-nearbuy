package com.nearbuy.backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI nearBuyOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("NearBuy E-Commerce Backend API")
                        .description("REST APIs for authentication, products, categories, cart, addresses, orders, and payments.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("NearBuy Backend")
                                .email("support@nearbuy.local"))
                        .license(new License()
                                .name("Learning Project")))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}

package com.example.helmes_challenge.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI jobfairHelmesOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Jobfair Helmes Challenge API")
                .version("v1")
                .description("API documentation for parental benefit calculation endpoints"));
    }
}

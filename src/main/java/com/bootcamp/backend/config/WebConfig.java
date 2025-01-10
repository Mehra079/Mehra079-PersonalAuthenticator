package com.bootcamp.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")  // This is your API path
                .allowedOrigins("http://localhost:4200")  // Your front-end origin
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Allow necessary methods
                .allowedHeaders("*")  // Allow any headers
                .allowCredentials(true);
    }
}

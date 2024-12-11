package com.example.JobBoard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Allow only the frontend URL (React) to make requests
        config.addAllowedOrigin("http://localhost:3000"); // React frontend URL
        config.addAllowedMethod("*");  // Allow all HTTP methods (GET, POST, PUT, DELETE, etc.)
        config.addAllowedHeader("*");  // Allow all headers
        config.setAllowCredentials(true);  // Allow cookies, authorization headers, etc.
        config.addExposedHeader("Authorization");  // Expose Authorization header for frontend
        config.addExposedHeader("Content-Type");  // Expose Content-Type header

        // Register the CORS configuration globally for all endpoints
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}

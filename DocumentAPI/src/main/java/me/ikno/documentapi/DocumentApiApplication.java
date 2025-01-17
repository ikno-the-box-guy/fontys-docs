package me.ikno.documentapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableAsync
@SpringBootApplication
public class DocumentApiApplication {
    @Value("${cors.origin}")
    private String corsOrigin;

    public static void main(String[] args) {
        SpringApplication.run(DocumentApiApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        String corsOrigin = this.corsOrigin;

        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .exposedHeaders("*")
                        .exposedHeaders("Content-Disposition")
                        .allowedOrigins(corsOrigin)
                        .allowCredentials(true);
            }
        };
    }
}
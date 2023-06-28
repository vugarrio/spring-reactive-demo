package com.example.reactive.annotation.geotracking.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenApiConfiguration configuration.
 */
@Configuration
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI customOpenAPI(@Value("${info.app.version}") String appVersion) {
        return new OpenAPI()

                .info(new Info()
                .title("Geo-tracking Service API")
                .description("API documentation for geotracking-reactive-annotation-microservice")
                .version(appVersion)
            );
    }
}

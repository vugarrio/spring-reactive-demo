package com.example.reactive.functional.geotracking.config;

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

            // TODO: pendiente de actualizar info
                .info(new Info()
                .title("Geo-tracking Service API")
                .description("API documentation for geotracking-reactive-funcional-microservice")
                .version(appVersion)
            );
    }
}

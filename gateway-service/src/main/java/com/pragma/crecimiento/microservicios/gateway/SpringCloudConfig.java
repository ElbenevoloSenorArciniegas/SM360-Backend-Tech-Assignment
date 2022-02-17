package com.pragma.crecimiento.microservicios.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringCloudConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/usuarios/**")
                        .uri("http://localhost:8091/")
                        //.id("usuario-service")
                )
                .route(r -> r.path("/imagenes/**")
                        .uri("http://localhost:8092/")
                        //.id("imagen-service")
                )
                .build();
    }

}
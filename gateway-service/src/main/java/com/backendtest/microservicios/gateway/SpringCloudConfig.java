package com.backendtest.microservicios.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringCloudConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/listings/**")
                        .uri("http://listing-service:8091/")
                        //.id("listing-service")
                )
                .route(r -> r.path("/dealers/**")
                        .uri("http://dealer-service:8092/")
                        //.id("dealer-service")
                )
                .build();
    }

}
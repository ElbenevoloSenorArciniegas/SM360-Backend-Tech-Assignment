package com.backendtest.microservicios.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class SpringCloudConfig {

        @Value( "${services.listing}" )
        private String URI_LISTING_SERVICE;

        @Value( "${services.dealer}" )
        private String URI_DEALER_SERVICE;

        @Bean
        public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
                return builder.routes()
                        .route(r -> r.path("/listings/**")
                                .uri(URI_LISTING_SERVICE)
                                //.id("listing-service")
                        )
                        .route(r -> r.path("/dealers/**")
                                .uri(URI_DEALER_SERVICE)
                                //.id("dealer-service")
                        )
                        .build();
        }

}
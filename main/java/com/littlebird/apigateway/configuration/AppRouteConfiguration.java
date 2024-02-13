package com.littlebird.apigateway.configuration;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppRouteConfiguration {


    // static imports from GatewayFilters and RoutePredicates
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/search/**")
                        .uri("http://localhost:8083"))
                .route(r -> r.path("/auth/**")
                        .uri("http://localhost:8081"))
                .build();
    }
}

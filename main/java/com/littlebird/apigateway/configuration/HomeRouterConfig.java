package com.littlebird.apigateway.configuration;

import com.littlebird.apigateway.router.AppRouter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;

@Configuration
public class HomeRouterConfig {

    @Bean
    public RouterFunction<ServerResponse> routes(AppRouter handler)  {
        return RouterFunctions.route(RequestPredicates.POST("/api/post")
                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::postTweetHandler);
    }
}

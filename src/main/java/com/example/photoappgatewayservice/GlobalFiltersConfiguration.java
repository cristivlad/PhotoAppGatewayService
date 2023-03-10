package com.example.photoappgatewayservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
public class GlobalFiltersConfiguration {

    private final Logger log = LoggerFactory.getLogger(GlobalFiltersConfiguration.class);

    @Order(1)
    @Bean
    public GlobalFilter secondPreFilter() {
        return ((exchange, chain) -> {
            log.info("My second global pre filter is executed...");
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                log.info("My second global post filter is executed...");
            }));
        });
    }

    @Order(2)
    @Bean
    public GlobalFilter thirdPreFilter() {
        return ((exchange, chain) -> {
            log.info("My third global pre filter is executed...");
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                log.info("My first global post filter is executed...");
            }));
        });
    }
}

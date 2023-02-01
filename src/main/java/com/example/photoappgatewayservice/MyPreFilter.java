package com.example.photoappgatewayservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Set;

@Component
public class MyPreFilter implements GlobalFilter {

    private final Logger logger = LoggerFactory.getLogger(MyPreFilter.class);
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.info("My first pre-filter executed...");

        ServerHttpRequest request = exchange.getRequest();
        String requestPath = request.getPath().toString();
        logger.info("Request path: " + requestPath);

        HttpHeaders headers = exchange.getRequest().getHeaders();
        Set<String> headerNames = headers.keySet();
        for (String header: headerNames) {
            String headerValue = headers.getFirst(header);
            logger.info(header + " " + headerValue);
        }
        return chain.filter(exchange);
    }
}

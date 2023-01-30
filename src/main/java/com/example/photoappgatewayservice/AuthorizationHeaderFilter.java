package com.example.photoappgatewayservice;

import io.jsonwebtoken.Jwts;
import org.apache.logging.log4j.util.Strings;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    private Environment environment;

    public AuthorizationHeaderFilter() {
        super(Config.class);
    }

    public AuthorizationHeaderFilter(Environment environment) {
        this.environment = environment;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION))
                return onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);

            String token = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String jwt = token.replace("Bearer", "");

            if (!isJwtValid(jwt))
                return onError(exchange, "JWT token not valid", HttpStatus.UNAUTHORIZED);

            return chain.filter(exchange);
        });
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        return response.setComplete();
    }

    private boolean isJwtValid(String jwt) {
        boolean returnValue = true;
        String subject = Jwts.parser().setSigningKey(environment.getProperty("token.secret"))
                .parseClaimsJws(jwt)
                .getBody()
                .getSubject();
        if (Strings.isBlank(subject))
            returnValue = false;

        return returnValue;
    }

    public static class Config {

    }
}

package com.sau.swe.apigateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private final WebClient webClient;
    
    private final List<String> publicPaths = Arrays.asList(
        "/finance-mgmt/api/authenticate/login",
        "/finance-mgmt/api/authenticate/signup",
        "/finance-mgmt/api/authenticate/verify-2fa",
        "/finance-mgmt/api/authenticate/verifytoken"
    );

    public AuthenticationFilter() {
        this.webClient = WebClient.builder()
            .baseUrl("http://authentication-service:8080")
            .build();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        String method = request.getMethod().toString();

        log.info("API Gateway Filter - Path: {}, Method: {}", path, method);

        if (isPublicPath(path)) {
            log.info("Public path detected, skipping authentication: {}", path);
            return chain.filter(exchange);
        }

        String authHeader = request.getHeaders().getFirst("Authorization");
        log.info("Authorization header: {}", authHeader != null ? "Bearer ***" : "null");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Missing or invalid Authorization header for path: {}", path);
            return onError(exchange, "Missing or invalid Authorization header", HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7); // "Bearer " prefix'ini kaldır
        log.info("Extracting token for verification, token length: {}", token.length());

        return verifyToken(token)
            .flatMap(isValid -> {
                log.info("Token verification result: {}", isValid);
                if (isValid) {
                    return chain.filter(exchange);
                } else {
                    return onError(exchange, "Invalid token", HttpStatus.UNAUTHORIZED);
                }
            })
            .onErrorResume(throwable -> {
                log.error("Token verification failed with error: {}", throwable.getMessage());
                return onError(exchange, "Token verification failed", HttpStatus.UNAUTHORIZED);
            });
    }

    private boolean isPublicPath(String path) {
        return publicPaths.stream().anyMatch(path::startsWith);
    }

    private Mono<Boolean> verifyToken(String token) {
        String requestBody = String.format("{\"token\":\"%s\"}", token);
        log.info("Sending token verification request to auth service");
        
        return webClient.post()
            .uri("/finance-mgmt/api/authenticate/verifytoken")
            .header("Content-Type", "application/json")
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(String.class)
            .map(response -> {
                log.info("Auth service response: {}", response);
                return response.contains("\"valid\":true");
            })
            .onErrorReturn(false);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        response.getHeaders().add("Content-Type", "application/json");
        
        String errorBody = String.format("{\"error\":\"%s\",\"status\":%d}", err, httpStatus.value());
        return response.writeWith(Mono.just(response.bufferFactory().wrap(errorBody.getBytes())));
    }

    @Override
    public int getOrder() {
        return -1; // High priority - run before other filters
    }
} 
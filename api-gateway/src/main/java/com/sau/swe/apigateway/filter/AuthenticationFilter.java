package com.sau.swe.apigateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AuthenticationFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        String method = request.getMethod().toString();

        log.info("AuthenticationFilter bypassed - Allowing request to pass: Path: {}, Method: {}", path, method);

        // Hiçbir doğrulama yapılmadan tüm istekler doğrudan geçer
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1; // Yüksek öncelikli, ama artık sadece loglama yapıyor
    }
}

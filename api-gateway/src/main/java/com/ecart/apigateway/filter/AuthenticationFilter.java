package com.ecart.apigateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    @Autowired
    private WebClient.Builder webClientBuilder;
    @Autowired
    private RouteValidator routeValidator;
    public AuthenticationFilter(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if(routeValidator.isSecured.test(exchange.getRequest())){
                if(!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("Missing authorization Header");
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if(authHeader != null && authHeader.startsWith("Bearer ")){
                    authHeader = authHeader.substring(7);
                    try {
                        String finalAuthHeader = authHeader;
                        webClientBuilder.build()
                                .get()
                                .uri("http://localhost:9094/auth/validate",
                                        uriBuilder -> uriBuilder.queryParam("token", finalAuthHeader).build()).retrieve();
                    } catch(Exception ex) {
                        throw new RuntimeException("unauthorized call");
                    }
                }
            }
            return chain.filter(exchange);
        });
    }

    public static class Config{

    }
}

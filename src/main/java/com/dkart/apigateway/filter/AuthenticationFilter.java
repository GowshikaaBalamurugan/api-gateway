package com.dkart.apigateway.filter;

import com.dkart.apigateway.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> implements GlobalFilter {
    private final RouteValidator validator;

    private final JwtUtil jwtUtil;


  //  private final UserServiceProxy userServiceProxy;
  //  private final RestTemplate restTemplate;
    private final Logger logger= LoggerFactory.getLogger(AuthenticationFilter.class);

    @Autowired
    public AuthenticationFilter(RouteValidator validator, JwtUtil jwtUtil) {
        super(Config.class);
        this.validator = validator;
        this.jwtUtil = jwtUtil;
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.info("Path of the request received -> {}",exchange.getRequest().getPath());
        logger.info(exchange.getRequest().getMethod().name());
        logger.info(exchange.getRequest().toString());
        //logger.info(exchange.getRequest().getMethod().name());
        return getVoidMono(exchange, chain, validator, jwtUtil);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> getVoidMono(exchange, chain,validator,jwtUtil));
    }



    public Mono<Void> getVoidMono(ServerWebExchange exchange, GatewayFilterChain chain, RouteValidator validator, JwtUtil jwtUtil) {
        if (validator.isSecured.test(exchange.getRequest())) {
            String path = exchange.getRequest().getPath().toString();
            //header contains token or not

            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                throw new RuntimeException("missing authorization header");
            }

            String authHeader = Objects.requireNonNull(exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION)).get(0);
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                authHeader = authHeader.substring(7);
            }
            try {
//                    //REST call to AUTH service--Getting circular dependency
                //   userServiceProxy.validateToken(authHeader);
                if(path.contains("/admin")){
                    Claims claims = jwtUtil.extractAllClaims(authHeader);
                    if(!claims.get("AUTH_ROLE").toString().equals("ADMIN")){
                        throw new RuntimeException("Unauthorized Request..");
                    }
                }
                jwtUtil.validateToken(authHeader);
               // userServiceProxy.validateToken(authHeader);
                 // restTemplate.getForObject("http://api-gateway//validate?token" + authHeader, String.class);

            } catch (Exception e) {
                System.out.println("invalid access...!");
                throw new RuntimeException("un authorized access to application");
            }
        }

        return chain.filter(exchange);
    }

    @Override
    public GatewayFilter apply(String routeId, Consumer<Config> consumer) {
        return super.apply(routeId, consumer);
    }

    @Override
    public GatewayFilter apply(Consumer<Config> consumer) {
        return super.apply(consumer);
    }


    @Override
    public GatewayFilter apply(String routeId, Config config) {
        return super.apply(routeId, config);
    }

    @Override
    public String name() {
        return super.name();
    }

    @Override
    public ShortcutType shortcutType() {
        return super.shortcutType();
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return super.shortcutFieldOrder();
    }

    @Override
    public String shortcutFieldPrefix() {
        return super.shortcutFieldPrefix();
    }


    public static class Config {

    }
}


package com.dkart.apigateway.proxy;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="api-gateway",path="/user-service")
public interface UserServiceProxy {
    @GetMapping("/validate")
    public ResponseEntity<Object> validateToken(@Valid @RequestParam("token") String token);
}

package com.dkart.apigateway.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="api-gateway",path="/user-service")
public interface UserServiceProxy {
    @GetMapping("/validate")
    public String validateToken(@RequestParam("token") String token);
}

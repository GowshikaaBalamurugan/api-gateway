package com.dkart.apigateway.advice;

import com.dkart.apigateway.dto.CustomErrorResponse;
import com.dkart.apigateway.exception.UnAuthorizedUserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ApiGatewayGlobalExceptionHandler {


    private final static String UNAUTH_USER="API-GATEWAY:UNAUTHORIZED_USER:403";
    private final static String COMMON_EX="API-GATEWAY:GENERAL_EXCEPTION:500";


    @ExceptionHandler(UnAuthorizedUserException.class)
    public ResponseEntity<?> handleUnAuthorizedUserException(UnAuthorizedUserException ex){
        CustomErrorResponse errorResponse= CustomErrorResponse.builder()
                .status(HttpStatus.FORBIDDEN)
                .errorCode(UNAUTH_USER)
                .errorMessage(ex.getMessage())
                .build()  ;
        log.error("ApiGatewayGlobalExceptionHandler::handleUnAuthorizedUserException exception caught {}",ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAllExceptions(Exception ex){
        CustomErrorResponse errorResponse= CustomErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .errorCode(COMMON_EX)
                .errorMessage(ex.getMessage())
                .build()  ;
        log.error("ApiGatewayGlobalExceptionHandler::handleAllExceptions exception caught {}",ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}

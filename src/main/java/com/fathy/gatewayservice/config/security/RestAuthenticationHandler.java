package com.fathy.gatewayservice.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fathy.gatewayservice.services.HttpErrorHandlingService;
import com.fathy.gatewayservice.common.enums.OrangeErrorInfo;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@ControllerAdvice
public class RestAuthenticationHandler implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;
    private final HttpErrorHandlingService errorHandlingService;

    public RestAuthenticationHandler(ObjectMapper objectMapper, HttpErrorHandlingService errorHandlingService) {
        this.objectMapper = objectMapper;
        this.errorHandlingService = errorHandlingService;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws IOException, InvalidTokenException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        if (authenticationException.getMessage().startsWith("Access token expired")) {
            response.getOutputStream().println(errorHandlingService.buildErrorResponseBody(request,
                    OrangeErrorInfo.EXPIRED_CREDENTIALS, "COMBO token expired"));
        } else {
            response.getOutputStream().println(errorHandlingService.buildErrorResponseBody(request,
                    OrangeErrorInfo.INVALID_CREDENTIALS, "Invalid COMBO token"));
        }
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public void commence(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getOutputStream().println(errorHandlingService.buildErrorResponseBody(request,
                OrangeErrorInfo.FORBIDDEN_USER, ex.getMessage()));
    }
}
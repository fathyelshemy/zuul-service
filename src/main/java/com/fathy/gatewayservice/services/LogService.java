package com.fathy.gatewayservice.services;

import com.fathy.gatewayservice.config.httpLoggingFilter.CustomHttpServletRequestWrapper;
import com.fathy.gatewayservice.config.httpLoggingFilter.CustomHttpServletResponseWrapper;


public interface LogService {
    void logRequest(CustomHttpServletRequestWrapper request);
    void logResponse(CustomHttpServletRequestWrapper request, CustomHttpServletResponseWrapper response, String body, String contentType);
}

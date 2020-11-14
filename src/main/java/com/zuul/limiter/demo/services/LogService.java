package com.zuul.limiter.demo.services;

import com.zuul.limiter.demo.httpLoggingFilter.CustomHttpServletRequestWrapper;
import com.zuul.limiter.demo.httpLoggingFilter.CustomHttpServletResponseWrapper;


public interface LogService {
    void logRequest(CustomHttpServletRequestWrapper request);
    void logResponse(CustomHttpServletRequestWrapper request, CustomHttpServletResponseWrapper response, String body, String contentType);
}

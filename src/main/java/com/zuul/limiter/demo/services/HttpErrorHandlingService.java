package com.zuul.limiter.demo.services;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuul.limiter.demo.dto.FieldErrorDTO;
import com.zuul.limiter.demo.exceptions.ErrorInfo;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * A service class used to handle operations when HTTP errors occur.
 * @since 1.1
 */
@Service
public class HttpErrorHandlingService {

    /**
     * builds a response body depending on the request context and client error
     *
     * @param request         The current HTTP request
     * @param errorInfo       the custom error code and message (ODI standardized)
     * @param errorMessage    the custom error description message
     * @return a string representing the response body, could be different depending on the request URL
     * @throws IOException
     */
    public String buildErrorResponseBody(HttpServletRequest request, ErrorInfo errorInfo, String errorMessage)
            throws IOException {
        //do further processing with request
        return buildErrorResponseBody(request.getRequestURI(), request.getMethod(), errorInfo, errorMessage);
    }

    public String buildErrorResponseBody(final String URI, final String httpMethod, ErrorInfo errorInfo, String errorMessage)
            throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

            return objectMapper.writeValueAsString(
                    new FieldErrorDTO(errorInfo.code, errorInfo.message, errorMessage));
    }

}
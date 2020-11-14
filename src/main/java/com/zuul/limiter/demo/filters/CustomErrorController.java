package com.zuul.limiter.demo.filters;

import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.support.RateLimitExceededException;
import com.netflix.zuul.exception.ZuulException;
import com.zuul.limiter.demo.exceptions.ComboHttpRequestException;
import com.zuul.limiter.demo.exceptions.ErrorInfo;
import com.zuul.limiter.demo.exceptions.ZuulRateLimitException;
import com.zuul.limiter.demo.services.HttpErrorHandlingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A custom error controller that extends Spring Boot default basic error controller
 * @since 2.1.4
 */
@Controller
public class CustomErrorController extends BasicErrorController {

    @Autowired
    private HttpErrorHandlingService httpErrorHandlingService;

    public CustomErrorController(ErrorAttributes errorAttributes, ServerProperties errorProperties, List<ErrorViewResolver> errorViewResolvers) {
        super(errorAttributes, errorProperties.getError(), errorViewResolvers);
    }

    @Override
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        Exception ex = ((Exception)request.getAttribute("javax.servlet.error.exception"));
        if(Objects.nonNull(ex)) {
            if(ex.getCause() instanceof MultipartException)
                throw new ComboHttpRequestException("The submitted value for content-type header isn't supported", request,
                        ErrorInfo.INTERNAL_ERROR);
            else if(ex instanceof ZuulException)
                if(new RateLimitExceededException().getMessage().contains(ex.getMessage()))
                    throw new ZuulRateLimitException("The application has made too many calls and has exceeded the rate limit for this service.",request,ErrorInfo.TOO_MANY_REQUESTS);
        }
        return super.error(request);
    }

    @ExceptionHandler(ComboHttpRequestException.class)
    public ResponseEntity<String> handleError(ComboHttpRequestException ex) {
        try {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(httpErrorHandlingService.buildErrorResponseBody(String.valueOf(ex.getRequest().getAttribute("javax.servlet.error.request_uri")),
                            ex.getRequest().getMethod(),
                            ex.getOrangeErrorInfo(),
                            ex.getMessage()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(ZuulRateLimitException.class)
    public ResponseEntity<String> handleZuulError(ZuulRateLimitException ex){
        try {

            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(httpErrorHandlingService.buildErrorResponseBody(String.valueOf(ex.getRequest().getAttribute("javax.servlet.error.request_uri")),
                            ex.getRequest().getMethod(),
                            ex.getOrangeErrorInfo(),
                            ex.getMessage()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();

    }

}

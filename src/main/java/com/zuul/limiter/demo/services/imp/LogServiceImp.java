package com.zuul.limiter.demo.services.imp;

import com.zuul.limiter.demo.httpLoggingFilter.CustomHttpServletRequestWrapper;
import com.zuul.limiter.demo.httpLoggingFilter.CustomHttpServletResponseWrapper;
import com.zuul.limiter.demo.services.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Component
public class LogServiceImp implements LogService {
    private static final Logger logger = LoggerFactory.getLogger("kibana-logger");
    private final String regex;

    public LogServiceImp(){
        this.regex = Stream
                .of(".*/v2/api-docs", ".*/swagger-resources", ".*/swagger-ui.html", ".*/webjars")
                .reduce((str1, str2) -> String.format("%s|%s", str1, str2))
                .map(str -> String.format("(%s).*", str))
                .orElse("");
    }

    @Override
    public void logRequest(CustomHttpServletRequestWrapper request) {
        final String path = request.getServletPath();
        if (!path.matches(this.regex)) {
            if (request.getHeader("host") != null) {
                final String host = request.getHeader("host");
                MDC.put("host", host);
            }

            /* request info */
            final String method = request.getMethod();
            final String url = request.getRequestURL().toString();
            logger.info("REST, request, {}, {}", method, url);

            /* request headers */
            final List<String> requestHeaders = new LinkedList<>();
            final Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                final String headerName = headerNames.nextElement();
                if (!headerName.equals("authorization")) {
                    final String headerValue = request.getHeader(headerName);
                    requestHeaders.add(String.format("%s: %s", headerName, headerValue));
                }
            }
            logger.info("REST, headers, {}", requestHeaders.toString());

            /* request data */
            final String requestData = request.getBody().replaceAll("\n", "");
            final StringBuilder stringBuilder = new StringBuilder();
            if (request.getParameterMap().size() > 0) {
                stringBuilder.append("{");
                request.getParameterMap().forEach((key, value) -> stringBuilder.append(key).append(": ").append(value[0]).append(", "));
                stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
                stringBuilder.append("}");
            }
            logger.info("REST, data, {}, {}", requestData, stringBuilder.toString());

        }


    }
    @Override
    public  void logResponse(CustomHttpServletRequestWrapper request, CustomHttpServletResponseWrapper response, String body, String contentType) {
        final String path = request.getServletPath();
        if (!path.matches(this.regex)) {
            final String removingWhitespacesRegex = "\n";

            final Integer status = response.getStatus();
            String responseData=(Objects.nonNull(body))? body.replaceAll(removingWhitespacesRegex, ""): null;
            contentType=(Objects.isNull(contentType))?"application/json":contentType;
            if (status < 400)
                logger.info("REST, response, {}, {}, {}", status, contentType, responseData);
            else
                logger.error("REST, response, {}, {}, {}", status, contentType, responseData);
        }
    }
}

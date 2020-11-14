package com.fathy.gatewayservice.filters;

import com.fathy.gatewayservice.services.LogService;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.fathy.gatewayservice.config.httpLoggingFilter.CustomHttpServletRequestWrapper;
import com.fathy.gatewayservice.config.httpLoggingFilter.CustomHttpServletResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ZuulLogResponseFilter extends ZuulFilter {
    private static final Logger logger = LoggerFactory.getLogger("kibana-logger");
    @Autowired
    private LogService logServiceImp;

    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        try {
            CustomHttpServletResponseWrapper response =  new CustomHttpServletResponseWrapper(ctx.getResponse());
            CustomHttpServletRequestWrapper request= new CustomHttpServletRequestWrapper(ctx.getRequest());
            String contentType=ctx.getResponse().getContentType();
            if(Objects.nonNull(ctx.getThrowable()) && ctx.getThrowable().getCause().getMessage().contains("429")) {
                String responseData="{ \"code\": \"53\", \"message\": \"Too many requests\", \"description\": \"The application has made too many calls and has exceeded the rate limit for this service\"}";
                ctx.setResponseBody(responseData);
//                logServiceImp.logResponse(request, response ,responseData,contentType);
            }
            } catch (Exception e) {
            logger.error("can't get response {}",e.getMessage());
        }
        return null;
    }
}

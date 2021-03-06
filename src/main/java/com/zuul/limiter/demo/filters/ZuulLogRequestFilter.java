package com.zuul.limiter.demo.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.zuul.limiter.demo.httpLoggingFilter.CustomHttpServletRequestWrapper;
import com.zuul.limiter.demo.services.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ZuulLogRequestFilter extends ZuulFilter {
    private static final Logger logger = LoggerFactory.getLogger("kibana-logger");

    @Autowired
    private LogService logServiceImp;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        try {
                CustomHttpServletRequestWrapper request= new CustomHttpServletRequestWrapper(ctx.getRequest());
                logServiceImp.logRequest(request);
            } catch (IOException e) {
            logger.error("can't get response {}",e.getMessage());
        }
        return null;
    }
}

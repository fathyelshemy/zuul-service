package com.fathy.gatewayservice.filters;

import com.fathy.gatewayservice.services.LogService;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.exception.ZuulException;
import com.fathy.gatewayservice.services.HttpErrorHandlingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

//@Component
public class ZuulLogErrorFilter extends ZuulFilter {
    private static final Logger logger = LoggerFactory.getLogger("kibana-logger");

    @Autowired
    private HttpErrorHandlingService errorHandlingService;

    @Autowired
    private LogService logServiceImp;

    @Override
    public String filterType() {
        return FilterConstants.ERROR_TYPE;
    }

    @Override
    public int filterOrder() {
        return -1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {

        return null;
    }
}

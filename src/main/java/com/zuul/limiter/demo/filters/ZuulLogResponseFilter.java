package com.zuul.limiter.demo.filters;

import com.google.common.io.CharStreams;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.zuul.limiter.demo.httpLoggingFilter.CustomHttpServletRequestWrapper;
import com.zuul.limiter.demo.httpLoggingFilter.CustomHttpServletResponseWrapper;
import com.zuul.limiter.demo.services.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;

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
            String responseData=  null;
            try {
                responseData = CharStreams.toString(new InputStreamReader(ctx.getResponseDataStream(), "UTF-8"));
                ctx.setResponseBody(responseData);
            }catch (Exception ex) { }
            String contentType=ctx.getResponse().getContentType();
            logServiceImp.logResponse(request, response ,responseData,contentType);


            } catch (IOException e) {
            logger.error("can't get response {}",e.getMessage());
        }
        return null;
    }
}

package com.fathy.gatewayservice.config.apiFactoryFilter;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class HttpRequestCustomFilter implements Filter {

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {

  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    CustomHeadersHttpServletRequestWrapper request = new CustomHeadersHttpServletRequestWrapper((HttpServletRequest) servletRequest);
    String comboToken = request.getHeader("Combo-Token");
    if(Objects.nonNull(comboToken)) {
      request.addHeader("Authorization", request.getHeader("Combo-Token"));
    }
    filterChain.doFilter(request, servletResponse);
  }

  @Override
  public void destroy() {

  }
}

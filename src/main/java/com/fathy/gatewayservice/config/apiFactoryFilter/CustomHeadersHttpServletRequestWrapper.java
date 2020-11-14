package com.fathy.gatewayservice.config.apiFactoryFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class CustomHeadersHttpServletRequestWrapper extends HttpServletRequestWrapper {
  private Map<String, String> headerMap = new HashMap<String, String>();

  public CustomHeadersHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
    super(request);
  }

  public void addHeader(String name, String value) {
    headerMap.put(name, value);
  }

  public void removeteHeader(String name) {
    headerMap.remove(name);
  }

  @Override
  public String getHeader(String name) {
    String headerValue = super.getHeader(name);
    if (headerMap.containsKey(name)) {
      headerValue = headerMap.get(name);
    }
    return headerValue;
  }

  @Override
  public Enumeration<String> getHeaderNames() {
    List<String> names = Collections.list(super.getHeaderNames());
    names = names.stream().filter(name -> !headerMap.containsKey(name)).collect(Collectors.toList());
    names.addAll(headerMap.keySet());
    return Collections.enumeration(names);
  }

  @Override
  public Enumeration<String> getHeaders(String name) {
    List<String> values = Collections.list(super.getHeaders(name));
    if (headerMap.containsKey(name)) {
      values.clear();
      values.add(headerMap.get(name));
    }
    return Collections.enumeration(values);
  }
}

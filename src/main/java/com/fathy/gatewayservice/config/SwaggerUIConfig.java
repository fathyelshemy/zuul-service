package com.fathy.gatewayservice.config;

import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SwaggerUIConfig {

  private final ZuulProperties zuulProperties;

  public SwaggerUIConfig(ZuulProperties zuulProperties) {
    this.zuulProperties = zuulProperties;
  }

  @Primary
  @Bean
  public SwaggerResourcesProvider swaggerResourcesProvider() {
    return () -> {
      List<SwaggerResource> resources = new ArrayList<>();
      zuulProperties.getRoutes().values().forEach(route -> resources.add(createResource(route.getId(), route.getId(), "2.0")));
      return resources;
    };
  }

  private SwaggerResource createResource(final String name, final String location, final String version) {
    SwaggerResource swaggerResource = new SwaggerResource();
    swaggerResource.setName(name);
    swaggerResource.setLocation("/" + location + "/v2/api-docs");
    swaggerResource.setSwaggerVersion(version);
    return swaggerResource;
  }
}

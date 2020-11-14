package com.fathy.gatewayservice.config;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MicrometerConfiguration {
  @Bean
  MeterRegistryCustomizer meterRegistryCustomizer(final MeterRegistry meterRegistry, @Value("${spring.application.name}") final String applicationName) {
    return meterRegistry1 -> meterRegistry.config().commonTags("application", applicationName);
  }
}

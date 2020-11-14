package com.fathy.gatewayservice.config;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FlywayConfig {

    @Bean
    public Flyway flyway(DataSource theDataSource) {
        Flyway flyway =  new Flyway();
        flyway.setDataSource(theDataSource);
        flyway.setLocations("classpath:db/migration");
        flyway.setTable("schema_version");
        flyway.repair();
        flyway.migrate();
        return flyway;
    }
}


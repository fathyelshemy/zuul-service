package com.fathy.gatewayservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.cloud.service.relational.DataSourceConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
@Profile("cloud")
public class CloudConfig extends AbstractCloudConfig {

    @Value("${cloudFoundry.postgresUrl}")
    private String databaseUrl;

    @Bean
    public DataSource postgresDataSource() {
        final String regex = "^(?:postgres|postgresql)://(.+):(.+)@(.+)$";

        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(databaseUrl);
        matcher.find();

        Map<String, Object> properties = new HashMap<>();
        properties.put("driverClassName", "org.postgresql.Driver");
        properties.put("url", String.format("jdbc:postgresql://%s", matcher.group(3)));
        properties.put("username", matcher.group(1));
        properties.put("password", matcher.group(2));

        DataSourceConfig dataSourceConfig = new DataSourceConfig(properties);
        return connectionFactory().dataSource(dataSourceConfig);
    }
}

package com.fathy.gatewayservice.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;

@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private final DefaultTokenServices defaultTokenServices;
    private final RestAuthenticationHandler restAuthenticationHandler;

    public ResourceServerConfig(DefaultTokenServices defaultTokenServices, RestAuthenticationHandler restAuthenticationHandler) {
        this.defaultTokenServices = defaultTokenServices;
        this.restAuthenticationHandler = restAuthenticationHandler;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests() // swagger-ui
              .antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security", "/swagger-ui.html",
                      "/webjars/**", "/token", "/user-management/token", "/gsma/**", "/*/v2/api-docs", "/*/doc",  "/*/monitor/*", "/*/status").permitAll()
              .and() /* testing */
              .authorizeRequests().antMatchers("/*/testing/**").permitAll()
              .and().authorizeRequests().anyRequest().authenticated()
              .and().csrf().disable();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenServices(this.defaultTokenServices);
        resources.authenticationEntryPoint(this.restAuthenticationHandler);
    }
}

package com.fathy.gatewayservice.config.security;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.io.IOException;

@Configuration
public class ResourceServerConfigBeans {

    @Bean
    @Primary
    public DefaultTokenServices defaultTokenServices(TokenStore tokenStore) {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore);
        return defaultTokenServices;
    }

    @Bean
    public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
        return new JwtTokenStore(jwtAccessTokenConverter);
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(CustomAccessTokenConverter customAccessTokenConverter, @Value("${auth.publicKeyName}") final String publicKeyName) {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setAccessTokenConverter(customAccessTokenConverter);
        Resource r = new ClassPathResource("publicKey/" + publicKeyName);
        String publicKey;
        try {
            publicKey = IOUtils.toString(r.getInputStream(), "UTF-8");
        } catch (final IOException e) {
            throw new InvalidTokenException("Can not convert access token");
        }
        jwtAccessTokenConverter.setVerifierKey(publicKey);

        return jwtAccessTokenConverter;
    }
}

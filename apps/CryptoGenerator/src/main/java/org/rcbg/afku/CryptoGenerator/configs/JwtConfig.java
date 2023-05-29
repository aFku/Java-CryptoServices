package org.rcbg.afku.CryptoGenerator.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;

@Configuration
public class JwtConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuer;

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwk;


    @Bean(name = "cryptoGeneratorJwtDecoder")
    JwtDecoder jwtDecoder(){
        return new CustomJwtDecoder(jwk, issuer);
    }

    public JwtDecoder getDecoder(){
        return this.jwtDecoder();
    }
}

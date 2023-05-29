package org.rcbg.afku.CryptoGenerator.configs;

import org.springframework.security.oauth2.jwt.*;

public class CustomJwtDecoder implements JwtDecoder {

    private final String jwkSetUri;
    private final String issuerUri;
    private final JwtDecoder delegate;

    public CustomJwtDecoder(String jwkSetUri, String issuerUri) {
        this.jwkSetUri = jwkSetUri;
        this.issuerUri = issuerUri;
        this.delegate = createDelegate();
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        return delegate.decode(token);
    }

    private JwtDecoder createDelegate() {
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
        decoder.setJwtValidator(JwtValidators.createDefaultWithIssuer(issuerUri));
        return decoder;
    }
}

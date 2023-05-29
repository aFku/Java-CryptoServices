package org.rcbg.afku.CryptoGenerator.integration_tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashMap;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class TestContainersBase {

    @Container
    static KeycloakContainer keycloak;

    static {
        keycloak = new KeycloakContainer("quay.io/keycloak/keycloak:21.1.1")
                .withRealmImportFile("/keycloak_realm.json");
    }

    private final RestTemplate restTemplate = new RestTemplate();

    protected String keycloakSecret = "secret_secret";
    protected String keycloakUsersPassword = "password";
    protected String keycloakGeneratorClientId = "cryptogenerator";
    protected String keycloakGrantType = "password";
    protected String keycloakAuthEndpointUrl = keycloak.getAuthServerUrl() + "realms/cryptoservices/protocol/openid-connect/token";

    @DynamicPropertySource
    static void registerResourceServerIssuerProperty(DynamicPropertyRegistry registry){
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri", () -> keycloak.getAuthServerUrl() + "realms/cryptoservices");
        registry.add("spring.security.oauth2.resourceserver.jwt.jwk-set-uri", () -> keycloak.getAuthServerUrl() + "realms/cryptoservices/protocol/openid-connect/certs");
    }

    protected String obtainJwtToken(KeycloakUser username){
        ResponseEntity<String> response = null;
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("username", username.name().toLowerCase());
            formData.add("password", this.keycloakUsersPassword);
            formData.add("client_id", this.keycloakGeneratorClientId);
            formData.add("grant_type", this.keycloakGrantType);
            formData.add("client_secret", this.keycloakSecret);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);
            response = restTemplate.postForEntity(this.keycloakAuthEndpointUrl, request, String.class);
            if (response.getStatusCode() != HttpStatus.OK) {
                Assertions.fail("Cannot get JWT token, http status: " + response.getStatusCode() + ", response: " + response.getBody());
            }
            return (String) new ObjectMapper().readValue(response.getBody(), HashMap.class).get("access_token");
        } catch (JsonProcessingException e){
            Assertions.fail("Cannot get JWT token, cannot parse response: " + response.getBody());
        }
        return null;
    }
}

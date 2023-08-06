package org.rcbg.afku.CryptoPass.integration_tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashMap;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestContainersBase {
    @Container
    protected static KeycloakContainer keycloak;

    @Container
    protected static MySQLContainer<?> mysql;

    static {
        mysql = new MySQLContainer<>("mysql:latest")
                .withPassword("testPassword")
                .withUsername("user")
                .withDatabaseName("cryptopass");
        mysql.start();
        keycloak = new KeycloakContainer("quay.io/keycloak/keycloak:21.1.1")
                .withRealmImportFile("/keycloak_realm.json");
        keycloak.start();
    }

    @DynamicPropertySource
    static void registerMySQLProperties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", () -> mysql.getJdbcUrl());
        registry.add("spring.datasource.username", () -> mysql.getUsername());
        registry.add("spring.datasource.password", () -> mysql.getPassword());
    }

    @DynamicPropertySource
    static void registerResourceServerIssuerProperty(DynamicPropertyRegistry registry){
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri", () -> keycloak.getAuthServerUrl() + "realms/cryptopass");
        registry.add("spring.security.oauth2.resourceserver.jwt.jwk-set-uri", () -> keycloak.getAuthServerUrl() + "realms/cryptopass/protocol/openid-connect/certs");
    }
    private final RestTemplate restTemplate = new RestTemplate();

    protected String keycloakSecret = "secret_secret";
    protected String keycloakUsersPassword = "password";
    protected String keycloakGeneratorClientId = "cryptopass";
    protected String keycloakGrantType = "password";
    protected String keycloakAuthEndpointUrl = keycloak.getAuthServerUrl() + "realms/cryptopass/protocol/openid-connect/token";

    protected MockMvc mockMvc;

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

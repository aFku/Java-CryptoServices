package org.rcbg.afku.CryptoGenerator.integration_tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.fabric8.kubernetes.client.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.rcbg.afku.CryptoGenerator.k8sClient.K8sCrdClientFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.k3s.K3sContainer;
import org.testcontainers.utility.DockerImageName;
import java.util.HashMap;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class TestContainersBase {

    @Container
    protected static KeycloakContainer keycloak;
    @Container
    protected static K3sContainer k3s;

    protected static Config config;

    static {
        keycloak = new KeycloakContainer("quay.io/keycloak/keycloak:21.1.1")
                .withRealmImportFile("/keycloak_realm.json");
        keycloak.start();
        k3s = new K3sContainer(DockerImageName.parse("rancher/k3s:v1.21.3-k3s1"));
        k3sInit();
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

    protected static void k3sInit(){
        // Init testcontainer and get config
        k3s.start();
        String kubeConfigYaml = k3s.getKubeConfigYaml();
        System.out.println(kubeConfigYaml);
        config = Config.fromKubeconfig(kubeConfigYaml);
        K8sCrdClientFactory k8sCrdClientFactory = new K8sCrdClientFactory(config);

        // Create all objects
        try (KubernetesClient client = new KubernetesClientBuilder().withConfig(config).build()) {
            client.namespaces().load(TestContainersBase.class.getResourceAsStream("/namespace.yaml")).create();
            client.apiextensions().v1().customResourceDefinitions().load(TestContainersBase.class.getResourceAsStream("/password-profile-crd.yaml")).create();
            k8sCrdClientFactory.passwordsClient().load(TestContainersBase.class.getResourceAsStream("/password-profile-resource.yaml")).create();
            client.apiextensions().v1().customResourceDefinitions().load(TestContainersBase.class.getResourceAsStream("/keys-profile-crd.yaml")).create();
            k8sCrdClientFactory.asymmetricKeysClient().load(TestContainersBase.class.getResourceAsStream("/keys-profile-resource.yaml")).create();
        }
    }
}

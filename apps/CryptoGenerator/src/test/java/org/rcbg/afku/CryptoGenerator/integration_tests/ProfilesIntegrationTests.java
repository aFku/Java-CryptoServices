package org.rcbg.afku.CryptoGenerator.integration_tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.NamespaceBuilder;
import io.fabric8.kubernetes.api.model.apiextensions.v1.CustomResourceDefinition;
import io.fabric8.kubernetes.api.model.apiextensions.v1.CustomResourceDefinitionList;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import org.apache.http.auth.AuthenticationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.rcbg.afku.CryptoGenerator.k8sClient.K8sCrdClientFactory;
import org.rcbg.afku.CryptoGenerator.k8sClient.models.PasswordProfile.PasswordProfileCR;
import org.rcbg.afku.CryptoGenerator.services.ProfilesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.output.OutputFrame;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.k3s.K3sContainer;
import org.testcontainers.utility.DockerImageName;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration
@ExtendWith(SpringExtension.class)
public class ProfilesIntegrationTests extends TestContainersBase{

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).apply(springSecurity()).build();
    }

    @Test
    public void testGetPasswordProfileByName() throws Exception {
        String jwt = obtainJwtToken(KeycloakUser.GENERATORUSER2);
        String profileName = "profile1";


        // Need rework to inject mocked kubernetes clients to profiles manager because we need client with specific config

        mockMvc.perform(get("/api/v1/profiles/passwords").param("profileName", profileName).header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt));
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.profileMetadata.profileName").value(profileName))
//                .andExpect(jsonPath("$.data.profileProperties.length").value(16));

        System.out.println(k3s.getLogs(OutputFrame.OutputType.STDOUT));
        System.out.println(k3s.getLogs(OutputFrame.OutputType.STDERR));
    }
}

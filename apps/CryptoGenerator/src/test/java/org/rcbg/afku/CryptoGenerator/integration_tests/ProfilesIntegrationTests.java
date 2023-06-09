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
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.rcbg.afku.CryptoGenerator.dtos.*;
import org.rcbg.afku.CryptoGenerator.k8sClient.K8sCrdClientFactory;
import org.rcbg.afku.CryptoGenerator.k8sClient.models.PasswordProfile.PasswordProfileCR;
import org.rcbg.afku.CryptoGenerator.services.ProfilesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProfilesIntegrationTests extends TestContainersBase{

    public PasswordProfileDTO createTestPasswordProfile(String name){
        PasswordProfileRequestBody data = new PasswordProfileRequestBody();
        data.setProfileName(name);
        data.setLength(10);
        data.setNumbersAllowed(true);
        data.setUppercaseAllowed(true);
        data.setSpecialCharsAllowed(true);
        data.setExcludedSpecialChars(".!");
        return profilesManager.createProfile(data, "testUser");
    }

    public AsymmetricKeysProfileDTO createTestKeysProfile(String name){
        AsymmetricKeysProfileRequestBody data = new AsymmetricKeysProfileRequestBody();
        data.setProfileName(name);
        data.setReturnBase64(false);
        data.setAlgorithm("DSA");
        return profilesManager.createProfile(data, "testUser");
    }

    @Test
    public void testGetPasswordProfileByName() throws Exception {
        String jwt = obtainJwtToken(KeycloakUser.GENERATORUSER2);
        String profileName = "profile1";

        mockMvc.perform(get("/api/v1/profiles/passwords").param("profileName", profileName).header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.profileMetadata.profileName").value(profileName))
                .andExpect(jsonPath("$.data.profileProperties.length").value(16));
    }

    @Test
    public void testGetPasswordProfileByNameAuthFail() throws Exception {
        String jwt = obtainJwtToken(KeycloakUser.GENERATORUSER1);
        String profileName = "profile1";

        mockMvc.perform(get("/api/v1/profiles/passwords").param("profileName", profileName).header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testGetPasswordProfileNotFound() throws Exception {
        String jwt = obtainJwtToken(KeycloakUser.GENERATORUSER2);
        String profileName = "profile12";

        mockMvc.perform(get("/api/v1/profiles/passwords").param("profileName", profileName).header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeletePasswordProfile() throws Exception {
        PasswordProfileDTO profile = this.createTestPasswordProfile("testname1");

        String jwt = obtainJwtToken(KeycloakUser.GENERATORUSER3);

        mockMvc.perform(delete("/api/v1/profiles/passwords").param("profileName", profile.getProfileMetadata().getProfileName()).header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeletePasswordProfileAuthFail() throws Exception {
        PasswordProfileDTO profile = this.createTestPasswordProfile("testname2");

        String jwt = obtainJwtToken(KeycloakUser.GENERATORUSER2);

        mockMvc.perform(delete("/api/v1/profiles/passwords").param("profileName", profile.getProfileMetadata().getProfileName()).header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testCreatePasswordProfile() throws Exception {
        PasswordProfileRequestBody data = new PasswordProfileRequestBody();
        data.setProfileName("testname3");
        data.setLength(10);
        data.setNumbersAllowed(true);
        data.setUppercaseAllowed(true);
        data.setSpecialCharsAllowed(true);
        data.setExcludedSpecialChars(".!");

        String jwt = obtainJwtToken(KeycloakUser.GENERATORUSER3);

        String content = this.mapper.writeValueAsString(data);
        mockMvc.perform(post("/api/v1/profiles/passwords").content(content).contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.profileMetadata.profileName").value(data.getProfileName()));
    }

    @Test
    public void testCreatePasswordConstraintViolation() throws Exception {
        PasswordProfileRequestBody data = new PasswordProfileRequestBody();
        data.setProfileName("testname4");
        data.setLength(256);
        data.setUppercaseAllowed(true);
        data.setSpecialCharsAllowed(true);
        data.setExcludedSpecialChars(".!");

        String jwt = obtainJwtToken(KeycloakUser.GENERATORUSER3);
        String[] expectedMessages = {
                "numbersAllowed cannot be null",
                "Password length should be between 6 and 255"
        };

        String content = this.mapper.writeValueAsString(data);
        mockMvc.perform(post("/api/v1/profiles/passwords").content(content).contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages.length()").value(expectedMessages.length))
                .andExpect(jsonPath("$.messages", Matchers.containsInAnyOrder(expectedMessages)));
    }

    @Test
    public void testCreatePasswordProfileNameAlreadyExist() throws Exception{
        createTestPasswordProfile("testname5");
        PasswordProfileRequestBody data = new PasswordProfileRequestBody();
        data.setProfileName("testname5");
        data.setLength(16);
        data.setNumbersAllowed(true);
        data.setUppercaseAllowed(true);
        data.setSpecialCharsAllowed(true);
        data.setExcludedSpecialChars(".!");

        String jwt = obtainJwtToken(KeycloakUser.GENERATORUSER3);

        String content = this.mapper.writeValueAsString(data);
        mockMvc.perform(post("/api/v1/profiles/passwords").content(content).contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.messages[0]").value("Profile with this name already exists."));
    }

    @Test
    public void testGetKeysProfileByName() throws Exception {
        String jwt = obtainJwtToken(KeycloakUser.GENERATORUSER2);
        String profileName = "profile1";

        mockMvc.perform(get("/api/v1/profiles/asymmetrics").param("profileName", profileName).header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.profileMetadata.profileName").value(profileName))
                .andExpect(jsonPath("$.data.profileProperties.algorithm").value("DSA"));
    }

    @Test
    public void testGetKeysProfileNotFound() throws Exception {
        String jwt = obtainJwtToken(KeycloakUser.GENERATORUSER2);
        String profileName = "profile12";

        mockMvc.perform(get("/api/v1/profiles/asymmetrics").param("profileName", profileName).header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteKeysProfile() throws Exception {
        AsymmetricKeysProfileDTO profile = this.createTestKeysProfile("testname1");

        String jwt = obtainJwtToken(KeycloakUser.GENERATORUSER3);

        mockMvc.perform(delete("/api/v1/profiles/asymmetrics").param("profileName", profile.getProfileMetadata().getProfileName()).header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteKeysProfileAuthFail() throws Exception {
        AsymmetricKeysProfileDTO profile = this.createTestKeysProfile("testname2");

        String jwt = obtainJwtToken(KeycloakUser.GENERATORUSER2);

        mockMvc.perform(delete("/api/v1/profiles/asymmetrics").param("profileName", profile.getProfileMetadata().getProfileName()).header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testCreateKeysProfile() throws Exception {
        AsymmetricKeysProfileRequestBody data = new AsymmetricKeysProfileRequestBody();
        data.setProfileName("testname3");
        data.setReturnBase64(false);
        data.setAlgorithm("DSA");

        String jwt = obtainJwtToken(KeycloakUser.GENERATORUSER3);

        String content = this.mapper.writeValueAsString(data);
        mockMvc.perform(post("/api/v1/profiles/asymmetrics").content(content).contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.profileMetadata.profileName").value(data.getProfileName()));
    }

    @Test
    public void testCreateKeysConstraintViolation() throws Exception {
        AsymmetricKeysProfileRequestBody data = new AsymmetricKeysProfileRequestBody();
        data.setProfileName("testname4");
        data.setAlgorithm("");

        String jwt = obtainJwtToken(KeycloakUser.GENERATORUSER3);
        String[] expectedMessages = {
                "returnBase64 cannot be null",
                "Algorithm cannot be empty"
        };

        String content = this.mapper.writeValueAsString(data);
        mockMvc.perform(post("/api/v1/profiles/asymmetrics").content(content).contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages.length()").value(expectedMessages.length))
                .andExpect(jsonPath("$.messages", Matchers.containsInAnyOrder(expectedMessages)));
    }

    @Test
    public void testCreateKeysProfileNameAlreadyExist() throws Exception{
        createTestKeysProfile("testname5");
        AsymmetricKeysProfileRequestBody data = new AsymmetricKeysProfileRequestBody();
        data.setProfileName("testname5");
        data.setReturnBase64(false);
        data.setAlgorithm("DSA");


        String jwt = obtainJwtToken(KeycloakUser.GENERATORUSER3);

        String content = this.mapper.writeValueAsString(data);
        mockMvc.perform(post("/api/v1/profiles/asymmetrics").content(content).contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.messages[0]").value("Profile with this name already exists."));
    }
}

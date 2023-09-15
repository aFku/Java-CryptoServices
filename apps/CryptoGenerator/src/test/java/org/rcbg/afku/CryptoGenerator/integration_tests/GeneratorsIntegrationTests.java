package org.rcbg.afku.CryptoGenerator.integration_tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rcbg.afku.CryptoGenerator.dtos.AsymmetricKeysProfileRequestBody;
import org.rcbg.afku.CryptoGenerator.dtos.PasswordProfileRequestBody;
import org.rcbg.afku.CryptoGenerator.k8sClient.K8sCrdClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class GeneratorsIntegrationTests extends TestContainersBase{

    @Test
    public void testGeneratePasswordWithProfileName() throws Exception {
        String profileName = "profile1";
        String jwt = obtainJwtToken(KeycloakUser.GENERATORUSER1);

        mockMvc.perform(get("/api/v1/generators/passwords").param("profileName", profileName).header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.password", Matchers.hasLength(16)));
    }

    @Test
    public void testGeneratePasswordWithRequestBody() throws Exception {
        PasswordProfileRequestBody data = new PasswordProfileRequestBody();
        data.setProfileName("profilerequestbody");
        data.setLength(10);
        data.setNumbersAllowed(true);
        data.setUppercaseAllowed(true);
        data.setSpecialCharsAllowed(true);
        data.setExcludedSpecialChars(".!");

        String jwt = obtainJwtToken(KeycloakUser.GENERATORUSER1);
        String content = mapper.writeValueAsString(data);

        mockMvc.perform(post("/api/v1/generators/passwords").content(content).contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.password", Matchers.hasLength(data.getLength())))
                .andExpect(jsonPath("$.data.profile.profileMetadata.profileName").value(data.getProfileName()));
    }

    @Test
    public void testGeneratePasswordProfileNameNotFound() throws Exception {
        String profileName = "notexist";
        String jwt = obtainJwtToken(KeycloakUser.GENERATORUSER1);

        mockMvc.perform(get("/api/v1/generators/passwords").param("profileName", profileName).header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGeneratePasswordRequestBodyConstraintViolation() throws Exception{
        PasswordProfileRequestBody data = new PasswordProfileRequestBody();
        data.setProfileName("profile1234");
        data.setLength(256);
        data.setUppercaseAllowed(true);
        data.setSpecialCharsAllowed(true);
        data.setExcludedSpecialChars(".!");

        String jwt = obtainJwtToken(KeycloakUser.GENERATORUSER1);
        String[] expectedMessages = {
                "numbersAllowed cannot be null",
                "Password length should be between 6 and 255"
        };

        String content = this.mapper.writeValueAsString(data);
        mockMvc.perform(post("/api/v1/generators/passwords").content(content).contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages.length()").value(expectedMessages.length))
                .andExpect(jsonPath("$.messages", Matchers.containsInAnyOrder(expectedMessages)));
    }

    @Test
    public void testGenerateKeysWithProfileName() throws Exception {
        String profileName = "profile1";
        String jwt = obtainJwtToken(KeycloakUser.GENERATORUSER1);

        mockMvc.perform(get("/api/v1/generators/asymmetrics").param("profileName", profileName).header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.publicKey", Matchers.notNullValue()))
                .andExpect(jsonPath("$.data.privateKey", Matchers.notNullValue()));
    }

    @Test
    public void testGenerateKeysWithRequestBody() throws Exception {
        AsymmetricKeysProfileRequestBody data = new AsymmetricKeysProfileRequestBody();
        data.setProfileName("testname3");
        data.setReturnBase64(false);
        data.setAlgorithm("DSA");

        String jwt = obtainJwtToken(KeycloakUser.GENERATORUSER1);
        String content = mapper.writeValueAsString(data);

        mockMvc.perform(post("/api/v1/generators/asymmetrics").content(content).contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.publicKey", Matchers.notNullValue()))
                .andExpect(jsonPath("$.data.privateKey", Matchers.notNullValue()))
                .andExpect(jsonPath("$.data.profile.profileMetadata.profileName").value(data.getProfileName()));
    }

    @Test
    public void testGenerateKeysProfileNameNotFound() throws Exception {
        String profileName = "notexist";
        String jwt = obtainJwtToken(KeycloakUser.GENERATORUSER1);

        mockMvc.perform(get("/api/v1/generators/asymmetrics").param("profileName", profileName).header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGenerateKeysRequestBodyConstraintViolation() throws Exception{
        AsymmetricKeysProfileRequestBody data = new AsymmetricKeysProfileRequestBody();
        data.setProfileName("testname4");
        data.setAlgorithm("");

        String jwt = obtainJwtToken(KeycloakUser.GENERATORUSER1);
        String[] expectedMessages = {
                "returnBase64 cannot be null",
                "Algorithm cannot be empty"
        };

        String content = this.mapper.writeValueAsString(data);
        mockMvc.perform(post("/api/v1/generators/asymmetrics").content(content).contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages.length()").value(expectedMessages.length))
                .andExpect(jsonPath("$.messages", Matchers.containsInAnyOrder(expectedMessages)));
    }

    @Test
    public void testGenerateKeysWithIncorrectAlgorithm() throws Exception {
        AsymmetricKeysProfileRequestBody data = new AsymmetricKeysProfileRequestBody();
        data.setProfileName("testname5");
        data.setReturnBase64(false);
        data.setAlgorithm("SDA");
        profilesManager.createProfile(data, "testUser");

        String jwt = obtainJwtToken(KeycloakUser.GENERATORUSER1);
        mockMvc.perform(get("/api/v1/generators/asymmetrics").param("profileName", data.getProfileName()).header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages[0]").value("Loaded profile has wrong algorithm name. No such algorithm: " + data.getAlgorithm()));
    }
}

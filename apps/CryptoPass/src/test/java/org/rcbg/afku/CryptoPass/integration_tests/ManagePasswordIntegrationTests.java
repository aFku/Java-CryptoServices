package org.rcbg.afku.CryptoPass.integration_tests;

import org.apache.http.HttpHeaders;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.rcbg.afku.CryptoPass.dto.SafeFetchResponseDto;
import org.rcbg.afku.CryptoPass.integration_tests.utils.KeycloakUser;
import org.rcbg.afku.CryptoPass.integration_tests.utils.WireMockConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ManagePasswordIntegrationTests extends TestContainersBase{

    @Test
    public void testSavePasswordConstraintViolation() throws Exception {
        String payload = generateSavePayloadAsString("x", "", "x".repeat(256), "123");
        String jwt = obtainJwtToken(KeycloakUser.PASSUSER2);

        Matcher<Iterable<?>> matcher = Matchers.containsInAnyOrder("Password length should be between 6 and 255",
                "Name cannot be empty", "Description cannot be longer than 255", "Key length should be between 6 and 32");

        mockMvc.perform(post("/api/v1/passwords")
                        .content(payload)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages", matcher));
    }

    @Test
    public void testSavePasswordOk() throws Exception {
        String payload = generateSavePayloadAsString("secretPassword1", "passwordName1", "desc", "secretKey123");
        String jwt = obtainJwtToken(KeycloakUser.PASSUSER2);

        mockMvc.perform(post("/api/v1/passwords")
                        .content(payload)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("passwordName1"))
                .andExpect(jsonPath("$.data.description").value("desc"))
                .andExpect(jsonPath("$.data.passwordId").isNumber());
    }

    @Test
    public void testSavePasswordUserHasNoAccessToStorage() throws Exception {
        String payload = generateSavePayloadAsString("secretPassword1", "passwordName1", "desc", "secretKey123");
        String jwt = obtainJwtToken(KeycloakUser.PASSUSER3);

        mockMvc.perform(post("/api/v1/passwords")
                        .content(payload)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testDeletePasswordWithValidKey() throws Exception {
        SafeFetchResponseDto password = createPasswordInStorage("secretPassword1", "secretKey1", "passwordName1", "desc", KeycloakUser.PASSUSER2.getUserId());
        String jwt = obtainJwtToken(KeycloakUser.PASSUSER2);

        mockMvc.perform(delete("/api/v1/passwords/" + password.getPasswordId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                        .header("ENCRYPTION-KEY", "secretKey1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeletePasswordWithInvalidKey() throws Exception {
        SafeFetchResponseDto password = createPasswordInStorage("secretPassword1", "secretKey1", "passwordName1", "desc", KeycloakUser.PASSUSER2.getUserId());
        String jwt = obtainJwtToken(KeycloakUser.PASSUSER2);

        mockMvc.perform(delete("/api/v1/passwords/" + password.getPasswordId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                        .header("ENCRYPTION-KEY", "notSecretKey321"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testDeletePasswordNotOwnedPasswordWithValidKey() throws Exception {
        SafeFetchResponseDto password = createPasswordInStorage("secretPassword1", "secretKey1", "passwordName1", "desc", KeycloakUser.PASSUSER2.getUserId());
        String jwt = obtainJwtToken(KeycloakUser.PASSUSER1);

        mockMvc.perform(delete("/api/v1/passwords/" + password.getPasswordId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                        .header("ENCRYPTION-KEY", "secretKey1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testDeletePasswordNotOwnedPasswordWithInvalidKey() throws Exception {
        SafeFetchResponseDto password = createPasswordInStorage("secretPassword1", "secretKey1", "passwordName1", "desc", KeycloakUser.PASSUSER2.getUserId());
        String jwt = obtainJwtToken(KeycloakUser.PASSUSER1);

        mockMvc.perform(delete("/api/v1/passwords/" + password.getPasswordId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                        .header("ENCRYPTION-KEY", "notSecretKey321"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testDeletePasswordUserHasNoAccessToStorage() throws Exception {
        SafeFetchResponseDto password = createPasswordInStorage("secretPassword1", "secretKey1", "passwordName1", "desc", KeycloakUser.PASSUSER2.getUserId());
        String jwt = obtainJwtToken(KeycloakUser.PASSUSER3);

        mockMvc.perform(delete("/api/v1/passwords/" + password.getPasswordId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                        .header("ENCRYPTION-KEY", "secretKey1"))
                .andExpect(status().isForbidden());
    }


}

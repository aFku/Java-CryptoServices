package org.rcbg.afku.CryptoPass.integration_tests;

import org.apache.http.HttpHeaders;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.rcbg.afku.CryptoPass.dto.SafeFetchResponseDto;
import org.rcbg.afku.CryptoPass.integration_tests.utils.KeycloakUser;
import org.rcbg.afku.CryptoPass.integration_tests.utils.WireMockConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FetchPasswordIntegrationTests extends TestContainersBase{

    @Test
    public void testGetAllPasswordsContainsUserData() throws Exception {
        SafeFetchResponseDto password1 = createPasswordInStorage("secretPassword1", "secretKey1", "name1", "desc", KeycloakUser.PASSUSER2.getUserId());
        SafeFetchResponseDto password2 = createPasswordInStorage("secretPassword2", "secretKey2", "name2", "desc", KeycloakUser.PASSUSER2.getUserId());
        createPasswordInStorage("secretPassword3", "secretKey3", "name3", "desc", KeycloakUser.PASSUSER3.getUserId());

        String jwt = obtainJwtToken(KeycloakUser.PASSUSER2);

        this.mockMvc.perform(get("/api/v1/passwords")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[*].name", Matchers.containsInAnyOrder(password1.getName(), password2.getName())));
    }

    @Test
    public void testGetAllPasswordsEmpty() throws Exception {
        SafeFetchResponseDto password1 = createPasswordInStorage("secretPassword1", "secretKey1", "name1", "desc", KeycloakUser.PASSUSER3.getUserId());
        SafeFetchResponseDto password2 = createPasswordInStorage("secretPassword2", "secretKey2", "name2", "desc", KeycloakUser.PASSUSER1.getUserId());
        createPasswordInStorage("secretPassword3", "secretKey3", "name3", "desc", KeycloakUser.PASSUSER3.getUserId());

        String jwt = obtainJwtToken(KeycloakUser.PASSUSER2);

        this.mockMvc.perform(get("/api/v1/passwords")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(0));
    }

    @Test
    public void testGetAllPasswordsUserPermissionsDenied() throws Exception {
        SafeFetchResponseDto password1 = createPasswordInStorage("secretPassword1", "secretKey1", "name1", "desc", KeycloakUser.PASSUSER3.getUserId());
        SafeFetchResponseDto password2 = createPasswordInStorage("secretPassword2", "secretKey2", "name2", "desc", KeycloakUser.PASSUSER3.getUserId());
        createPasswordInStorage("secretPassword3", "secretKey3", "name3", "desc", KeycloakUser.PASSUSER3.getUserId());

        String jwt = obtainJwtToken(KeycloakUser.PASSUSER3);

        this.mockMvc.perform(get("/api/v1/passwords")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testGetSpecificPasswordNotFound() throws Exception {
        String jwt = obtainJwtToken(KeycloakUser.PASSUSER2);

        this.mockMvc.perform(get("/api/v1/passwords/123")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                        .header("ENCRYPTION-KEY", "secretKey1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetSpecificPasswordFoundWithValidKey() throws Exception {
        SafeFetchResponseDto password = createPasswordInStorage("secretPassword1", "secretKey1", "name1", "desc", KeycloakUser.PASSUSER2.getUserId());
        String jwt = obtainJwtToken(KeycloakUser.PASSUSER2);

        this.mockMvc.perform(get("/api/v1/passwords/" + password.getPasswordId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                        .header("ENCRYPTION-KEY", "secretKey1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.password").value("secretPassword1"));
    }

    @Test
    public void testGetSpecificPasswordFoundWithInvalidKey() throws Exception {
        SafeFetchResponseDto password = createPasswordInStorage("secretPassword1", "secretKey1", "name1", "desc", KeycloakUser.PASSUSER2.getUserId());
        String jwt = obtainJwtToken(KeycloakUser.PASSUSER2);

        this.mockMvc.perform(get("/api/v1/passwords/" + password.getPasswordId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                        .header("ENCRYPTION-KEY", "NotSecretKey321"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetSpecificPasswordNotOwnedByUserWithValidKey() throws Exception {
        SafeFetchResponseDto password = createPasswordInStorage("secretPassword1", "secretKey1", "name1", "desc", KeycloakUser.PASSUSER2.getUserId());
        String jwt = obtainJwtToken(KeycloakUser.PASSUSER1);

        this.mockMvc.perform(get("/api/v1/passwords/" + password.getPasswordId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                        .header("ENCRYPTION-KEY", "secretKey1"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.messages[0]").value("You don't have password with such ID"));
    }

    @Test
    public void testGetSpecificPasswordNotOwnedByUserWithInvalidKey() throws Exception {
        SafeFetchResponseDto password = createPasswordInStorage("secretPassword1", "secretKey1", "name1", "desc", KeycloakUser.PASSUSER2.getUserId());
        String jwt = obtainJwtToken(KeycloakUser.PASSUSER1);

        this.mockMvc.perform(get("/api/v1/passwords/" + password.getPasswordId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                        .header("ENCRYPTION-KEY", "NotSecretKey321"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.messages[0]").value("You don't have password with such ID"));
    }

    @Test
    public void testGetSpecificPasswordUserHasNoAccessToService() throws Exception {
        String jwt = obtainJwtToken(KeycloakUser.PASSUSER3);

        this.mockMvc.perform(get("/api/v1/passwords/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
                .andExpect(status().isForbidden());
    }
}

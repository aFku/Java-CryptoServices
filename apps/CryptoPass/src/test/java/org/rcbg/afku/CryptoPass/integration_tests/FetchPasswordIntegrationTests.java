package org.rcbg.afku.CryptoPass.integration_tests;

import org.apache.http.HttpHeaders;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.rcbg.afku.CryptoPass.dto.PasswordSaveRequestDto;
import org.rcbg.afku.CryptoPass.dto.SafeFetchResponseDto;
import org.rcbg.afku.CryptoPass.services.PasswordManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { WireMockConfig.class })
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FetchPasswordIntegrationTests extends TestContainersBase{

    @Autowired
    private PasswordManagementService managementService;

    private SafeFetchResponseDto createPasswordInStorage(String password, String key, String name, String description, String userId) {
        PasswordSaveRequestDto dto = new PasswordSaveRequestDto();
        dto.setPassword(password);
        dto.setKey(key);
        dto.setName(name);
        dto.setDescription(description);

        return managementService.savePassword(dto, userId);
    }

    @BeforeEach
    public void cleanDb(){
        passwordRepository.deleteAll();
    }

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
}

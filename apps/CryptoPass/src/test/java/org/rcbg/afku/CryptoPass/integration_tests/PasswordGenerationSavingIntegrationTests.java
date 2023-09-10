package org.rcbg.afku.CryptoPass.integration_tests;

import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Test;
import org.rcbg.afku.CryptoPass.dto.PasswordGenerationSaveRequestDto;
import org.rcbg.afku.CryptoPass.integration_tests.TestContainersBase;
import org.rcbg.afku.CryptoPass.integration_tests.utils.GeneratorMocks;
import org.rcbg.afku.CryptoPass.integration_tests.utils.KeycloakUser;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PasswordGenerationSavingIntegrationTests extends TestContainersBase {

    private String generatePasswordSaveWithGenerationPayloadAsString(String name, String password, String description, String key, int length, boolean numbersAllowed, boolean uppercaseAllowed, boolean specialCharsAllowed, String excludedSpecialChars){
        return  "{" +
                "\"passwordData\": {" +
                "\"name\": \"" + name + "\"," +
                "\"password\": \"" + password + "\"," +
                "\"description\": \"" + description + "\"," +
                "\"key\": \"" + key + "\"" +
                "}," +
                "\"properties\": {" +
                "\"length\": \"" + length + "\"," +
                "\"numbersAllowed\": \"" + numbersAllowed + "\"," +
                "\"uppercaseAllowed\": \"" + uppercaseAllowed + "\"," +
                "\"specialCharsAllowed\": \"" + specialCharsAllowed + "\"," +
                "\"excludedSpecialChars\": \"" + excludedSpecialChars + "\"" +
                "}" +
                "}";
    }

    @Test
    public void testSaveGeneratedPasswordWithOkProfile() throws Exception {
        String payload = generateSavePayloadAsString(null, "passwordName1", "desc", "secretKey123");
        String jwt = obtainJwtToken(KeycloakUser.PASSUSER1);
        String profileName = "profile1";

        GeneratorMocks.setupOkResponseWithProfile(this.mockPasswordService);

        mockMvc.perform(post("/api/v1/passwords/profiles")
                        .content(payload)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .param("profileName", profileName))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("passwordName1"))
                .andExpect(jsonPath("$.data.description").value("desc"))
                .andExpect(jsonPath("$.data.passwordId").isNumber());
    }

    @Test
    public void testSaveGeneratedPasswordWithProfileNotFound() throws Exception {
        String payload = generateSavePayloadAsString(null, "passwordName1", "desc", "secretKey123");
        String jwt = obtainJwtToken(KeycloakUser.PASSUSER1);
        String profileName = "profile1";

        GeneratorMocks.setupProfileNotFoundResponse(this.mockPasswordService);

        mockMvc.perform(post("/api/v1/passwords/profiles")
                        .content(payload)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .param("profileName", profileName))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages[0]").value("Http client occurred an error: Password profile with name " + profileName +" cannot be found"));
    }

    @Test
    public void testSaveGeneratedPasswordWithOkProperties() throws Exception {
        String payload = generatePasswordSaveWithGenerationPayloadAsString("passwordName1", null, "desc", "secretKey123", 6, true, true, true, "");
        String jwt = obtainJwtToken(KeycloakUser.PASSUSER1);

        GeneratorMocks.setupOkResponseWithProperties(this.mockPasswordService);

        mockMvc.perform(post("/api/v1/passwords/properties")
                        .content(payload)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("passwordName1"))
                .andExpect(jsonPath("$.data.description").value("desc"))
                .andExpect(jsonPath("$.data.passwordId").isNumber());
    }

    @Test
    public void testSaveGeneratedPasswordWithIncorrectProperties() throws Exception {
        String payload = generatePasswordSaveWithGenerationPayloadAsString("passwordName1", null, "desc", "secretKey123", 2, true, true, true, "");
        String jwt = obtainJwtToken(KeycloakUser.PASSUSER1);

        GeneratorMocks.setupPropertiesIncorrectResponse(this.mockPasswordService);

        mockMvc.perform(post("/api/v1/passwords/properties")
                        .content(payload)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages[0]").value("Http client occurred an error: Password length should be between 6 and 255" ));
    }

    @Test
    public void testSaveGeneratedPasswordWithOkProfileWithNotAccessToGenerator() throws Exception {
        String payload = generateSavePayloadAsString(null, "passwordName1", "desc", "secretKey123");
        String jwt = obtainJwtToken(KeycloakUser.PASSUSER2);
        String profileName = "profile1";

        GeneratorMocks.setupUnauthorizedResponse(this.mockPasswordService);

        mockMvc.perform(post("/api/v1/passwords/profiles")
                        .content(payload)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .param("profileName", profileName))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages[0]").value("Http client occurred an error: Forbidden"));
    }

    @Test
    public void testSaveGeneratedPasswordWithOkProfileWithNotAccessToStorage() throws Exception {
        String payload = generateSavePayloadAsString(null, "passwordName1", "desc", "secretKey123");
        String jwt = obtainJwtToken(KeycloakUser.PASSUSER3);
        String profileName = "profile1";

        GeneratorMocks.setupUnauthorizedResponse(this.mockPasswordService);

        mockMvc.perform(post("/api/v1/passwords/profiles")
                        .content(payload)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .param("profileName", profileName))
                .andExpect(status().isForbidden());
    }
}

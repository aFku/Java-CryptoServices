package org.rcbg.afku.CryptoPass.integration_tests;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.smallrye.common.constraint.Assert;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.rcbg.afku.CryptoPass.domain.PasswordRepository;
import org.rcbg.afku.CryptoPass.dto.httpclient.PasswordsGeneratorResponse;
import org.rcbg.afku.CryptoPass.services.PasswordGeneratorClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { WireMockConfig.class })
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "feign.client.config.default.loggerLevel=FULL",
        "logging.level.org.rcbg.afku.CryptoPass.services.PasswordGeneratorClient=DEBUG"})
public class testTest extends TestContainersBase{

    @Autowired
    PasswordRepository repository;

    @Autowired
    private WireMockServer mockBooksService;

    @Autowired
    private PasswordGeneratorClient client;


    @Test
    public void test() throws Exception {
        KeycloakUser user = KeycloakUser.PASSUSER1;
        String jwt = obtainJwtToken(KeycloakUser.PASSUSER1);
        String profileName = "profileName1";
        String content = generateSavePayloadAsString("N/A", "myPassword", "No desc", "myKey123");
        GeneratorMocks.setupOkResponse(mockBooksService, profileName);

        this.mockMvc.perform(post("/api/v1/passwords/profiles")
                    .param("profileName", profileName)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                    .content(content).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.passwordId").value(1))
                .andExpect(jsonPath("$.data.name").value("myPassword"));

        Assert.assertNotNull(repository.getReferenceById(1));
    }
}

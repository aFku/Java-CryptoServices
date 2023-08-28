package org.rcbg.afku.CryptoPass.integration_tests;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.rcbg.afku.CryptoPass.exceptions.PasswordInternalError;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Objects;

@Slf4j
public class GeneratorMocks {

    private static String streamToString(InputStream stream){
        try {
            return IOUtils.toString(stream, StandardCharsets.UTF_8);
        } catch (IOException ex){
            log.error("Cannot convert inputstream: " + stream.toString());
            throw new PasswordInternalError();
        }
    }

    public static void setupOkResponse(WireMockServer mockService, String profileName) {
        mockService.stubFor(WireMock.get(WireMock.urlMatching("/api/v1/passwords.*")).withQueryParam("profileName", WireMock.anyUrl().getPattern())
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(
                                streamToString(Objects.requireNonNull(GeneratorMocks.class.getClassLoader().getResourceAsStream("payloads/ok_password.json")))
                        ))
        );
    }
}

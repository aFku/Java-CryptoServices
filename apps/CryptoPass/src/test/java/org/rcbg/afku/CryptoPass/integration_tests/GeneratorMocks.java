package org.rcbg.afku.CryptoPass.integration_tests;

import java.util.regex.Pattern;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Objects;

public class GeneratorMocks {

    public static void setupOkResponse(WireMockServer mockService, String profileName) {
        mockService.stubFor(WireMock.get(WireMock.urlMatching("/api/v1/passwords.*")).withQueryParam("profileName", WireMock.anyUrl().getPattern())
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(
                                Objects.requireNonNull(GeneratorMocks.class.getClassLoader().getResourceAsStream("payloads/ok_password.json")).toString()
                        ))
        );

    }
}

package org.rcbg.afku.CryptoPass.integration_tests.utils;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class WireMockConfig {

    //https://www.baeldung.com/spring-cloud-feign-integration-tests
    private static int port = 9561;

    @Bean(initMethod = "start", destroyMethod = "stop")
    public WireMockServer mockGeneratorService() {
        return new WireMockServer(port);
    }

    public static int getPort() {
        return port;
    }
}

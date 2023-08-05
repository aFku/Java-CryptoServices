package org.rcbg.afku.CryptoPass.services;

import lombok.extern.slf4j.Slf4j;
import org.rcbg.afku.CryptoPass.dto.httpclient.PasswordGeneratorProfileProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GeneratorClientService {

    private final PasswordGeneratorClient client;

    @Autowired
    GeneratorClientService(PasswordGeneratorClient client){
        this.client = client;
    }

    public String generatePasswordWithProperties(String jwt, PasswordGeneratorProfileProperties properties){
        log.debug("Request will be sent to generator for password from properties");
        String password = this.client.getPasswordBySpecification(properties, jwt).getData().getPassword();
        log.debug("Received password: " + password);
        return password;
    }

    public String generatePasswordWithProfileName(String jwt, String profileName){
        log.debug("Request will be sent to generator for password from profile name");
        String password = this.client.getPasswordByProfileName(profileName, jwt).getData().getPassword();
        log.debug("Received password: " + password);
        return password;
    }
}

package org.rcbg.afku.CryptoPass.services;

import org.rcbg.afku.CryptoPass.configs.PasswordGeneratorClientConfig;
import org.rcbg.afku.CryptoPass.dto.httpclient.PasswordGeneratorProfileProperties;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "PasswordGenerator", configuration = PasswordGeneratorClientConfig.class)
public interface PasswordGeneratorClient {
    @RequestMapping(method = RequestMethod.GET, value = "/passwords")
    String getPasswordByProfileName(@RequestParam("profileName") String profileName, @RequestHeader("Authorization") String jwtToken);

    @RequestMapping(method = RequestMethod.POST, value = "/passwords", consumes = "application/json")
    String getPasswordBySpecification(@RequestBody PasswordGeneratorProfileProperties properties, @RequestHeader("Authorization") String jwtToken);
}

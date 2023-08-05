package org.rcbg.afku.CryptoPass.services;

import org.rcbg.afku.CryptoPass.dto.httpclient.PasswordGeneratorProfileProperties;
import org.rcbg.afku.CryptoPass.dto.httpclient.PasswordsGeneratorResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "PasswordGenerator", url = "${crypto-services.cryptogenerator.url}")
public interface PasswordGeneratorClient {
    @RequestMapping(method = RequestMethod.GET, value = "/passwords")
    PasswordsGeneratorResponse getPasswordByProfileName(@RequestParam("profileName") String profileName, @RequestHeader("Authorization") String jwtToken);

    @RequestMapping(method = RequestMethod.POST, value = "/passwords", consumes = "application/json")
    PasswordsGeneratorResponse getPasswordBySpecification(@RequestBody PasswordGeneratorProfileProperties properties, @RequestHeader("Authorization") String jwtToken);
}

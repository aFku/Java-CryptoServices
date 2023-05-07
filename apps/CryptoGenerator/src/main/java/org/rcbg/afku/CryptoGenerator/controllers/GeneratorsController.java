package org.rcbg.afku.CryptoGenerator.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.rcbg.afku.CryptoGenerator.dtos.AsymmetricKeysProfileDTO;
import org.rcbg.afku.CryptoGenerator.dtos.AsymmetricKeysProfileRequestBody;
import org.rcbg.afku.CryptoGenerator.dtos.PasswordProfileDTO;
import org.rcbg.afku.CryptoGenerator.dtos.PasswordProfileRequestBody;
import org.rcbg.afku.CryptoGenerator.responses.asymmetrickeys.KeysResponse;
import org.rcbg.afku.CryptoGenerator.responses.password.PasswordResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/generators")
public class GeneratorsController {

    @GetMapping(value = "passwords") // Fill produces + consumes
    public ResponseEntity<PasswordResponse> getPasswordWithProfileName(HttpServletRequest request, @RequestParam String profileName){
        return null;
    }

    @PostMapping(value = "passwords")
    public ResponseEntity<PasswordResponse> getPasswordWithGivenSpec(HttpServletRequest request, @RequestBody PasswordProfileRequestBody requestBody){
        return null;
    }

    @GetMapping(value = "asymmetrics")
    public ResponseEntity<KeysResponse> getAsymmetricKeysWithProfileName(HttpServletRequest request, @RequestParam String profileName){
        return null;
    }

    @PostMapping(value = "asymmetrics")
    public ResponseEntity<KeysResponse> getAsymmetricKeysWithGivenSpec(HttpServletRequest request, @RequestBody AsymmetricKeysProfileRequestBody requestBody){
        return null;
    }
}

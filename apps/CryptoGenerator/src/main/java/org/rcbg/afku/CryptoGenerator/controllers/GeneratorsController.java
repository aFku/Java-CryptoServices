package org.rcbg.afku.CryptoGenerator.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/v1/generators")
public class GeneratorsController {

    @GetMapping(value = "passwords") // Fill produces + consumes
    public ResponseEntity<?> getPasswordWithProfileName(HttpServletRequest request, @RequestParam String profileName){
        return null;
    }

    @PostMapping(value = "passwords")
    public ResponseEntity<?> getPasswordWithGivenSpec(HttpServletRequest request, @RequestBody PasswordProfile spec){ // TO DO: Change PasswordProfile to something without metadata
        return null;
    }

    @GetMapping(value = "asymmetrics")
    public ResponseEntity<?> getAsymmetricKeysWithProfileName(HttpServletRequest request, @RequestParam String profileName){
        return null;
    }

    @PostMapping(value = "asymmetrics")
    public ResponseEntity<?> getAsymmetricKeysWithGivenSpec(HttpServletRequest request, @RequestBody AsymmetricKeysProfile spec){ // TO DO: Same as above
        return null;
    }
}

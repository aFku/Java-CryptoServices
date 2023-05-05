package org.rcbg.afku.CryptoGenerator.controllers;

import org.rcbg.afku.CryptoGenerator.dtos.AsymmetricKeysProfile;
import org.rcbg.afku.CryptoGenerator.dtos.PasswordProfile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profiles")
public class ProfilesController {

    @GetMapping(value = "passwords")
    public ResponseEntity<?> getPasswordProfile(@RequestParam String name){
        return null;
    }

    @PostMapping(value = "passwords")
    public ResponseEntity<?> createPasswordProfile(@RequestBody PasswordProfile spec){ // TO DO: Normalize PasswordProfile DTO with this from generators controller
        return null;
    }

    @DeleteMapping(value = "passwords")
    public ResponseEntity<?> deletePasswordProfile(@RequestParam String name){
        return null;
    }

    @GetMapping(value = "asymmetrics")
    public ResponseEntity<?> getAsymmetricKeysProfile(@RequestParam String name){
        return null;
    }

    @PostMapping(value = "asymmetrics")
    public ResponseEntity<?> createAsymmetricKeysProfile(@RequestBody AsymmetricKeysProfile spec){ // TO DO: Normalize AsymmetricKeysProfile DTO with this from generators controller
        return null;
    }

    @DeleteMapping(value = "asymmetrics")
    public ResponseEntity<?> deleteAsymmetricKeysProfile(@RequestParam String name){
        return null;
    }
}

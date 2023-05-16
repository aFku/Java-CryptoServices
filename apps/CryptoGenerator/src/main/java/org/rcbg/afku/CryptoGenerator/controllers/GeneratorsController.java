package org.rcbg.afku.CryptoGenerator.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.rcbg.afku.CryptoGenerator.dtos.*;
import org.rcbg.afku.CryptoGenerator.responses.ResponseMetadata;
import org.rcbg.afku.CryptoGenerator.responses.asymmetrickeys.KeysResponse;
import org.rcbg.afku.CryptoGenerator.responses.asymmetrickeys.KeysResponseBuilder;
import org.rcbg.afku.CryptoGenerator.responses.password.PasswordResponse;
import org.rcbg.afku.CryptoGenerator.responses.password.PasswordResponseBuilder;
import org.rcbg.afku.CryptoGenerator.services.GenerationService;
import org.rcbg.afku.CryptoGenerator.services.ProfilesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/generators")
public class GeneratorsController {

    private final GenerationService generationService;
    private final ProfilesManager profilesManager;

    @Autowired
    public GeneratorsController(GenerationService generationService, ProfilesManager profilesManager){
        this.generationService = generationService;
        this.profilesManager = profilesManager;
    }

    @GetMapping(value = "passwords", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PasswordResponse> getPasswordWithProfileName(HttpServletRequest request, @RequestParam String profileName){
        PasswordProfileDTO profileDTO = profilesManager.getPasswordProfileByName(profileName);
        String password = generationService.generatePassword(profileDTO);
        return new PasswordResponseBuilder()
                .withPassword(password)
                .withProfileDTO(profileDTO)
                .withMetadata(new ResponseMetadata(request.getRequestURI(), HttpStatus.OK.value(), MediaType.APPLICATION_JSON_VALUE))
                .withHttpStatus(HttpStatus.OK)
                .generate();
    }

    @PostMapping(value = "passwords", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PasswordResponse> getPasswordWithGivenSpec(HttpServletRequest request, @RequestBody PasswordProfileRequestBody requestBody){
        PasswordProfileDTO profileDTO = PasswordProfileMapper.INSTANCE.toFullDto(requestBody, "exampleUser"); // TO DO: user from jwt
        String password = generationService.generatePassword(profileDTO);
        return new PasswordResponseBuilder()
                .withPassword(password)
                .withProfileDTO(profileDTO)
                .withMetadata(new ResponseMetadata(request.getRequestURI(), HttpStatus.OK.value(), MediaType.APPLICATION_JSON_VALUE))
                .withHttpStatus(HttpStatus.OK)
                .generate();
    }

    @GetMapping(value = "asymmetrics", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<KeysResponse> getAsymmetricKeysWithProfileName(HttpServletRequest request, @RequestParam String profileName){
        AsymmetricKeysProfileDTO profileDTO = profilesManager.getAsymmetricKeysProfileByName(profileName);
        String[] keys = generationService.generateKeys(profileDTO);
        return new KeysResponseBuilder()
                .withKeys(keys[0], keys[1])
                .withAsymmetricKeysProfileDTO(profileDTO)
                .withMetadata(new ResponseMetadata(request.getRequestURI(), HttpStatus.OK.value(), MediaType.APPLICATION_JSON_VALUE))
                .withHttpStatus(HttpStatus.OK)
                .generate();
    }

    @PostMapping(value = "asymmetrics", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<KeysResponse> getAsymmetricKeysWithGivenSpec(HttpServletRequest request, @RequestBody AsymmetricKeysProfileRequestBody requestBody){
        AsymmetricKeysProfileDTO profileDTO = AsymmetricKeysProfileMapper.INSTANCE.toFullDto(requestBody, "exampleUser"); // TO DO: user from JWT
        String[] keys = generationService.generateKeys(profileDTO);
        return new KeysResponseBuilder()
                .withKeys(keys[0], keys[1])
                .withAsymmetricKeysProfileDTO(profileDTO)
                .withMetadata(new ResponseMetadata(request.getRequestURI(), HttpStatus.OK.value(), MediaType.APPLICATION_JSON_VALUE))
                .withHttpStatus(HttpStatus.OK)
                .generate();
    }
}

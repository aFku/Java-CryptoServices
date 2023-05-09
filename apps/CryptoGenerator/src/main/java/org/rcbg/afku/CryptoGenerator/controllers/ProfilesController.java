package org.rcbg.afku.CryptoGenerator.controllers;

import com.nimbusds.common.contenttype.ContentType;
import jakarta.servlet.http.HttpServletRequest;
import org.rcbg.afku.CryptoGenerator.dtos.AsymmetricKeysProfileDTO;
import org.rcbg.afku.CryptoGenerator.dtos.AsymmetricKeysProfileRequestBody;
import org.rcbg.afku.CryptoGenerator.dtos.PasswordProfileDTO;
import org.rcbg.afku.CryptoGenerator.dtos.PasswordProfileRequestBody;
import org.rcbg.afku.CryptoGenerator.responses.ResponseMetadata;
import org.rcbg.afku.CryptoGenerator.responses.asymmetrickeys.KeysProfileResponse;
import org.rcbg.afku.CryptoGenerator.responses.password.PasswordProfileResponse;
import org.rcbg.afku.CryptoGenerator.services.ProfilesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profiles")
public class ProfilesController {

    private final ProfilesManager profilesManager;

    @Autowired
    public ProfilesController(ProfilesManager profilesManager){
        this.profilesManager = profilesManager;
    }

    @GetMapping(value = "passwords")
    public ResponseEntity<?> getPasswordProfile(HttpServletRequest request, @RequestParam String name){
        PasswordProfileDTO dto = profilesManager.getPasswordProfileByName(name);
        ResponseMetadata metadata = new ResponseMetadata(request.getRequestURI(), HttpStatus.OK.value(), ContentType.APPLICATION_JSON.toString());
        return new ResponseEntity<>(new PasswordProfileResponse(dto, metadata), new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping(value = "passwords")
    public ResponseEntity<?> createPasswordProfile(HttpServletRequest request, @RequestBody PasswordProfileRequestBody requestBody){
        PasswordProfileDTO dto = profilesManager.createProfile(requestBody, "exampleUser"); // TO DO: Add user identification
        ResponseMetadata metadata = new ResponseMetadata(request.getRequestURI(), HttpStatus.OK.value(), ContentType.APPLICATION_JSON.toString());
        return new ResponseEntity<>(new PasswordProfileResponse(dto, metadata), new HttpHeaders(), HttpStatus.OK);
    }

    @DeleteMapping(value = "passwords")
    public ResponseEntity<?> deletePasswordProfile(@RequestParam String name){
        profilesManager.deletePasswordProfileByName(name);
        return new ResponseEntity<>(new HttpHeaders(), HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "asymmetrics")
    public ResponseEntity<?> getAsymmetricKeysProfile(HttpServletRequest request, @RequestParam String name){
        AsymmetricKeysProfileDTO dto = profilesManager.getAsymmetricKeysProfileByName(name);
        ResponseMetadata metadata = new ResponseMetadata(request.getRequestURI(), HttpStatus.OK.value(), ContentType.APPLICATION_JSON.toString());
        return new ResponseEntity<>(new KeysProfileResponse(dto, metadata), new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping(value = "asymmetrics")
    public ResponseEntity<?> createAsymmetricKeysProfile(HttpServletRequest request, @RequestBody AsymmetricKeysProfileRequestBody requestBody){
        AsymmetricKeysProfileDTO dto = profilesManager.createProfile(requestBody, "exampleUser"); // TO DO: Add user identification
        ResponseMetadata metadata = new ResponseMetadata(request.getRequestURI(), HttpStatus.OK.value(), ContentType.APPLICATION_JSON.toString());
        return new ResponseEntity<>(new KeysProfileResponse(dto, metadata), new HttpHeaders(), HttpStatus.OK);
    }

    @DeleteMapping(value = "asymmetrics")
    public ResponseEntity<?> deleteAsymmetricKeysProfile(@RequestParam String name){
        profilesManager.deleteAsymmetricKeysProfileByName(name);
        return new ResponseEntity<>(new HttpHeaders(), HttpStatus.NO_CONTENT);
    }
}

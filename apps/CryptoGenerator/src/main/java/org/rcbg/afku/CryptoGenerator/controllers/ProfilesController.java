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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;

@RestController
@RequestMapping("/api/v1/profiles")
public class ProfilesController {

    private final ProfilesManager profilesManager;

    @Autowired
    public ProfilesController(ProfilesManager profilesManager){
        this.profilesManager = profilesManager;
    }

    @GetMapping(value = "passwords", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPasswordProfile(HttpServletRequest request, @RequestParam String profileName){
        PasswordProfileDTO dto = profilesManager.getPasswordProfileByName(profileName);
        ResponseMetadata metadata = new ResponseMetadata(request.getRequestURI(), HttpStatus.OK.value(), MediaType.APPLICATION_JSON_VALUE);
        return new ResponseEntity<>(new PasswordProfileResponse(dto, metadata), new HttpHeaders(), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_GeneratorsProfilesAdmin')")
    @PostMapping(value = "passwords", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createPasswordProfile(HttpServletRequest request, @RequestBody PasswordProfileRequestBody requestBody, Authentication authentication){
        PasswordProfileDTO dto = profilesManager.createProfile(requestBody, authentication.getName());
        ResponseMetadata metadata = new ResponseMetadata(request.getRequestURI(), HttpStatus.OK.value(), MediaType.APPLICATION_JSON_VALUE);
        return new ResponseEntity<>(new PasswordProfileResponse(dto, metadata), new HttpHeaders(), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_GeneratorsProfilesAdmin')")
    @DeleteMapping(value = "passwords")
    public ResponseEntity<?> deletePasswordProfile(@RequestParam String profileName){
        profilesManager.deletePasswordProfileByName(profileName);
        return new ResponseEntity<>(new HttpHeaders(), HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "asymmetrics", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAsymmetricKeysProfile(HttpServletRequest request, @RequestParam String profileName){
        AsymmetricKeysProfileDTO dto = profilesManager.getAsymmetricKeysProfileByName(profileName);
        ResponseMetadata metadata = new ResponseMetadata(request.getRequestURI(), HttpStatus.OK.value(), MediaType.APPLICATION_JSON_VALUE);
        return new ResponseEntity<>(new KeysProfileResponse(dto, metadata), new HttpHeaders(), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_GeneratorsProfilesAdmin')")
    @PostMapping(value = "asymmetrics", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createAsymmetricKeysProfile(HttpServletRequest request, @RequestBody AsymmetricKeysProfileRequestBody requestBody, Authentication authentication){
        AsymmetricKeysProfileDTO dto = profilesManager.createProfile(requestBody, authentication.getName());
        ResponseMetadata metadata = new ResponseMetadata(request.getRequestURI(), HttpStatus.OK.value(), MediaType.APPLICATION_JSON_VALUE);
        return new ResponseEntity<>(new KeysProfileResponse(dto, metadata), new HttpHeaders(), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_GeneratorsProfilesAdmin')")
    @DeleteMapping(value = "asymmetrics")
    public ResponseEntity<?> deleteAsymmetricKeysProfile(@RequestParam String profileName){
        profilesManager.deleteAsymmetricKeysProfileByName(profileName);
        return new ResponseEntity<>(new HttpHeaders(), HttpStatus.NO_CONTENT);
    }
}

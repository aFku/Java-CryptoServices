package org.rcbg.afku.CryptoPass.controllers;

import org.apache.coyote.Response;
import org.rcbg.afku.CryptoPass.dto.FullFetchResponseDto;
import org.rcbg.afku.CryptoPass.dto.PasswordSaveRequestDto;
import org.rcbg.afku.CryptoPass.dto.SafeFetchResponseDto;
import org.rcbg.afku.CryptoPass.services.PasswordManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/passwords")
public class DirectStorageController { // TODO: Factory for responses

    private final PasswordManagementService managementService;

    @Autowired
    public DirectStorageController(PasswordManagementService managementService){
        this.managementService = managementService;
    }

    @GetMapping
    public ResponseEntity<?> getAllUserPasswords(Pageable pageable){ // TODO: Auth - get userID
        List<SafeFetchResponseDto> listOfPasswords = managementService.getListOfPasswords("{PLACEHOLDER}", pageable).getContent();
        return new ResponseEntity<>(listOfPasswords, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSpecificPasswordByID(@PathVariable int id, @RequestHeader("ENCRYPTION-KEY") String key) { // TODO: Auth
        var dto = managementService.getPassword(id, key, "{PLACEHOLDER}");
        return new ResponseEntity<>(dto, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> savePassword(@RequestBody PasswordSaveRequestDto requestDto){ // TODO: Auth
        var responseDto = managementService.savePassword(requestDto, "{PLACEHOLDER}");
        return new ResponseEntity<>(responseDto, new HttpHeaders(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePassword(@PathVariable int id, @RequestHeader("ENCRYPTION-KEY") String key){ // TODO: Auth
        managementService.deletePassword(id, key, "{PLACEHOLDER}");
        return new ResponseEntity<>(new HttpHeaders(), HttpStatus.NO_CONTENT);
    }


}

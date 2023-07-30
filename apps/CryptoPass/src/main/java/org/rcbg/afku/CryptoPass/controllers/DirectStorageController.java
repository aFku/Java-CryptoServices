package org.rcbg.afku.CryptoPass.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.Response;
import org.rcbg.afku.CryptoPass.dto.FullFetchResponseDto;
import org.rcbg.afku.CryptoPass.dto.PasswordSaveRequestDto;
import org.rcbg.afku.CryptoPass.dto.SafeFetchResponseDto;
import org.rcbg.afku.CryptoPass.responses.FullFetchResponse;
import org.rcbg.afku.CryptoPass.responses.ResponseFactory;
import org.rcbg.afku.CryptoPass.responses.SafeFetchResponse;
import org.rcbg.afku.CryptoPass.responses.SafePaginationResponse;
import org.rcbg.afku.CryptoPass.services.PasswordManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/passwords")
public class DirectStorageController {

    private final PasswordManagementService managementService;

    @Autowired
    public DirectStorageController(PasswordManagementService managementService){
        this.managementService = managementService;
    }

    @GetMapping
    public ResponseEntity<SafePaginationResponse> getAllUserPasswords(HttpServletRequest request, Pageable pageable){ // TODO: Auth - get userID
        Page<SafeFetchResponseDto> pageOfPasswords = managementService.getListOfPasswords("{PLACEHOLDER}", pageable);
        return ResponseFactory.createSafePaginationResponse(request.getRequestURI(), HttpStatus.OK, pageOfPasswords);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FullFetchResponse> getSpecificPasswordByID(HttpServletRequest request, @PathVariable int id, @RequestHeader("ENCRYPTION-KEY") String key) { // TODO: Auth
        FullFetchResponseDto dto = managementService.getPassword(id, key, "{PLACEHOLDER}");
        return ResponseFactory.createFullFetchResponse(request.getRequestURI(), HttpStatus.OK, dto);
    }

    @PostMapping
    public ResponseEntity<SafeFetchResponse> savePassword(HttpServletRequest request, @RequestBody PasswordSaveRequestDto requestDto){ // TODO: Auth
        SafeFetchResponseDto responseDto = managementService.savePassword(requestDto, "{PLACEHOLDER}");
        return ResponseFactory.createSafeFetchResponse(request.getRequestURI(), HttpStatus.OK, responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePassword(HttpServletRequest request, @PathVariable int id, @RequestHeader("ENCRYPTION-KEY") String key){ // TODO: Auth
        managementService.deletePassword(id, key, "{PLACEHOLDER}");
        return new ResponseEntity<>(new HttpHeaders(), HttpStatus.NO_CONTENT);
    }


}

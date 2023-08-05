package org.rcbg.afku.CryptoPass.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.rcbg.afku.CryptoPass.dto.FullFetchResponseDto;
import org.rcbg.afku.CryptoPass.dto.PasswordGenerationSaveRequestDto;
import org.rcbg.afku.CryptoPass.dto.PasswordSaveRequestDto;
import org.rcbg.afku.CryptoPass.dto.SafeFetchResponseDto;
import org.rcbg.afku.CryptoPass.responses.FullFetchResponse;
import org.rcbg.afku.CryptoPass.responses.ResponseFactory;
import org.rcbg.afku.CryptoPass.responses.SafeFetchResponse;
import org.rcbg.afku.CryptoPass.responses.SafePaginationResponse;
import org.rcbg.afku.CryptoPass.services.GeneratorClientService;
import org.rcbg.afku.CryptoPass.services.PasswordManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/passwords")
public class ManagementController {

    private final PasswordManagementService managementService;

    private final GeneratorClientService generatorClientService;

    @Autowired
    public ManagementController(PasswordManagementService managementService, GeneratorClientService generatorClientService){
        this.managementService = managementService;
        this.generatorClientService = generatorClientService;
    }

    @GetMapping
    public ResponseEntity<SafePaginationResponse> getAllUserPasswords(HttpServletRequest request, Pageable pageable, Authentication authentication){
        Page<SafeFetchResponseDto> pageOfPasswords = managementService.getListOfPasswords(authentication.getName(), pageable);
        return ResponseFactory.createSafePaginationResponse(request.getRequestURI(), HttpStatus.OK, pageOfPasswords);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FullFetchResponse> getSpecificPasswordByID(HttpServletRequest request, @PathVariable int id, @RequestHeader("ENCRYPTION-KEY") String key, Authentication authentication) {
        FullFetchResponseDto dto = managementService.getPassword(id, key, authentication.getName());
        return ResponseFactory.createFullFetchResponse(request.getRequestURI(), HttpStatus.OK, dto);
    }

    @PostMapping
    public ResponseEntity<SafeFetchResponse> savePassword(HttpServletRequest request, @RequestBody PasswordSaveRequestDto requestDto, Authentication authentication){
        SafeFetchResponseDto responseDto = managementService.savePassword(requestDto, authentication.getName());
        return ResponseFactory.createSafeFetchResponse(request.getRequestURI(), HttpStatus.OK, responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePassword(HttpServletRequest request, @PathVariable int id, @RequestHeader("ENCRYPTION-KEY") String key, Authentication authentication){
        managementService.deletePassword(id, key, authentication.getName());
        return new ResponseEntity<>(new HttpHeaders(), HttpStatus.NO_CONTENT);
    }

    @PostMapping
    public ResponseEntity<SafeFetchResponse> savePassword(HttpServletRequest request, @RequestBody PasswordGenerationSaveRequestDto requestDto, Authentication authentication){
        String password = generatorClientService.generatePasswordWithProperties(request.getHeader("Authentication"), requestDto.getProperties());
        PasswordSaveRequestDto passwordData = requestDto.getPasswordData();
        passwordData.setPassword(password);
        SafeFetchResponseDto responseDto = managementService.savePassword(passwordData, authentication.getName());
        return ResponseFactory.createSafeFetchResponse(request.getRequestURI(), HttpStatus.OK, responseDto);
    }

    @PostMapping
    public ResponseEntity<SafeFetchResponse> savePassword(HttpServletRequest request, @RequestBody PasswordSaveRequestDto requestDto, @RequestParam("profileName") String profileName, Authentication authentication){
        String password = generatorClientService.generatePasswordWithProfileName(request.getHeader("Authentication"), profileName);
        requestDto.setPassword(password);
        SafeFetchResponseDto responseDto = managementService.savePassword(requestDto, authentication.getName());
        return ResponseFactory.createSafeFetchResponse(request.getRequestURI(), HttpStatus.OK, responseDto);
    }
}

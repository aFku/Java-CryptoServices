package org.rcbg.afku.CryptoPass.controllers;

import lombok.extern.slf4j.Slf4j;
import org.rcbg.afku.CryptoPass.exceptions.PasswordAccessDenied;
import org.rcbg.afku.CryptoPass.exceptions.PasswordInternalError;
import org.rcbg.afku.CryptoPass.exceptions.PasswordNotFound;
import org.rcbg.afku.CryptoPass.services.PasswordManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerAdvisor { // TO DO: Create responses factory and log there captured exceptions

    @ExceptionHandler(PasswordNotFound.class)
    public ResponseEntity<?> passwordNotFoundHandler(PasswordNotFound ex){
        return new ResponseEntity<>(ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PasswordAccessDenied.class)
    public ResponseEntity<?> passwordAccessDeniedHandler(PasswordAccessDenied ex){
        return new ResponseEntity<>(ex.getMessage(), new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(PasswordInternalError.class)
    public ResponseEntity<?> passwordInternalErrorHandler(PasswordInternalError ex){
        return new ResponseEntity<>(ex.getMessage(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

package org.rcbg.afku.CryptoPass.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.rcbg.afku.CryptoPass.exceptions.PasswordAccessDenied;
import org.rcbg.afku.CryptoPass.exceptions.PasswordInternalError;
import org.rcbg.afku.CryptoPass.exceptions.PasswordNotFound;
import org.rcbg.afku.CryptoPass.responses.ErrorResponse;
import org.rcbg.afku.CryptoPass.responses.ResponseFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(PasswordNotFound.class)
    public ResponseEntity<ErrorResponse> passwordNotFoundHandler(PasswordNotFound ex, HttpServletRequest request){
        log.error("PasswordNotFound: " + ex.getMessage());
        return ResponseFactory.createErrorResponse(request.getRequestURI(), HttpStatus.NOT_FOUND, new ArrayList<>(Collections.singleton(ex.getMessage())));
    }

    @ExceptionHandler(PasswordAccessDenied.class)
    public ResponseEntity<?> passwordAccessDeniedHandler(PasswordAccessDenied ex, HttpServletRequest request){
        log.error("PasswordAccessDenied: " + ex.getMessage());
        return ResponseFactory.createErrorResponse(request.getRequestURI(), HttpStatus.UNAUTHORIZED, new ArrayList<>(Collections.singleton(ex.getMessage())));
    }

    @ExceptionHandler(PasswordInternalError.class)
    public ResponseEntity<?> passwordInternalErrorHandler(PasswordInternalError ex, HttpServletRequest request){
        log.error("InternalServerError: " + ex.getMessage());
        return ResponseFactory.createErrorResponse(request.getRequestURI(), HttpStatus.INTERNAL_SERVER_ERROR, new ArrayList<>(Collections.singleton("Internal server error")));
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ErrorResponse> constraintViolationHandler(ConstraintViolationException ex, HttpServletRequest request){
        List<String> messages = ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).toList();
        log.error("ConstraintViolationException: " + messages.toString());
        return ResponseFactory.createErrorResponse(request.getRequestURI(), HttpStatus.BAD_REQUEST, messages);
    }
}

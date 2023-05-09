package org.rcbg.afku.CryptoGenerator.controllers;

import io.fabric8.kubernetes.client.KubernetesClientException;
import jakarta.servlet.http.HttpServletRequest;
import org.rcbg.afku.CryptoGenerator.exceptions.unchecked.CheckedExceptionWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice{

    private static final Logger logger = LoggerFactory.getLogger(ControllerAdvice.class);

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({CheckedExceptionWrapper.class})
    private ResponseEntity<?> handleCheckedExceptionWrapper(CheckedExceptionWrapper ex, HttpServletRequest request){
        logger.error("Error type: " + ex.getCheckedName() + ", message: " + ex.getMessage());
        return new ResponseEntity<>("Internal error", new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({KubernetesClientException.class})
    private ResponseEntity<?> handleAllK8sClientErrors(KubernetesClientException ex){
        String x = ex.getStatus().getMessage();
        return null;
    }


}

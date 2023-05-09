package org.rcbg.afku.CryptoGenerator.controllers;

import io.fabric8.kubernetes.client.KubernetesClientException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ConstraintViolation;
import org.rcbg.afku.CryptoGenerator.exceptions.unchecked.CheckedExceptionWrapper;
import org.rcbg.afku.CryptoGenerator.responses.ResponseMetadata;
import org.rcbg.afku.CryptoGenerator.responses.error.ErrorResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ControllerAdvice{

    private static final Logger logger = LoggerFactory.getLogger(ControllerAdvice.class);

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({CheckedExceptionWrapper.class})
    private ResponseEntity<?> handleCheckedExceptionWrapper(CheckedExceptionWrapper ex, HttpServletRequest request){
        logger.error("Error type: " + ex.getCheckedName() + ", message: " + ex.getMessage());
        return new ErrorResponseBuilder()
                .withHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .addMessage(ex.getMessage())
                .withMetadata(new ResponseMetadata(request.getRequestURI(), HttpStatus.INTERNAL_SERVER_ERROR.value(), MediaType.APPLICATION_JSON_VALUE))
                .generate();
    }

    @ExceptionHandler({KubernetesClientException.class})
    private ResponseEntity<?> handleAllK8sClientErrors(HttpServletRequest request, KubernetesClientException ex){
        logger.error("Error type: KubernetesClientException, message: " + ex.getMessage());
        HttpStatus httpStatus = HttpStatus.valueOf(ex.getCode());
        return new ErrorResponseBuilder()
                .withHttpStatus(httpStatus)
                .addMessage(ex.getMessage())
                .withMetadata(new ResponseMetadata(request.getRequestURI(), httpStatus.value(), MediaType.APPLICATION_JSON_VALUE))
                .generate();
    }

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request){
        List<String> messages = ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).toList();
        logger.error("Constraint violation: " + messages.toString());
        ErrorResponseBuilder builder = new ErrorResponseBuilder()
                .withHttpStatus(HttpStatus.BAD_REQUEST)
                .withMetadata(new ResponseMetadata(request.getRequestURI(), HttpStatus.BAD_REQUEST.value(), MediaType.APPLICATION_JSON_VALUE));
        for(String message: messages){
            builder.addMessage(message);
        }
        return builder.generate();
    }


}

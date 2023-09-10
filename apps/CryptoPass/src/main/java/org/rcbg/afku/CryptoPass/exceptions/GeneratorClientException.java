package org.rcbg.afku.CryptoPass.exceptions;

import feign.Response;
import org.springframework.http.HttpStatus;

public class GeneratorClientException extends GeneratorBaseException{
    public GeneratorClientException(String targetUrl, String errorMessage, HttpStatus status) {
        super(targetUrl, errorMessage, status);
    }
}

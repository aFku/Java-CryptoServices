package org.rcbg.afku.CryptoPass.exceptions;

import feign.Response;
import org.springframework.http.HttpStatus;

public class GeneratorServerException extends GeneratorBaseException{
    public GeneratorServerException(String targetUrl, String errorMessage, HttpStatus status) {
        super(targetUrl, errorMessage, status);
    }
}

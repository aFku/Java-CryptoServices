package org.rcbg.afku.CryptoPass.exceptions;

import feign.Response;
import org.springframework.http.HttpStatus;

public class GeneratorServerException extends GeneratorBaseException{
    public GeneratorServerException(String targetUrl, Response.Body requestBody, HttpStatus status) {
        super(targetUrl, requestBody, status);
    }
}

package org.rcbg.afku.CryptoPass.exceptions;

import feign.Response;
import org.springframework.http.HttpStatus;

public class GeneratorClientException extends GeneratorBaseException{
    public GeneratorClientException(String targetUrl, Response.Body requestBody, HttpStatus status) {
        super(targetUrl, requestBody, status);
    }
}

package org.rcbg.afku.CryptoPass.exceptions;

import feign.Response;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
public class GeneratorBaseException extends RuntimeException{
    protected String targetUrl;
    protected Response.Body responseBody;
    protected HttpStatus status;

    public GeneratorBaseException(String targetUrl, Response.Body responseBody, HttpStatus status) {
        this.targetUrl = targetUrl;
        this.responseBody = responseBody;
        this.status = status;
    }
}

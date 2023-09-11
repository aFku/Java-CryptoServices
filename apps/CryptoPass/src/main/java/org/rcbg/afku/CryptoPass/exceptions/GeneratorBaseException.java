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
    protected String errorMessage;
    protected HttpStatus status;

    public GeneratorBaseException(String targetUrl, String errorMessage, HttpStatus status) {
        this.targetUrl = targetUrl;
        this.errorMessage = errorMessage;
        this.status = status;
    }
}

package org.rcbg.afku.CryptoGenerator.responses.error;

import org.rcbg.afku.CryptoGenerator.responses.ResponseMetadata;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ErrorResponseBuilder {
    private HttpStatus status;
    private HttpHeaders headers;
    private ErrorResponse content;

    public ErrorResponseBuilder withMetadata(ResponseMetadata metadata){
        this.content.setMetadata(metadata);
        return this;
    }

    public ErrorResponseBuilder withHeaders(HttpHeaders headers){
        this.headers = headers;
        return this;
    }

    public ErrorResponseBuilder withHttpStatus(HttpStatus status){
        this.status = status;
        return this;
    }

    public ErrorResponseBuilder addMessage(String message){
        this.content.addMessage(message);
        return this;
    }
    public ResponseEntity<ErrorResponse> generate(){
        return new ResponseEntity<>(this.content, this.headers, this.status);
    }

    public void reset(){
        this.content = new ErrorResponse();
        this.headers = new HttpHeaders();
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public ErrorResponseBuilder(){
        this.reset();
    }
}

package org.rcbg.afku.CryptoGenerator.responses.password;

import org.rcbg.afku.CryptoGenerator.dtos.PasswordProfileDTO;
import org.rcbg.afku.CryptoGenerator.responses.ResponseMetadata;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class PasswordResponseBuilder {

    private HttpStatus status;
    private HttpHeaders headers;
    private PasswordResponseContent content;
    private ResponseMetadata metadata;

    public PasswordResponseBuilder withMetadata(ResponseMetadata metadata){
        this.metadata = metadata;
        return this;
    }

    public PasswordResponseBuilder withProfileDTO(PasswordProfileDTO profileDTO){
        this.content.setProfile(profileDTO);
        return this;
    }

    public PasswordResponseBuilder withPassword(String password){
        this.content.setPassword(password);
        return this;
    }

    public PasswordResponseBuilder withHeaders(HttpHeaders headers){
        this.headers = headers;
        return this;
    }

    public PasswordResponseBuilder withHttpStatus(HttpStatus status){
        this.status = status;
        return this;
    }
    public ResponseEntity<PasswordResponse> generate(){
        return new ResponseEntity<>(new PasswordResponse(this.metadata, this.content), this.headers, this.status);
    }

    public void reset(){
        this.metadata = new ResponseMetadata();
        this.content = new PasswordResponseContent();
        this.headers = new HttpHeaders();
        this.status = HttpStatus.OK;
    }

    public PasswordResponseBuilder(){
        this.reset();
    }
}

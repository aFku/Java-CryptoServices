package org.rcbg.afku.CryptoGenerator.responses.asymmetrickeys;

import org.rcbg.afku.CryptoGenerator.dtos.AsymmetricKeysProfileDTO;
import org.rcbg.afku.CryptoGenerator.responses.ResponseMetadata;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class KeysResponseBuilder {
    private HttpStatus status;
    private HttpHeaders headers;
    private KeysResponseContent content;
    private ResponseMetadata metadata;

    public KeysResponseBuilder withMetadata(ResponseMetadata metadata){
        this.metadata = metadata;
        return this;
    }

    public KeysResponseBuilder withAsymmetricKeysProfileDTO(AsymmetricKeysProfileDTO profileDTO){
        this.content.setProfile(profileDTO);
        return this;
    }

    public KeysResponseBuilder withKeys(String publicKey, String privateKey){
        this.content.setPublicKey(publicKey);
        this.content.setPrivateKey(privateKey);
        return this;
    }

    public KeysResponseBuilder withHeaders(HttpHeaders headers){
        this.headers = headers;
        return this;
    }

    public KeysResponseBuilder withHttpStatus(HttpStatus status){
        this.status = status;
        return this;
    }
    public ResponseEntity<KeysResponse> generate(){
        return new ResponseEntity<>(new KeysResponse(this.metadata, this.content), this.headers, this.status);
    }

    public void reset(){
        this.metadata = new ResponseMetadata();
        this.content = new KeysResponseContent();
        this.headers = new HttpHeaders();
        this.status = HttpStatus.OK;
    }

    public KeysResponseBuilder(){
        this.reset();
    }
}

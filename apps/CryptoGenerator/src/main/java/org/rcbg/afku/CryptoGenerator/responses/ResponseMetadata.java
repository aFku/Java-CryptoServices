package org.rcbg.afku.CryptoGenerator.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class ResponseMetadata {
    private String uri;
    private int statusCode;
    private String timestamp;
    private String contentType;

    public ResponseMetadata(String uri, int statusCode, String contentType){
        this.timestamp = Instant.now().toString();
        this.uri = uri;
        this.statusCode = statusCode;
        this.contentType = contentType;
    }
}

package org.rcbg.afku.CryptoPass.configs;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.rcbg.afku.CryptoPass.exceptions.GeneratorClientException;
import org.rcbg.afku.CryptoPass.exceptions.GeneratorServerException;
import org.springframework.http.HttpStatus;

public class FeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response){
        String requestUrl = response.request().url();
        Response.Body responseBody = response.body();
        HttpStatus responseStatus = HttpStatus.valueOf(response.status());
        if (responseStatus.is5xxServerError()) {
            return new GeneratorServerException(requestUrl, responseBody, responseStatus);
        } else if (responseStatus.is4xxClientError()) {
            return new GeneratorClientException(requestUrl, responseBody, responseStatus);
        } else {
            return new Exception("Generic exception");
        }
    }
}

package org.rcbg.afku.CryptoPass.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nimbusds.jose.shaded.gson.JsonObject;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.CharStreams;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.rcbg.afku.CryptoPass.exceptions.GeneratorClientException;
import org.rcbg.afku.CryptoPass.exceptions.GeneratorServerException;
import org.rcbg.afku.CryptoPass.exceptions.PasswordInternalError;
import org.rcbg.afku.CryptoPass.responses.ErrorResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response){
        String requestUrl = response.request().url();
        String errorMessage = parseErrorMessages(response.body());
        HttpStatus responseStatus = HttpStatus.valueOf(response.status());
        if (responseStatus.is5xxServerError()) {
            return new GeneratorServerException(requestUrl, errorMessage, responseStatus);
        } else if (responseStatus.is4xxClientError()) {
            return new GeneratorClientException(requestUrl, errorMessage, responseStatus);
        } else {
            return new Exception("Generic exception");
        }
    }

    private String parseErrorMessages(Response.Body body){
        try{
            String stringBody = CharStreams.fromReader(body.asReader(Charset.defaultCharset())).toString();
            JSONParser parser = new JSONParser(stringBody);
            LinkedHashMap<String, Object> json = parser.parseObject();
            return ((List<String>) json.get("messages")).get(0);
        } catch (IOException ex) {
            log.error("Cannot read Reader: " + ex + " - " + body);
            throw new PasswordInternalError();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}

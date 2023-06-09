package org.rcbg.afku.CryptoGenerator.responses.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.rcbg.afku.CryptoGenerator.responses.ResponseMetadata;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private List<String> messages = new ArrayList<>();
    private ResponseMetadata metadata;

    public ErrorResponse(ResponseMetadata metaData) {
        this.metadata = metaData;
    }

    public void addMessage(String message){
        messages.add(message);
    }
}

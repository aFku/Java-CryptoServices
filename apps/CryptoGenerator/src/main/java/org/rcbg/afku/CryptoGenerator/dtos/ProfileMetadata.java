package org.rcbg.afku.CryptoGenerator.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.time.Instant;

@Getter
public class ProfileMetadata {

    private final String profileName;

    private final String creationTimestamp;

    private final String creatorUserId;

    public ProfileMetadata(String profileName, String creatorUserId){
        this.profileName = profileName;
        this.creatorUserId = creatorUserId;
        this.creationTimestamp = Instant.now().toString();
    }
}

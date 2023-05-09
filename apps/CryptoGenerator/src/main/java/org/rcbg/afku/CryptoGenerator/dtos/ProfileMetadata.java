package org.rcbg.afku.CryptoGenerator.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor
public class ProfileMetadata {

    private String profileName;

    private final String creationTimestamp = Instant.now().toString();

    private String creatorUserId;

    public ProfileMetadata(String profileName, String creatorUserId){
        this.profileName = profileName;
        this.creatorUserId = creatorUserId;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public void setCreatorUserId(String creatorUserId) {
        this.creatorUserId = creatorUserId;
    }
}

package org.rcbg.afku.CryptoGenerator.k8sClient.models.PasswordProfile;

import org.rcbg.afku.CryptoGenerator.dtos.PasswordProfileDTO;
import org.rcbg.afku.CryptoGenerator.dtos.PasswordProfileProperties;
import org.rcbg.afku.CryptoGenerator.dtos.ProfileMetadata;

public class PasswordProfileSpec extends PasswordProfileDTO{
    public PasswordProfileSpec(){
        super();
    }

    public PasswordProfileSpec(ProfileMetadata profileMetadata, PasswordProfileProperties profileProperties) {
        super(profileMetadata, profileProperties);
    }
}
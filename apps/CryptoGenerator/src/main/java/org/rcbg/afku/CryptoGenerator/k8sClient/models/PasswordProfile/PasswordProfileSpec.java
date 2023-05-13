package org.rcbg.afku.CryptoGenerator.k8sClient.models.PasswordProfile;

import io.fabric8.kubernetes.api.model.KubernetesResource;
import org.rcbg.afku.CryptoGenerator.dtos.PasswordProfileDTO;
import org.rcbg.afku.CryptoGenerator.dtos.PasswordProfileProperties;
import org.rcbg.afku.CryptoGenerator.dtos.ProfileMetadata;

public class PasswordProfileSpec extends PasswordProfileDTO implements KubernetesResource {
    public PasswordProfileSpec(){
        super();
    }

    public PasswordProfileSpec(PasswordProfileProperties profileProperties, ProfileMetadata metadata){
        this.setProfileProperties(profileProperties);
        this.setMetadata(metadata);
    }

    public void setProfileMetadata(ProfileMetadata metadata){
        this.setMetadata(metadata);
    }
}
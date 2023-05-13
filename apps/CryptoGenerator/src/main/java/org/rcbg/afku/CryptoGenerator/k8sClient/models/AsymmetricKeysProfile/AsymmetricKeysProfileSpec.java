package org.rcbg.afku.CryptoGenerator.k8sClient.models.AsymmetricKeysProfile;

import io.fabric8.kubernetes.api.model.KubernetesResource;
import org.rcbg.afku.CryptoGenerator.dtos.AsymmetricKeysProfileDTO;
import org.rcbg.afku.CryptoGenerator.dtos.AsymmetricKeysProfileProperties;
import org.rcbg.afku.CryptoGenerator.dtos.ProfileMetadata;

public class AsymmetricKeysProfileSpec extends AsymmetricKeysProfileDTO{
    public AsymmetricKeysProfileSpec(ProfileMetadata profileMetadata, AsymmetricKeysProfileProperties profileProperties) {
        super(profileMetadata, profileProperties);
    }

    public AsymmetricKeysProfileSpec() {
        super();
    }
}

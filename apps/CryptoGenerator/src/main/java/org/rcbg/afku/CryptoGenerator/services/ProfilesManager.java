package org.rcbg.afku.CryptoGenerator.services;

import io.fabric8.kubernetes.api.model.runtime.RawExtension;
import io.fabric8.kubernetes.client.utils.Serialization;
import jakarta.validation.Valid;
import org.rcbg.afku.CryptoGenerator.dtos.*;
import org.rcbg.afku.CryptoGenerator.exceptions.unchecked.ProfileNotFound;
import org.rcbg.afku.CryptoGenerator.k8sClient.K8sCrdClientFactory;
import org.rcbg.afku.CryptoGenerator.k8sClient.models.AsymmetricKeysProfile.AsymmetricKeysProfileCR;
import org.rcbg.afku.CryptoGenerator.k8sClient.models.PasswordProfile.PasswordProfileCR;
import org.rcbg.afku.CryptoGenerator.k8sClient.models.PasswordProfile.PasswordProfileSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class ProfilesManager {

    @Value("${crd.namespace}")
    private String namespace;

    private final static Logger logger = LoggerFactory.getLogger(ProfilesManager.class);

    private PasswordProfileCR getPasswordProfileCR(String name){
        PasswordProfileCR cr = K8sCrdClientFactory.getPasswordProfileClient().inNamespace(this.namespace).withName(name).get();
        if(cr == null){
            throw new ProfileNotFound("Password profile with name '" + name +"' cannot be found");
        }
        logger.debug("Got password profile from cluster: " + cr.toString());
        return cr;
    }

    private AsymmetricKeysProfileCR getAsymmetricKeysProfileCR(String name){
        AsymmetricKeysProfileCR cr = K8sCrdClientFactory.getAsymmetricKeysProfileClient().inNamespace(this.namespace).withName(name).get();
        if(cr == null){
            throw new ProfileNotFound("Asymmetric keys profile with name '" + name +"' cannot be found");
        }
        logger.debug("Got asymmetric keys profile from cluster: " + cr.toString());
        return cr;
    }

    @Validated
    public PasswordProfileDTO createProfile(@Valid PasswordProfileRequestBody requestBody, String userId){
        PasswordProfileCR cr = PasswordProfileMapper.INSTANCE.createProfileResource(requestBody, userId);
        cr = K8sCrdClientFactory.getPasswordProfileClient().inNamespace(this.namespace).resource(cr).create();
        PasswordProfileDTO dto = PasswordProfileMapper.INSTANCE.specToDto(cr.getSpec());
        logger.info("Password profile has been created: " + dto.toString());
        return dto;
    }

    @Validated
    public AsymmetricKeysProfileDTO createProfile(@Valid AsymmetricKeysProfileRequestBody requestBody, String userId){
        AsymmetricKeysProfileCR cr = AsymmetricKeysProfileMapper.INSTANCE.createProfileResource(requestBody, userId);
        cr = K8sCrdClientFactory.getAsymmetricKeysProfileClient().inNamespace(this.namespace).resource(cr).create();
        AsymmetricKeysProfileDTO dto = AsymmetricKeysProfileMapper.INSTANCE.specToDto(cr.getSpec());
        logger.info("Keys profile has been created: " + dto.toString());
        return dto;
    }

    public PasswordProfileDTO getPasswordProfileByName(String name){
        PasswordProfileCR cr = this.getPasswordProfileCR(name);
        return PasswordProfileMapper.INSTANCE.specToDto(cr.getSpec());
    }

    public AsymmetricKeysProfileDTO getAsymmetricKeysProfileByName(String name){
        AsymmetricKeysProfileCR cr = this.getAsymmetricKeysProfileCR(name);
        return AsymmetricKeysProfileMapper.INSTANCE.specToDto(cr.getSpec());
    }

    public void deletePasswordProfileByName(String name){
        PasswordProfileCR cr = this.getPasswordProfileCR(name);
        K8sCrdClientFactory.getPasswordProfileClient().inNamespace(this.namespace).resource(cr).delete();
        logger.info("Password profile with name: " + name + " has been deleted");
    }

    public void deleteAsymmetricKeysProfileByName(String name){
        AsymmetricKeysProfileCR cr = this.getAsymmetricKeysProfileCR(name);
        K8sCrdClientFactory.getAsymmetricKeysProfileClient().inNamespace(this.namespace).resource(cr).delete();
        logger.info("Keys profile with name: " + name + " has been deleted");
    }


}

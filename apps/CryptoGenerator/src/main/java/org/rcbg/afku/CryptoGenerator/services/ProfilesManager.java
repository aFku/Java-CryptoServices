package org.rcbg.afku.CryptoGenerator.services;

import io.fabric8.kubernetes.api.model.runtime.RawExtension;
import io.fabric8.kubernetes.client.utils.Serialization;
import jakarta.validation.Valid;
import org.rcbg.afku.CryptoGenerator.dtos.*;
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
public class ProfilesManager {

    @Value("${crd.namespace}")
    private String namespace;

    private final static Logger logger = LoggerFactory.getLogger(ProfilesManager.class);

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
        logger.info("List all: " + K8sCrdClientFactory.getPasswordProfileClient().inAnyNamespace().list().toString()); // Debug
        logger.info("List in namespace: " + K8sCrdClientFactory.getPasswordProfileClient().inNamespace(this.namespace).list().toString()); // Debug
        PasswordProfileCR cr = K8sCrdClientFactory.getPasswordProfileClient().inNamespace(this.namespace).withName(name).get(); // Sprawdzenie czy istnieje wgl. Jeśli nie istnieje to zwraca null a nie żaden Not found error (Przynajmniej w przypadku podania błędnego namespacea)
        logger.info("class Name: " + cr.getClass().getSimpleName());   // Debug
        logger.info("xd: " + cr.getSpec().getClass());
        logger.info("content: " + cr); // Debug
        //logger.info("spec:" + cr.getSpec()); // Debug
        logger.info("spec class: " + cr.getSpec().getClass().getSimpleName()); // Debug


        logger.info("\n\n\n\n\n\n\n\n\n"); // Debug
        PasswordProfileDTO dto = PasswordProfileMapper.INSTANCE.specToDto(cr.getSpec());
        logger.debug("Got password profile from cluster: " + dto.toString());
        return dto;
    }

    public AsymmetricKeysProfileDTO getAsymmetricKeysProfileByName(String name){
        AsymmetricKeysProfileCR cr = K8sCrdClientFactory.getAsymmetricKeysProfileClient().inNamespace(this.namespace).withName(name).get();
        AsymmetricKeysProfileDTO dto = AsymmetricKeysProfileMapper.INSTANCE.specToDto(cr.getSpec());
        logger.debug("Got keys profile from cluster: " + dto.toString());
        return dto;
    }

    public void deletePasswordProfileByName(String name){
        K8sCrdClientFactory.getPasswordProfileClient().inNamespace(this.namespace).withName(name).delete();
        logger.info("Password profile with name: " + name + " has been deleted");
    }

    public void deleteAsymmetricKeysProfileByName(String name){
        K8sCrdClientFactory.getAsymmetricKeysProfileClient().inNamespace(this.namespace).withName(name).delete();
        logger.info("Keys profile with name: " + name + " has been deleted");
    }


}

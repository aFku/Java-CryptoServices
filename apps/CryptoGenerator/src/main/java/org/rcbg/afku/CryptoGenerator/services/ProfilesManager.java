package org.rcbg.afku.CryptoGenerator.services;

import jakarta.validation.Valid;
import org.rcbg.afku.CryptoGenerator.dtos.AsymmetricKeysProfileDTO;
import org.rcbg.afku.CryptoGenerator.dtos.AsymmetricKeysProfileRequestBody;
import org.rcbg.afku.CryptoGenerator.dtos.PasswordProfileDTO;
import org.rcbg.afku.CryptoGenerator.dtos.PasswordProfileRequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
public class ProfilesManager {

    private final static Logger logger = LoggerFactory.getLogger(ProfilesManager.class);

    @Validated
    public PasswordProfileDTO createProfile(@Valid PasswordProfileRequestBody requestBody){
        return null;
    }

    @Validated
    public AsymmetricKeysProfileDTO createProfile(@Valid AsymmetricKeysProfileRequestBody requestBody){
        return null;
    }

    public PasswordProfileDTO getPasswordProfileByName(String name){
        return null;
    }

    public AsymmetricKeysProfileDTO getAsymmetricKeysProfileByName(String name){
        return null;
    }

    public void deletePasswordProfileByName(String name){
        return;
    }

    public void deleteAsymmetricKeysProfileByName(String name){
        return;
    }


}

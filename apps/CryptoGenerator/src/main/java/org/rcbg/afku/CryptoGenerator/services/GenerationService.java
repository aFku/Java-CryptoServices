package org.rcbg.afku.CryptoGenerator.services;

import org.rcbg.afku.CryptoGenerator.dtos.AsymmetricKeysProfileDTO;
import org.rcbg.afku.CryptoGenerator.dtos.PasswordProfileDTO;
import org.rcbg.afku.CryptoGenerator.exceptions.checked.ProfileNotLoaded;
import org.rcbg.afku.CryptoGenerator.exceptions.unchecked.CheckedExceptionWrapper;
import org.rcbg.afku.CryptoGenerator.services.Generators.AsymmetricKeysGenerator.AsymmetricKeysGeneratorProxyProfilesProp;
import org.rcbg.afku.CryptoGenerator.services.Generators.PasswordGenerator.PasswordGeneratorProxyProfilesProp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenerationService {

    private final Logger logger = LoggerFactory.getLogger(GenerationService.class);
    private final PasswordGeneratorProxyProfilesProp passwordGenerator;
    private final AsymmetricKeysGeneratorProxyProfilesProp asymmetricKeysGenerator;

    @Autowired
    public GenerationService(PasswordGeneratorProxyProfilesProp passwordGenerator, AsymmetricKeysGeneratorProxyProfilesProp asymmetricKeysGenerator){
        this.passwordGenerator = passwordGenerator;
        this.asymmetricKeysGenerator = asymmetricKeysGenerator;
    }

    public String generatePassword(PasswordProfileDTO profile){
        try {
            String password = this.passwordGenerator.withProfileProperties(profile.getProfileProperties()).generate();
            logger.info("Password has been generated with profile: " + profile.getProfileMetadata().getProfileName());
            return password;
        } catch (ProfileNotLoaded e) {
            logger.error("Cannot generate password");
            throw new CheckedExceptionWrapper(e.getMessage(), e.getClass().getSimpleName());
        }
    }

    public String[] generateKeys(AsymmetricKeysProfileDTO profile){
        try {
            String[] keys = this.asymmetricKeysGenerator.withProfileProperties(profile.getProfileProperties()).generate();
            logger.info("Pair of keys has been generated with profile: " + profile.getProfileMetadata().getProfileName());
            return keys;
        } catch (ProfileNotLoaded e) {
            logger.error("Cannot generate keys");
            throw new CheckedExceptionWrapper(e.getMessage(), e.getClass().getSimpleName());
        }
    }


}

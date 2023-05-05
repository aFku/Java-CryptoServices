package org.rcbg.afku.CryptoGenerator.services;

import org.rcbg.afku.CryptoGenerator.dtos.AsymmetricKeysProfile;
import org.rcbg.afku.CryptoGenerator.dtos.PasswordProfile;
import org.rcbg.afku.CryptoGenerator.exceptions.checked.ProfileNotLoaded;
import org.rcbg.afku.CryptoGenerator.exceptions.unchecked.CheckedExceptionWrapper;
import org.rcbg.afku.CryptoGenerator.services.Generators.AsymmetricKeysGenerator.AsymmetricKeysGeneratorProxyProfiles;
import org.rcbg.afku.CryptoGenerator.services.Generators.PasswordGenerator.PasswordGeneratorProxyProfiles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenerationService {

    private final Logger logger = LoggerFactory.getLogger(GenerationService.class);
    private final PasswordGeneratorProxyProfiles passwordGenerator;
    private final AsymmetricKeysGeneratorProxyProfiles asymmetricKeysGenerator;

    @Autowired
    public GenerationService(PasswordGeneratorProxyProfiles passwordGenerator, AsymmetricKeysGeneratorProxyProfiles asymmetricKeysGenerator){
        this.passwordGenerator = passwordGenerator;
        this.asymmetricKeysGenerator = asymmetricKeysGenerator;
    }

    public String generatePassword(PasswordProfile profile){
        try {
            String password = this.passwordGenerator.withProfile(profile).generate();
            logger.info("Password has been generated with profile: " + profile.getName());
            return password;
        } catch (ProfileNotLoaded e) {
            logger.error("Cannot generate password");
            throw new CheckedExceptionWrapper(e.getMessage(), e.getClass().getSimpleName());
        }
    }

    public String[] generateKeys(AsymmetricKeysProfile profile){
        try {
            String[] keys = this.asymmetricKeysGenerator.withProfile(profile).generate();
            logger.info("Pair of keys has been generated with profile: " + profile.getName());
            return keys;
        } catch (ProfileNotLoaded e) {
            logger.error("Cannot generate keys");
            throw new CheckedExceptionWrapper(e.getMessage(), e.getClass().getSimpleName());
        }
    }


}

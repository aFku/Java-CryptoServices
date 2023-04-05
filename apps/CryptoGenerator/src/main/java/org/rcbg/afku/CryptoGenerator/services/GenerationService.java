package org.rcbg.afku.CryptoGenerator.services;

import org.rcbg.afku.CryptoGenerator.dtos.AsymmetricKeysProfile;
import org.rcbg.afku.CryptoGenerator.dtos.PasswordProfile;
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
        return "";
    }

    public String[] generateKeys(AsymmetricKeysProfile profile){
        return new String[]{};
    }


}

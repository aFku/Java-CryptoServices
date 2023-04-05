package org.rcbg.afku.CryptoGenerator.services.Generators.PasswordGenerator;

import org.rcbg.afku.CryptoGenerator.dtos.PasswordProfile;
import org.rcbg.afku.CryptoGenerator.exceptions.checked.ProfileNotLoaded;
import org.rcbg.afku.CryptoGenerator.services.Generators.AbstractProfileLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PasswordGeneratorProxyProfiles extends AbstractProfileLoader<PasswordProfile> implements IPasswordGenerator{

    private final Logger logger = LoggerFactory.getLogger(PasswordGeneratorProxyProfiles.class);
    private final PasswordGenerator passwordGeneratorService;

    @Autowired
    public PasswordGeneratorProxyProfiles(PasswordGenerator service){
        this.passwordGeneratorService = service;
    }

    public PasswordGeneratorProxyProfiles withProfile(PasswordProfile profile){
        this.passwordGeneratorService.reset();
        this.logger.warn("Password generator has been reset");
        this.loadProfile(profile);
        return this;
    }

    @Override
    public String generate() throws ProfileNotLoaded {
        this.passwordGeneratorService.withLength(profile.getLength());
        if(profile.isNumbersAllowed()){this.passwordGeneratorService.withNumbers();}
        if(profile.isUppercaseAllowed()){this.passwordGeneratorService.withUppercase();}
        if(profile.isSpecialCharsAllowed()){this.passwordGeneratorService.withSpecialChars();}
        if(profile.getExcludedSpecialChars().length() != 0){this.passwordGeneratorService.excludeSpecialChars(profile.getExcludedSpecialChars());}
        return this.passwordGeneratorService.generate();
    }
}

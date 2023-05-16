package org.rcbg.afku.CryptoGenerator.services.Generators.PasswordGenerator;

import org.rcbg.afku.CryptoGenerator.dtos.PasswordProfileProperties;
import org.rcbg.afku.CryptoGenerator.exceptions.checked.ProfileNotLoaded;
import org.rcbg.afku.CryptoGenerator.services.Generators.AbstractProfileLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PasswordGeneratorProxyProfilesProp extends AbstractProfileLoader<PasswordProfileProperties> implements IPasswordGenerator{

    private final Logger logger = LoggerFactory.getLogger(PasswordGeneratorProxyProfilesProp.class);
    private final PasswordGenerator passwordGeneratorService;

    @Autowired
    public PasswordGeneratorProxyProfilesProp(PasswordGenerator service){
        this.passwordGeneratorService = service;
    }

    public PasswordGeneratorProxyProfilesProp withProfileProperties(PasswordProfileProperties profile){
        this.passwordGeneratorService.reset();
        this.logger.warn("Password generator has been reset");
        this.loadProfile(profile);
        return this;
    }

    @Override
    public String generate() throws ProfileNotLoaded {
        this.passwordGeneratorService.withLength(profile.getLength());
        if(profile.getNumbersAllowed()){this.passwordGeneratorService.withNumbers();}
        if(profile.getUppercaseAllowed()){this.passwordGeneratorService.withUppercase();}
        if(profile.getSpecialCharsAllowed()){this.passwordGeneratorService.withSpecialChars();}
        if(profile.getExcludedSpecialChars().length() != 0){this.passwordGeneratorService.excludeSpecialChars(profile.getExcludedSpecialChars());}
        return this.passwordGeneratorService.generate();
    }
}

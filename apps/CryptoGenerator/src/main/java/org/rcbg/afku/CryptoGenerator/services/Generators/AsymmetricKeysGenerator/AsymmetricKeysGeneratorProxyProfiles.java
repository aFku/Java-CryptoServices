package org.rcbg.afku.CryptoGenerator.services.Generators.AsymmetricKeysGenerator;

import org.rcbg.afku.CryptoGenerator.dtos.AsymmetricKeysProfile;
import org.rcbg.afku.CryptoGenerator.exceptions.checked.ProfileNotLoaded;
import org.rcbg.afku.CryptoGenerator.exceptions.unchecked.CorruptedProfile;
import org.rcbg.afku.CryptoGenerator.services.Generators.AbstractProfileLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Service
public class AsymmetricKeysGeneratorProxyProfiles extends AbstractProfileLoader<AsymmetricKeysProfile> implements IAsymmetricKeysGenerator{

    private final Logger logger = LoggerFactory.getLogger(AsymmetricKeysGeneratorProxyProfiles.class);
    private final AsymmetricKeysGenerator keysGenerator;

    @Autowired
    public AsymmetricKeysGeneratorProxyProfiles(AsymmetricKeysGenerator service){ this.keysGenerator = service; }

    public AsymmetricKeysGeneratorProxyProfiles withProfile(AsymmetricKeysProfile profile){
        this.keysGenerator.reset();
        this.logger.warn("Asymmetric keys generator has been reset");
        this.loadProfile(profile);
        return this;
    }

    @Override
    public String[] generate() throws ProfileNotLoaded {
        this.isProfileLoaded();
        try {
            this.keysGenerator.withSize(this.profile.getSize()).withAlgorithm(this.profile.getAlgorithm());
        } catch (NoSuchAlgorithmException ex){
            throw new CorruptedProfile("Loaded profile has wrong algorithm name. No such algorithm: " + this.profile.getAlgorithm());
        }
        if(this.profile.isReturnBase64()){this.keysGenerator.returnBase64();}
        return keysGenerator.generate();
    }
}

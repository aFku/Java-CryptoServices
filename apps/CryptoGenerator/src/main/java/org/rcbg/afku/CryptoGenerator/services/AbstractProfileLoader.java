package org.rcbg.afku.CryptoGenerator.services;

import org.rcbg.afku.CryptoGenerator.dtos.AbstractProfile;
import org.rcbg.afku.CryptoGenerator.exceptions.checked.ProfileNotLoaded;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class AbstractProfileLoader<T extends AbstractProfile> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    protected T profile;

    protected void loadProfile(T profile){
        this.profile = profile;
        this.logger.info("New profile loaded - " + profile.toString());
    }

    protected void isProfileLoaded() throws ProfileNotLoaded {
        if(this.profile == null){
            ProfileNotLoaded notLoaded = new ProfileNotLoaded("Proxy service cannot find profile. Make sure you loaded it.");
            this.logger.error(String.valueOf(notLoaded));
            throw notLoaded;
        }
    }

    public T getProfile(){
        return this.profile;

    }
}

package org.rcbg.afku.CryptoGenerator.services.Profiles;

import org.rcbg.afku.CryptoGenerator.dtos.PasswordProfile;

public class PasswordProfileBuilder {

    private PasswordProfile profile;

    public void reset(){
        this.profile = new PasswordProfile();
    }

    public PasswordProfileBuilder(){
        this.reset();
    }

    public PasswordProfileBuilder withName(String name){

    }
}

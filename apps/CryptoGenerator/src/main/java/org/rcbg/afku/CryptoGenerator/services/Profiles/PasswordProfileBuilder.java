package org.rcbg.afku.CryptoGenerator.services.Profiles;

import org.rcbg.afku.CryptoGenerator.dtos.PasswordProfileProperties;

public class PasswordProfileBuilder {

    private PasswordProfileProperties profile;

    public void reset(){
        this.profile = new PasswordProfileProperties();
    }

    public PasswordProfileBuilder(){
        this.reset();
    }

//    public PasswordProfileBuilder withName(String name){
//
//    }
}

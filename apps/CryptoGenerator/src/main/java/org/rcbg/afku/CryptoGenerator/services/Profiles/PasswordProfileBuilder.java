package org.rcbg.afku.CryptoGenerator.services.Profiles;

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

package org.rcbg.afku.CryptoGenerator.exceptions.unchecked;

public class CorruptedProfile extends RuntimeException{
    public CorruptedProfile(String message) {
        super(message);
    }
}

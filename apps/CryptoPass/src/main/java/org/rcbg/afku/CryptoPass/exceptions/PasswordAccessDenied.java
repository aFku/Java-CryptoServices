package org.rcbg.afku.CryptoPass.exceptions;

public class PasswordAccessDenied extends RuntimeException{

    public PasswordAccessDenied() {
    }

    public PasswordAccessDenied(String message) {
        super(message);
    }
}

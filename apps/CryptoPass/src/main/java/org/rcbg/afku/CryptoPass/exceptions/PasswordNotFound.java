package org.rcbg.afku.CryptoPass.exceptions;

public class PasswordNotFound extends RuntimeException{

    public PasswordNotFound() {
    }

    public PasswordNotFound(String message) {
        super(message);
    }
}

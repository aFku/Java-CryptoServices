package org.rcbg.afku.CryptoPass.exceptions;

public class PasswordInternalError extends RuntimeException{

    private String errorClass;
    public PasswordInternalError() {
    }

    public PasswordInternalError(String message, String errorClass) {
        super(message);
        this.errorClass = errorClass;
    }

    public String getErrorClass() {
        return errorClass;
    }

    public void setErrorClass(String errorClass) {
        this.errorClass = errorClass;
    }
}

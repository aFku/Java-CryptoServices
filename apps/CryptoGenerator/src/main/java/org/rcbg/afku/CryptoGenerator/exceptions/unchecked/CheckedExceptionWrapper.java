package org.rcbg.afku.CryptoGenerator.exceptions.unchecked;

public class CheckedExceptionWrapper extends RuntimeException{

    private final String checkedName;

    public CheckedExceptionWrapper(String message, String checkedName) {
        super(message);
        this.checkedName = checkedName;
    }

    public String getCheckedName(){
        return this.checkedName;
    }
}

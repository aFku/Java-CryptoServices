package org.rcbg.afku.CryptoGenerator.dtos;

public class PasswordProfile extends AbstractProfile{
    private int length;
    private  boolean numbersAllowed;
    private boolean uppercaseAllowed;
    private boolean specialCharsAllowed;
    private String excludedSpecialChars;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isNumbersAllowed() {
        return numbersAllowed;
    }

    public void setNumbersAllowed(boolean numbersAllowed) {
        this.numbersAllowed = numbersAllowed;
    }

    public boolean isUppercaseAllowed() {
        return uppercaseAllowed;
    }

    public void setUppercaseAllowed(boolean uppercaseAllowed) {
        this.uppercaseAllowed = uppercaseAllowed;
    }

    public boolean isSpecialCharsAllowed() {
        return specialCharsAllowed;
    }

    public void setSpecialCharsAllowed(boolean specialCharsAllowed) {
        this.specialCharsAllowed = specialCharsAllowed;
    }

    public String getExcludedSpecialChars() {
        return excludedSpecialChars;
    }

    public void setExcludedSpecialChars(String excludedSpecialChars) {
        this.excludedSpecialChars = excludedSpecialChars;
    }

    @Override
    public String toString(){
        return String.format("PasswordProfile - %s, Length: %d, UppercaseAllowed: %b, NumbersAllowed: %b, SpecialCharsAllowed: %b, SpecialCharsExcluded: %s",
                super.toString(), this.length, this.uppercaseAllowed, this.numbersAllowed, this.specialCharsAllowed, this.excludedSpecialChars);
    }
}

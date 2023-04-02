package org.rcbg.afku.CryptoGenerator.services.PasswordGenerator;

import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.rcbg.afku.CryptoGenerator.services.IGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PasswordGenerator implements IPasswordGenerator, IGenerator {

    private final Logger logger = LoggerFactory.getLogger(PasswordGenerator.class);

    private int length;
    //private int seed;
    private boolean numbersAllowed;
    private boolean uppercaseAllowed;
    private boolean specialCharsAllowed;
    private String excludedSpecialChars;

    List<CharacterRule> generatorRules;

    private PasswordGenerator(){
        this.reset();
    }

    public void reset(){
        this.length = 16;
        this.numbersAllowed = false;
        this.uppercaseAllowed = false;
        this.specialCharsAllowed = false;
        this.excludedSpecialChars = "";
        this.generatorRules = new ArrayList<>();
    }

    private String prepareSpecialCharsRule(){
        String defaultSpecials = "!@#$%^&*()_+{}[]?.<>";

        if(!specialCharsAllowed){
            return "";
        }

        StringBuilder targetSpecials = new StringBuilder();
        for(char c: defaultSpecials.toCharArray()){
            if(excludedSpecialChars.indexOf(c) == -1){
                targetSpecials.append(c);
            }
        }

        if(targetSpecials.isEmpty()){
            logger.warn("Password generation with special characters, but all was excluded");
            return "";
        }

        CharacterData specialChars = new CharacterData() {
            public String getErrorCode() {
                return "E";
            }
            public String getCharacters() {
                return targetSpecials.toString();
            }
        };

        this.generatorRules.add(new CharacterRule(specialChars));
        return targetSpecials.toString();
    }

    public String generate(){
        String specialChars = this.prepareSpecialCharsRule();
        logger.info(String.format("Password will be generated with rules - Length: %d, numbersAllowed: %b," +
                "uppercaseAllowed: %b, hasSpecialCharsAllowed: %b, specialChars: %s", this.length, this.numbersAllowed,
                this.uppercaseAllowed, this.specialCharsAllowed, specialChars));

        String password = new org.passay.PasswordGenerator().generatePassword(this.length, this.generatorRules);
        logger.info("Generated password: " + password);
        return password;
    }

    public PasswordGenerator withLength(int length){
        if(length < 0) {throw new IllegalArgumentException("Length cannot be 0 or less");}
        this.length = length;
        return this;
    }

    public PasswordGenerator withNumbers(){
        this.numbersAllowed = true;
        this.generatorRules.add(new CharacterRule(EnglishCharacterData.Digit));
        return this;
    }

    public PasswordGenerator withUppercase(){
        this.uppercaseAllowed = true;
        this.generatorRules.add(new CharacterRule(EnglishCharacterData.UpperCase));
        return this;
    }

    public PasswordGenerator withSpecialChars(){
        this.specialCharsAllowed = true;
        return this;
    }

    public PasswordGenerator excludeSpecialChars(String excludedChars){
        this.excludedSpecialChars = excludedChars;
        return this;
    }

    public int getLength() {
        return length;
    }

    public boolean hasNumbersAllowed() {
        return numbersAllowed;
    }

    public boolean hasUppercaseAllowed() {
        return uppercaseAllowed;
    }

    public boolean hasSpecialCharsAllowed() {
        return specialCharsAllowed;
    }

    public String getExcludedChars() {
        return excludedSpecialChars;
    }
}

package org.rcbg.afku.CryptoGenerator.unit_tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rcbg.afku.CryptoGenerator.services.Generators.PasswordGenerator.PasswordGenerator;

public class PasswordGeneratorUnitTests {
    private PasswordGenerator generator = new PasswordGenerator();

    private boolean checkPassword(String password, int length){
        if(password == null){
            return false;
        }
        if(password.length() != length){
            return false;
        }
        return true;
    }

    @BeforeEach
    public void setup(){
        generator.reset();
    }

    @Test
    public void generatePasswordTest(){
        Assertions.assertTrue(checkPassword(generator.generate(), 16));
    }

    @Test
    public void generatePasswordWithUppercaseTest(){
        Assertions.assertTrue(checkPassword(generator.withUppercase().generate(), 16));
    }

    @Test
    public void generatePasswordWithNumbersTest(){
        Assertions.assertTrue(checkPassword(generator.withNumbers().generate(), 16));
    }

    @Test
    public void generatePasswordWithSpecialCharsTest(){
        Assertions.assertTrue(checkPassword(generator.withSpecialChars().generate(), 16));
    }

    @Test
    public void generatePasswordWithCustomLengthTest(){
        Assertions.assertTrue(checkPassword(generator.withLength(24).generate(), 24));
    }
}

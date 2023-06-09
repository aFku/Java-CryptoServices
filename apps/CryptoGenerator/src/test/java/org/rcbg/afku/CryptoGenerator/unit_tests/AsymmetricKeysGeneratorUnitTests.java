package org.rcbg.afku.CryptoGenerator.unit_tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.security.NoSuchAlgorithmException;
import org.rcbg.afku.CryptoGenerator.services.Generators.AsymmetricKeysGenerator.AsymmetricKeysGenerator;

public class AsymmetricKeysGeneratorUnitTests {

    private AsymmetricKeysGenerator generator = new AsymmetricKeysGenerator();

    public boolean checkIfKeysPresent(String[] keys){
        if(keys[0] == null || keys[0].length() < 1){
            return false;
        }
        if(keys[1] == null || keys[1].length() < 1){
            return false;
        }
        return true;
    }

    @BeforeEach
    public void setup(){
        generator.reset();
    }

    @Test
    public void generateKeysWithoutBase64Test(){
        String[] keys = generator.generate();
        Assertions.assertTrue(checkIfKeysPresent(keys));
    }

    @Test
    public void generateKeysWithBase64Test(){
        String[] keys = generator.returnBase64().generate();
        Assertions.assertTrue(checkIfKeysPresent(keys));
    }

    @Test
    public void generateKeysWithIncorrectAlgorithmTest(){
        Assertions.assertThrows(NoSuchAlgorithmException.class, () -> generator.withAlgorithm("test-algorithm"));
    }

    @Test
    public void setGeneratorKeysWithOtherAlgorithmTest() throws NoSuchAlgorithmException{
        String[] keys = generator.returnBase64().withAlgorithm("DSA").generate();
    }


}

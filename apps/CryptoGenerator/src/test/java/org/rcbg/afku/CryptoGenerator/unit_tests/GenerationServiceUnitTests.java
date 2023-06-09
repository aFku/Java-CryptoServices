package org.rcbg.afku.CryptoGenerator.unit_tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.rcbg.afku.CryptoGenerator.dtos.*;
import org.rcbg.afku.CryptoGenerator.services.GenerationService;
import org.rcbg.afku.CryptoGenerator.services.Generators.AsymmetricKeysGenerator.AsymmetricKeysGenerator;
import org.rcbg.afku.CryptoGenerator.services.Generators.AsymmetricKeysGenerator.AsymmetricKeysGeneratorProxyProfilesProp;
import org.rcbg.afku.CryptoGenerator.services.Generators.PasswordGenerator.PasswordGenerator;
import org.rcbg.afku.CryptoGenerator.services.Generators.PasswordGenerator.PasswordGeneratorProxyProfilesProp;


public class GenerationServiceUnitTests {

    private AsymmetricKeysGeneratorProxyProfilesProp keys = new AsymmetricKeysGeneratorProxyProfilesProp(new AsymmetricKeysGenerator());

    private PasswordGeneratorProxyProfilesProp passwords = new PasswordGeneratorProxyProfilesProp(new PasswordGenerator());

    private GenerationService generationService = new GenerationService(passwords, keys);
    // testPasswordGeneration()
    @Test
    public void passwordGenerationTest(){
        PasswordProfileProperties properties = new PasswordProfileProperties();
        properties.setNumbersAllowed(true);
        properties.setLength(14);
        properties.setExcludedSpecialChars("");
        properties.setUppercaseAllowed(true);
        properties.setSpecialCharsAllowed(true);
        ProfileMetadata meta = new ProfileMetadata();
        meta.setProfileName("name");
        meta.setCreatorUserId("user");
        PasswordProfileDTO dto = new PasswordProfileDTO(meta, properties);

        String password = this.generationService.generatePassword(dto);
        Assertions.assertEquals(password.length(), 14);
        boolean hasNumber = false;
        boolean hasUpperCase = false;
        boolean hasSpecialChars = false;
        for(char c : password.toCharArray()){
            if(Character.isDigit(c)){
                hasNumber = true;
                continue;
            }
            if(Character.isUpperCase(c)){
                hasUpperCase = true;
                continue;
            }
            if(! Character.isLetter(c)){
                hasSpecialChars = true;
                continue;
            }
        }
        Assertions.assertTrue(hasUpperCase);
        Assertions.assertTrue(hasNumber);
        Assertions.assertTrue(hasSpecialChars);
    }

    @Test
    public void asymmetricKeysGenerationTest(){
        AsymmetricKeysProfileProperties properties = new AsymmetricKeysProfileProperties();
        properties.setAlgorithm("DSA");
        properties.setReturnBase64(true);
        ProfileMetadata meta = new ProfileMetadata();
        meta.setProfileName("name");
        meta.setCreatorUserId("user");
        AsymmetricKeysProfileDTO dto = new AsymmetricKeysProfileDTO(meta, properties);

        String[] keys = generationService.generateKeys(dto);
        Assertions.assertEquals(keys.length, 2);
        Assertions.assertNotNull(keys[0]);
        Assertions.assertNotNull(keys[1]);
        Assertions.assertTrue(keys[0].length() >= 1);
        Assertions.assertTrue(keys[1].length() >= 1);
    }
}

package org.rcbg.afku.CryptoPass.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

@Service
public class AESCryptographyService {

    private final SecretKeyFactory keyFactory;

    @Value("${encryption.iv-length}")
    private int ivLength;

    @Value("${bcrypt-service.salt.log-rounds}")
    private int saltLogRounds;

    private final int bcryptSaltLength = 16; // 128 bits = 16 bytes

    public AESCryptographyService() throws NoSuchAlgorithmException {
        this.keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
    }

    private IvParameterSpec generateIv(){
        byte[] iv = new byte[this.ivLength];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    private SecretKey getSecretFromPassword(String password, String salt) throws InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
        return new SecretKeySpec(this.keyFactory.generateSecret(spec).getEncoded(), "AES");
    }

    public String encryptPassword(String data, String passwordBase) throws
            InvalidKeySpecException, NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {

        IvParameterSpec ivParameterSpec = this.generateIv();
        String salt = BCrypt.gensalt(saltLogRounds);
        String base64Iv = Base64.getEncoder().encodeToString(ivParameterSpec.getIV());
        String base64Salt = Base64.getEncoder().encodeToString(salt.getBytes());

        SecretKey key = getSecretFromPassword(passwordBase, salt);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);

        byte[] cipherText = cipher.doFinal(data.getBytes());
        String base64CipherText = Base64.getEncoder().encodeToString(cipherText);

        return base64Salt + "." + base64Iv + "." + base64CipherText;
    }

    public String decryptPassword(String data, String passwordBase) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        String[] rawData = data.split("\\.");
        byte[] salt = Base64.getDecoder().decode(rawData[0]);
        byte[] iv = Base64.getDecoder().decode(rawData[1]);
        byte[] cipherText = Base64.getDecoder().decode(rawData[2]);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, getSecretFromPassword(passwordBase, new String(salt)), new IvParameterSpec(iv));

        return new String(cipher.doFinal(cipherText));
    }


}

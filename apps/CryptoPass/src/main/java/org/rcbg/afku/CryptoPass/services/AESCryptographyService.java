package org.rcbg.afku.CryptoPass.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
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

    private SecretKeyFactory keyFactory;

    @Value("${encryption.iv-length}")
    private int ivLength;

    public AESCryptographyService() throws NoSuchAlgorithmException {
        this.keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
    }

    private IvParameterSpec generateIv(){
        byte[] iv = new byte[this.ivLength];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    private SecretKey getSecretFromPassword(String password) throws InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(password.toCharArray());
        return new SecretKeySpec(this.keyFactory.generateSecret(spec).getEncoded(), "AES");
    }

    public String encryptPassword(String data, String passwordBase) throws
            InvalidKeySpecException, NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {
        IvParameterSpec ivParameterSpec = this.generateIv();
        SecretKey key = getSecretFromPassword(passwordBase);
        Cipher cipher = Cipher.getInstance("CBC");
        cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);

        byte[] cipherText = cipher.doFinal(data.getBytes());
        byte[] cipherTextWithIv = new byte[this.ivLength + cipherText.length];
        System.arraycopy(ivParameterSpec.getIV(), 0, cipherTextWithIv, 0, this.ivLength);
        System.arraycopy(cipherText, 0, cipherTextWithIv, this.ivLength, cipherTextWithIv.length);
        return Base64.getEncoder().encodeToString(cipherTextWithIv);
    }

    public String decryptePassword(String data, String passwordBase) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] rawData = Base64.getDecoder().decode(data);
        byte[] iv = Arrays.copyOfRange(rawData, 0, this.ivLength);
        byte[] cipherText = Arrays.copyOfRange(rawData, this.ivLength, rawData.length);

        Cipher cipher = Cipher.getInstance("CBC");
        cipher.init(Cipher.DECRYPT_MODE, getSecretFromPassword(passwordBase), new IvParameterSpec(iv));

        return new String(cipher.doFinal(cipherText));
    }


}

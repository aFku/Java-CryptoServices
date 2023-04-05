package org.rcbg.afku.CryptoGenerator.services.Generators.AsymmetricKeysGenerator;

import org.rcbg.afku.CryptoGenerator.services.Generators.IGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class AsymmetricKeysGenerator implements IAsymmetricKeysGenerator, IGenerator {

    Logger logger = LoggerFactory.getLogger(AsymmetricKeysGenerator.class);

    private KeyPairGenerator generator;
    private int size;
    private boolean returnBase64;

    public AsymmetricKeysGenerator(){
        this.reset();
    }

    public void reset(){
        try {
            this.generator = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException ex){
            logger.error(ex.toString());
        }
        this.size = 2048;
        this.returnBase64 = false;
    }

    public AsymmetricKeysGenerator withAlgorithm(String algorithm) throws NoSuchAlgorithmException{
        this.generator = KeyPairGenerator.getInstance(algorithm);
        return this;
    }

    public AsymmetricKeysGenerator withSize(int size){
        if(size < 0) {throw new IllegalArgumentException("Size cannot be 0 or less");}
        this.size = size;
        return this;
    }

    public AsymmetricKeysGenerator returnBase64(){
        this.returnBase64 = true;
        return this;
    }

    private String[] encodeKeysBase64(KeyPair pair){
        logger.info("Keys will be encrypted with base64 after generating");
        byte[] privateKey = pair.getPrivate().getEncoded();
        byte[] publicKey = pair.getPublic().getEncoded();
        return new String[]{Base64.getEncoder().encodeToString(privateKey), Base64.getEncoder().encodeToString(publicKey)};
    }

    @Override
    public String[] generate(){
        this.generator.initialize(this.size);
        KeyPair pair = this.generator.generateKeyPair();
        logger.info(String.format("Starting generating asymmetric keys with parameters - Algorithm: %s, Size: %d",
                this.generator.getAlgorithm(), this.size));

        String[] keys;
        if(returnBase64) {
            keys = this.encodeKeysBase64(pair);
        } else {
            keys = new String[]{pair.getPrivate().toString(), pair.getPublic().toString()};
        }
        logger.info(String.format("Generated keys - Private: %s, Public: %s", keys[0], keys[1]));
        return keys;
    }

    public String getAlgorithm(){
        return this.generator.getAlgorithm();
    }

    public int getSize(){
        return this.size;
    }

    public boolean isResultBase64(){
        return this.returnBase64;
    }
}

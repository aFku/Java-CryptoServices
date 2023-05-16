package org.rcbg.afku.CryptoGenerator.services.Generators.AsymmetricKeysGenerator;

public enum AlgorithmSize {
    RSA(2048),
    DSA(2048),
    EC(256);


    private final int keySize;

    AlgorithmSize(int keySize) {
        this.keySize = keySize;
    }

    public int getKeySize() {
        return keySize;
    }
}

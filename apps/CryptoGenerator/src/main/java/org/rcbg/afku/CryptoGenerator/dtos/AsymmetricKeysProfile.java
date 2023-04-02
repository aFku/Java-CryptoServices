package org.rcbg.afku.CryptoGenerator.dtos;

public class AsymmetricKeysProfile extends AbstractProfile{
    private int size;
    private String algorithm;
    private boolean returnBase64;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public boolean isReturnBase64() {
        return returnBase64;
    }

    public void setReturnBase64(boolean returnBase64) {
        this.returnBase64 = returnBase64;
    }

    @Override
    public String toString(){
        return String.format("AsymmetricKeysProfile - %s, Size: %d, Algorithm: %b, ReturnBase64: %b",
                super.toString(), this.size, this.algorithm, this.returnBase64);
    }
}

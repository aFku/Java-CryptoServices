package org.rcbg.afku.CryptoGenerator.services.Generators.AsymmetricKeysGenerator;

import org.rcbg.afku.CryptoGenerator.exceptions.checked.ProfileNotLoaded;

public interface IAsymmetricKeysGenerator {
    public String[] generate() throws ProfileNotLoaded;
}

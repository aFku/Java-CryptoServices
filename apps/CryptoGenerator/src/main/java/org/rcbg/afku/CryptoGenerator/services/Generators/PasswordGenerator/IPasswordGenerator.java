package org.rcbg.afku.CryptoGenerator.services.Generators.PasswordGenerator;

import org.rcbg.afku.CryptoGenerator.exceptions.checked.ProfileNotLoaded;

public interface IPasswordGenerator {
    public String generate() throws ProfileNotLoaded;
}

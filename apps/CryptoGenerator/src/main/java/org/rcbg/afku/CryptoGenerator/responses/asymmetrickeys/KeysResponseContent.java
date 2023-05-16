package org.rcbg.afku.CryptoGenerator.responses.asymmetrickeys;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.rcbg.afku.CryptoGenerator.dtos.AsymmetricKeysProfileDTO;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KeysResponseContent {
    private AsymmetricKeysProfileDTO profile;
    private String publicKey;
    private String privateKey;
}

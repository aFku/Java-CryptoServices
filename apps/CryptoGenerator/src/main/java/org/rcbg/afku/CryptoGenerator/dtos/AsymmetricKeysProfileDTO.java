package org.rcbg.afku.CryptoGenerator.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AsymmetricKeysProfileDTO {

    private ProfileMetadata profileMetadata;

    private AsymmetricKeysProfileProperties profileProperties;

    // TO DO: toString()
}

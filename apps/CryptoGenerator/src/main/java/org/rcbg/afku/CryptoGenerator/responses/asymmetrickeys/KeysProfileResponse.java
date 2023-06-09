package org.rcbg.afku.CryptoGenerator.responses.asymmetrickeys;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.rcbg.afku.CryptoGenerator.dtos.AsymmetricKeysProfileDTO;
import org.rcbg.afku.CryptoGenerator.responses.ResponseMetadata;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KeysProfileResponse {
    private AsymmetricKeysProfileDTO data;
    private ResponseMetadata metadata;
}

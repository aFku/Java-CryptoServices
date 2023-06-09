package org.rcbg.afku.CryptoGenerator.responses.asymmetrickeys;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.rcbg.afku.CryptoGenerator.responses.ResponseMetadata;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KeysResponse {
    private ResponseMetadata metadata;
    private KeysResponseContent data;
}

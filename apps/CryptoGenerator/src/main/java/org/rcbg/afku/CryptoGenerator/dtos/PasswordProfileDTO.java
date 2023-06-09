package org.rcbg.afku.CryptoGenerator.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordProfileDTO {

    private ProfileMetadata profileMetadata;

    private PasswordProfileProperties profileProperties;

    // TO DO: toString()
}

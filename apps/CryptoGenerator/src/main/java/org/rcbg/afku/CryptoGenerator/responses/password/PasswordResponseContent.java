package org.rcbg.afku.CryptoGenerator.responses.password;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.rcbg.afku.CryptoGenerator.dtos.PasswordProfileDTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResponseContent {
    private PasswordProfileDTO profile;
    private String password;
}

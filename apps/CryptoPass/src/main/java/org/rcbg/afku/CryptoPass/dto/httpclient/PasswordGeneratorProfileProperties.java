package org.rcbg.afku.CryptoPass.dto.httpclient;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PasswordGeneratorProfileProperties {
    private Integer length;
    private  Boolean numbersAllowed;
    private Boolean uppercaseAllowed;
    private Boolean specialCharsAllowed;
    private String excludedSpecialChars;
}

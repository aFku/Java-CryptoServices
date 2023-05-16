package org.rcbg.afku.CryptoGenerator.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@NoArgsConstructor
public class PasswordProfileProperties {

    @Range(min = 6, max = 255, message = "Password length should be between 6 and 255")
    private Integer length;

    @NotNull(message = "numbersAllowed cannot be null")
    private  Boolean numbersAllowed;

    @NotNull(message = "uppercaseAllowed cannot be null")
    private Boolean uppercaseAllowed;

    @NotNull(message = "specialCharsAllowed cannot be null")
    private Boolean specialCharsAllowed;

    private String excludedSpecialChars;
}

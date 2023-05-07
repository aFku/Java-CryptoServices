package org.rcbg.afku.CryptoGenerator.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PasswordProfileProperties {

    @Size(min = 6, max = 255, message = "Password length should be between 6 and 255")
    private int length;

    @NotNull(message = "numbersAllowed cannot be null")
    private  boolean numbersAllowed;

    @NotNull(message = "uppercaseAllowed cannot be null")
    private boolean uppercaseAllowed;

    @NotNull(message = "specialCharsAllowed cannot be null")
    private boolean specialCharsAllowed;

    private String excludedSpecialChars;
}

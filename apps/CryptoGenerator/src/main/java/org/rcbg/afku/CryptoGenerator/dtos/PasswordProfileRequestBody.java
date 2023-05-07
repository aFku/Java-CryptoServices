package org.rcbg.afku.CryptoGenerator.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PasswordProfileRequestBody extends PasswordProfileProperties{

    @NotEmpty(message = "profileName cannot be empty")
    private String profileName;
}

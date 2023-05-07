package org.rcbg.afku.CryptoGenerator.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AsymmetricKeysProfileRequestBody extends AsymmetricKeysProfileProperties{

    @NotEmpty(message = "profileName cannot be empty")
    private String profileName;
}

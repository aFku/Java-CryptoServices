package org.rcbg.afku.CryptoGenerator.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@NoArgsConstructor
public class AsymmetricKeysProfileProperties {
    @NotEmpty(message = "Algorithm cannot be empty")
    private String algorithm;

    @NotNull(message = "returnBase64 cannot be null")
    private Boolean returnBase64;
}

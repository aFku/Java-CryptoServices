package org.rcbg.afku.CryptoGenerator.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AsymmetricKeysProfileProperties {
    @Size(min = 1, max = 4096, message = "Size has to be between 1 and 4096")
    private int size;

    @NotEmpty(message = "Algorithm cannot be empty")
    private String algorithm;

    @NotNull(message = "returnBase64 cannot be null")
    private boolean returnBase64;
}

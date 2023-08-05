package org.rcbg.afku.CryptoPass.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.rcbg.afku.CryptoPass.dto.httpclient.PasswordGeneratorProfileProperties;

@Getter
@Setter
@NoArgsConstructor
public class PasswordGenerationSaveRequestDto {
    PasswordGeneratorProfileProperties properties;
    PasswordSaveRequestDto passwordData;
}

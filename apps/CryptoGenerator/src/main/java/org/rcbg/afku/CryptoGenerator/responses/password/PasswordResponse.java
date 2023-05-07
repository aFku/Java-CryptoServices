package org.rcbg.afku.CryptoGenerator.responses.password;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.rcbg.afku.CryptoGenerator.responses.ResponseMetadata;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResponse {
    private ResponseMetadata metadata;
    private PasswordResponseContent data;
}

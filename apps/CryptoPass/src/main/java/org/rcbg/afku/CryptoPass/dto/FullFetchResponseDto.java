package org.rcbg.afku.CryptoPass.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FullFetchResponseDto extends SafeFetchResponseDto{

    private String password;

    @JsonIgnore
    private String ownerUserId;

    @JsonIgnore
    private String key;
}

package org.rcbg.afku.CryptoPass.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SafeFetchResponseDto {
    private String name;
    private String description;
    private Integer passwordId;
}

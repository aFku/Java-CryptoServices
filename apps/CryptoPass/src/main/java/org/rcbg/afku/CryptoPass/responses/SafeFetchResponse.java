package org.rcbg.afku.CryptoPass.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.rcbg.afku.CryptoPass.dto.SafeFetchResponseDto;

@Getter
@Setter
@NoArgsConstructor
public class SafeFetchResponse extends MetaDataResponse{

    private SafeFetchResponseDto data;
}

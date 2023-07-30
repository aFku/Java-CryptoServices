package org.rcbg.afku.CryptoPass.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.rcbg.afku.CryptoPass.dto.FullFetchResponseDto;

@Setter
@Getter
@NoArgsConstructor
public class FullFetchResponse extends MetaDataResponse{
    private FullFetchResponseDto data;
}

package org.rcbg.afku.CryptoPass.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.rcbg.afku.CryptoPass.dto.SafeFetchResponseDto;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class SafePaginationResponse extends PaginationResponse{
    private List<SafeFetchResponseDto> data;
}

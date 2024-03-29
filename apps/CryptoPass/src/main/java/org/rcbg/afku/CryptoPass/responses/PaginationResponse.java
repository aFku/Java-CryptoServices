package org.rcbg.afku.CryptoPass.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaginationResponse extends MetaDataResponse{

    private PaginationData pagination;
}

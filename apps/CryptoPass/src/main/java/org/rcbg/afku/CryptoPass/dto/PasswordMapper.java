package org.rcbg.afku.CryptoPass.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.rcbg.afku.CryptoPass.domain.Password;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PasswordMapper {

    PasswordMapper INSTANCE = Mappers.getMapper(PasswordMapper.class);

    @Mapping(source = "ownerUserId", target = "ownerUserId")
    public Password toEntity(PasswordSaveRequestDto dto, String ownerUserId);

    public SafeFetchResponseDto toSafeDto(Password password);

    public FullFetchResponseDto toFullDto(Password password);
}

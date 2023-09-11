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
    @Mapping(source = "dto.password", target = "encryptedPassword")
    @Mapping(source = "dto.key", target = "secretKey")
    @Mapping(source = "dto.description", target = "passwordDescription")
    @Mapping(source = "dto.name", target = "passwordName")
    public Password toEntity(PasswordSaveRequestDto dto, String ownerUserId);

    @Mapping(source = "password.passwordId", target = "passwordId")
    @Mapping(source = "password.passwordDescription", target = "description")
    @Mapping(source = "password.passwordName", target = "name")
    public SafeFetchResponseDto toSafeDto(Password password);

    @Mapping(source = "password.passwordId", target = "passwordId")
    @Mapping(source = "password.passwordDescription", target = "description")
    @Mapping(source = "password.passwordName", target = "name")
    @Mapping(source = "password.ownerUserId", target = "ownerUserId")
    @Mapping(source = "password.encryptedPassword", target = "password")
    @Mapping(source = "password.secretKey", target = "key")
    public FullFetchResponseDto toFullDto(Password password);
}

package org.rcbg.afku.CryptoGenerator.dtos;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.rcbg.afku.CryptoGenerator.k8sClient.models.PasswordProfile.PasswordProfileCR;
import org.rcbg.afku.CryptoGenerator.k8sClient.models.PasswordProfile.PasswordProfileSpec;
import org.rcbg.afku.CryptoGenerator.services.ProfilesManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PasswordProfileMapper {

    final static Logger logger = LoggerFactory.getLogger(ProfilesManager.class); // debug

    PasswordProfileMapper INSTANCE = Mappers.getMapper(PasswordProfileMapper.class);

    @Mapping(target = "profileMetadata.profileName", source = "requestBody.profileName")
    @Mapping(target = "profileMetadata.creatorUserId", source = "creatorUserId")
    @Mapping(target = "profileMetadata.creationTimestamp", ignore = true)
    @Mapping(target = "profileProperties.length", source = "requestBody.length")
    @Mapping(target = "profileProperties.numbersAllowed", source = "requestBody.numbersAllowed")
    @Mapping(target = "profileProperties.uppercaseAllowed", source = "requestBody.uppercaseAllowed")
    @Mapping(target = "profileProperties.specialCharsAllowed", source = "requestBody.specialCharsAllowed")
    @Mapping(target = "profileProperties.excludedSpecialChars", source = "requestBody.excludedSpecialChars")
    public PasswordProfileDTO toFullDto(PasswordProfileRequestBody requestBody, String creatorUserId);

    public default PasswordProfileCR createProfileResource(PasswordProfileRequestBody requestBody, String creatorUserId){
        PasswordProfileDTO profileDTO = INSTANCE.toFullDto(requestBody, creatorUserId);
        PasswordProfileSpec spec = new PasswordProfileSpec();
        spec.setProfileMetadata(profileDTO.getProfileMetadata());
        spec.setProfileProperties(profileDTO.getProfileProperties());
        PasswordProfileCR cr = new PasswordProfileCR();
        cr.setSpec(spec);
        cr.getMetadata().setName(requestBody.getProfileName());
        return cr;
    }

    @Mapping(target = "profileMetadata", source = "spec.profileMetadata")
    @Mapping(target = "profileProperties", source = "spec.profileProperties")
    public PasswordProfileDTO specToDto(PasswordProfileSpec spec);
}

package org.rcbg.afku.CryptoGenerator.dtos;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.rcbg.afku.CryptoGenerator.k8sClient.models.PasswordProfile.PasswordProfileCR;
import org.rcbg.afku.CryptoGenerator.k8sClient.models.PasswordProfile.PasswordProfileSpec;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PasswordProfileMapper {

    PasswordProfileMapper INSTANCE = Mappers.getMapper(PasswordProfileMapper.class);

    @Mapping(target = "metadata.profileName", source = "requestBody.profileName")
    @Mapping(target = "metadata.creatorUserId", source = "creatorUserId")
    @Mapping(target = "metadata.creationTimestamp", ignore = true)
    public PasswordProfileDTO toFullDto(PasswordProfileRequestBody requestBody, String creatorUserId);

    public default PasswordProfileCR createProfileResource(PasswordProfileRequestBody requestBody, String creatorUserId){
        PasswordProfileDTO profileDTO = INSTANCE.toFullDto(requestBody, creatorUserId);
        PasswordProfileSpec spec = new PasswordProfileSpec();
        spec.setMetadata(profileDTO.getMetadata());
        spec.setProfileProperties(profileDTO.getProfileProperties());
        PasswordProfileCR cr = new PasswordProfileCR();
        cr.setSpec(spec);
        cr.getMetadata().setName(requestBody.getProfileName());
        return cr;
    }

    @Mapping(target = "metadata", source = "spec.metadata")
    @Mapping(target = "profileProperties", source = "spec.profileProperties")
    public PasswordProfileDTO specToDto(PasswordProfileSpec spec);
}

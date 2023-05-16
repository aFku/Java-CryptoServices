package org.rcbg.afku.CryptoGenerator.dtos;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.rcbg.afku.CryptoGenerator.k8sClient.models.AsymmetricKeysProfile.AsymmetricKeysProfileCR;
import org.rcbg.afku.CryptoGenerator.k8sClient.models.AsymmetricKeysProfile.AsymmetricKeysProfileSpec;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AsymmetricKeysProfileMapper {

    AsymmetricKeysProfileMapper INSTANCE = Mappers.getMapper(AsymmetricKeysProfileMapper.class);

    @Mapping(target = "profileMetadata.profileName", source = "requestBody.profileName")
    @Mapping(target = "profileMetadata.creatorUserId", source = "creatorUserId")
    @Mapping(target = "profileMetadata.creationTimestamp", ignore = true)
    @Mapping(target = "profileProperties.algorithm", source = "requestBody.algorithm")
    @Mapping(target = "profileProperties.returnBase64", source = "requestBody.returnBase64")
    public AsymmetricKeysProfileDTO toFullDto(AsymmetricKeysProfileRequestBody requestBody, String creatorUserId);

    public default AsymmetricKeysProfileCR createProfileResource(AsymmetricKeysProfileRequestBody requestBody, String creatorUserId){
        AsymmetricKeysProfileDTO profileDTO = INSTANCE.toFullDto(requestBody, creatorUserId);
        AsymmetricKeysProfileSpec spec = new AsymmetricKeysProfileSpec();
        spec.setProfileMetadata(profileDTO.getProfileMetadata());
        spec.setProfileProperties(profileDTO.getProfileProperties());
        AsymmetricKeysProfileCR cr = new AsymmetricKeysProfileCR();
        cr.setSpec(spec);
        cr.getMetadata().setName(requestBody.getProfileName());
        return cr;
    }

    @Mapping(target = "profileMetadata", source = "spec.profileMetadata")
    @Mapping(target = "profileProperties", source = "spec.profileProperties")
    public AsymmetricKeysProfileDTO specToDto(AsymmetricKeysProfileSpec spec);
}

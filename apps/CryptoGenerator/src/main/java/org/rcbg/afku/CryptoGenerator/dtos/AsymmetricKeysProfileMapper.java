package org.rcbg.afku.CryptoGenerator.dtos;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.rcbg.afku.CryptoGenerator.k8sClient.models.AsymmetricKeysProfile.AsymmetricKeysProfileCR;
import org.rcbg.afku.CryptoGenerator.k8sClient.models.AsymmetricKeysProfile.AsymmetricKeysProfileSpec;
import org.rcbg.afku.CryptoGenerator.k8sClient.models.PasswordProfile.PasswordProfileCR;
import org.rcbg.afku.CryptoGenerator.k8sClient.models.PasswordProfile.PasswordProfileSpec;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AsymmetricKeysProfileMapper {

    AsymmetricKeysProfileMapper INSTANCE = Mappers.getMapper(AsymmetricKeysProfileMapper.class);

    @Mapping(target = "metadata.profileName", source = "requestBody.profileName")
    @Mapping(target = "metadata.creatorUserId", source = "creatorUserId")
    @Mapping(target = "metadata.creationTimestamp", ignore = true)
    public AsymmetricKeysProfileDTO toFullDto(AsymmetricKeysProfileRequestBody requestBody, String creatorUserId);

    public default AsymmetricKeysProfileCR createProfileResource(AsymmetricKeysProfileRequestBody requestBody, String creatorUserId){
        AsymmetricKeysProfileDTO profileDTO = INSTANCE.toFullDto(requestBody, creatorUserId);
        AsymmetricKeysProfileSpec spec = new AsymmetricKeysProfileSpec();
        spec.setMetadata(profileDTO.getMetadata());
        spec.setProfileProperties(profileDTO.getProfileProperties());
        AsymmetricKeysProfileCR cr = new AsymmetricKeysProfileCR();
        cr.setSpec(spec);
        cr.getMetadata().setName(requestBody.getProfileName());
        return cr;
    }

    @Mapping(target = "metadata", source = "spec.metadata")
    @Mapping(target = "profileProperties", source = "spec.profileProperties")
    public AsymmetricKeysProfileDTO specToDto(AsymmetricKeysProfileSpec spec);
}

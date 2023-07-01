package org.rcbg.afku.CryptoPass.services;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.rcbg.afku.CryptoPass.dto.FullFetchResponseDto;
import org.rcbg.afku.CryptoPass.dto.PasswordSaveRequestDto;
import org.rcbg.afku.CryptoPass.dto.SafeFetchResponseDto;
import org.rcbg.afku.CryptoPass.exceptions.PasswordAccessDenied;
import org.rcbg.afku.CryptoPass.exceptions.PasswordInternalError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

@Slf4j
@Service
@Validated
public class PasswordManagementService {

    private final BCryptPasswordService keyHashingService;
    private final AESCryptographyService passwordEncryptionService;
    private final PasswordDbService dbService;

    @Autowired
    public PasswordManagementService(BCryptPasswordService keyHashingService, AESCryptographyService passwordEncryptionService, PasswordDbService dbService) {
        this.keyHashingService = keyHashingService;
        this.passwordEncryptionService = passwordEncryptionService;
        this.dbService = dbService;
    }

    private void checkOwnership(FullFetchResponseDto data, String ownerUserId){
        if(!Objects.equals(data.getOwnerUserId(), ownerUserId)){
            throw new PasswordAccessDenied("You don't have password with such ID");
        }
    }

    private void checkKey(FullFetchResponseDto data, String key){
        if(! keyHashingService.checkPassword(key, data.getKey())){
            throw new PasswordAccessDenied("Provided key is incorrect");
        }
    }

    private void verifyAccess(FullFetchResponseDto data, String key, String ownerUserId){
        this.checkOwnership(data, ownerUserId);
        this.checkKey(data, key);
        log.info("Access granted to password ID: " + data.getPasswordId() + " for user ID: " + ownerUserId);
    }

    @Validated
    public SafeFetchResponseDto savePassword(@Valid PasswordSaveRequestDto dto, String ownerUserId){
        try {
            String hashedKey = this.keyHashingService.hashPassword(dto.getKey());
            String encryptedPassword = this.passwordEncryptionService.encryptPassword(dto.getPassword(), dto.getKey());
            dto.setPassword(encryptedPassword);
            dto.setKey(hashedKey);
        } catch (Exception e){
            log.error("Error during saving password for user: " + ownerUserId);
            throw new PasswordInternalError(e.getMessage(), e.getClass().getSimpleName());
        }
        return dbService.savePassword(dto, ownerUserId);
    }

    public Page<SafeFetchResponseDto> getListOfPasswords(String ownerUserId, Pageable pageable){
        return dbService.getPasswordsList(ownerUserId, pageable);
    }

    public FullFetchResponseDto getPassword(int passwordId, String key, String ownerUserId){
        FullFetchResponseDto passwordData = dbService.getPassword(passwordId);
        this.verifyAccess(passwordData, key, ownerUserId);
        String decryptedPassword;
        try {
            decryptedPassword = passwordEncryptionService.decryptPassword(passwordData.getPassword(), key);
        } catch (Exception exc){
            throw new PasswordInternalError(exc.getMessage(), exc.getClass().getSimpleName());
        }
        passwordData.setPassword(decryptedPassword);
        return passwordData;
    }

    public void deletePassword(int passwordId, String key, String ownerUserId){
        FullFetchResponseDto passwordData = dbService.getPassword(passwordId);
        this.verifyAccess(passwordData, key, ownerUserId);
        dbService.deletePassword(passwordId);
    }
}

package org.rcbg.afku.CryptoPass.services;

import jakarta.validation.Valid;
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

import java.util.List;

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
        if(data.getOwnerUserId() != ownerUserId){
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
    }

    @Validated
    public SafeFetchResponseDto savePassword(@Valid PasswordSaveRequestDto dto, String ownerUserId){
        // Add encryption logic
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
            decryptedPassword = passwordEncryptionService.decryptePassword(passwordData.getPassword(), key);
        } catch (Exception exc){
            throw new PasswordInternalError(exc.getMessage(), exc.getClass().getSimpleName());
        }
        passwordData.setPassword(decryptedPassword);
        return passwordData;
    }
}

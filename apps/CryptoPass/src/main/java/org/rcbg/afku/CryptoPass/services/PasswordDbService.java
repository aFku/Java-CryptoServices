package org.rcbg.afku.CryptoPass.services;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.rcbg.afku.CryptoPass.domain.Password;
import org.rcbg.afku.CryptoPass.domain.PasswordRepository;
import org.rcbg.afku.CryptoPass.dto.FullFetchResponseDto;
import org.rcbg.afku.CryptoPass.dto.PasswordMapper;
import org.rcbg.afku.CryptoPass.dto.PasswordSaveRequestDto;
import org.rcbg.afku.CryptoPass.dto.SafeFetchResponseDto;
import org.rcbg.afku.CryptoPass.exceptions.PasswordNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Slf4j
@Service
public class PasswordDbService {

    private final PasswordRepository passwordRepository;

    @Autowired
    public PasswordDbService(PasswordRepository passwordRepository){
        this.passwordRepository = passwordRepository;
    }

    public FullFetchResponseDto getPassword(Integer passwordId){
        Password password = passwordRepository.findById(passwordId).orElseThrow(() -> new PasswordNotFound("Password with ID: " + passwordId + "cannot be found"));
        return PasswordMapper.INSTANCE.toFullDto(password);
    }

    @Transactional
    public SafeFetchResponseDto savePassword(PasswordSaveRequestDto passwordDto, String ownerUserId){
        Password password = PasswordMapper.INSTANCE.toEntity(passwordDto, ownerUserId);
        password = passwordRepository.save(password);
        log.info("Password with ID: " + password.getPasswordId() + " has been saved for user: " + ownerUserId);
        return PasswordMapper.INSTANCE.toSafeDto(password);
    }

    @Transactional
    public void deletePassword(int passwordId){
        if(passwordRepository.existsById(passwordId)){
            passwordRepository.deleteById(passwordId);
            log.info("Password with ID: " + passwordId + " has been deleted");
        }else{
            throw new PasswordNotFound("Password with ID: " + passwordId + "cannot be found");
        }
    }

    public Page<SafeFetchResponseDto> getPasswordsList(String ownerUserId, Pageable pageable){
        return passwordRepository.getPasswordsByOwnerUserId(ownerUserId, pageable).map(PasswordMapper.INSTANCE::toSafeDto);
    }
}

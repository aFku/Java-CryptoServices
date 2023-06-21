package org.rcbg.afku.CryptoPass.services;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class BCryptPasswordService {

    @Value("${bcrypt-service.salt.log-rounds}")
    private int saltLogRounds;

    public boolean checkPassword(String givenPassword, String storedPassword){
        return BCrypt.checkpw(givenPassword, storedPassword);
    }

    public String hashPassword(String plainPassword){
        String salt = BCrypt.gensalt(this.saltLogRounds);
        return BCrypt.hashpw(plainPassword, salt);
    }
}

package org.rcbg.afku.CryptoPass.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordRepository extends JpaRepository<Password, Integer> {

    Page<Password> getPasswordsByOwnerUserId(String ownerUserId, Pageable pageable);
}

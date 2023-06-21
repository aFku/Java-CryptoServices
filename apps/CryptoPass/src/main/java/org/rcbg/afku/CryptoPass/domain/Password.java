package org.rcbg.afku.CryptoPass.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor

public class Password {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer passwordId;

    private String ownerUserId;
    private String password;
    private String name;
    private String description;
    private String key;
}

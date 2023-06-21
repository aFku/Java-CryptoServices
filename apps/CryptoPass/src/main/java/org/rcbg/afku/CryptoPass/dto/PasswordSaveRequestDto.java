package org.rcbg.afku.CryptoPass.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordSaveRequestDto {

    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 6, max = 255, message = "Password length should be between 6 and 255")
    private String password;

    @NotEmpty(message = "Name cannot be empty")
    @Size(max = 32, message = "Name cannot be longer than 32 chars")
    private String name;

    @Size(max = 255, message = "Description cannot be longer than 255")
    private String description;

    @NotEmpty(message = "Key cannot be empty")
    @Size(min = 6, max = 32, message = "Key length should be between 6 and 32")
    private String key;
}

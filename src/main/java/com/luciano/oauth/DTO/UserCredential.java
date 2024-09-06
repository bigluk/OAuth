package com.luciano.oauth.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserCredential {
    
    @NotBlank(message = "username must not be blank")
    private String username;

    @NotBlank(message = "password must not be blank")
    private String password;

}

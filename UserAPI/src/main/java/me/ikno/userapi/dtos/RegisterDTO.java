package me.ikno.userapi.dtos;

import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class RegisterDTO {
    @NotNull
    @NotBlank
    @Size(max = 255)
    private String displayName;
    @NotNull
    @NotBlank
    @Size(max = 320)
    @Email(message = " is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String email;
    @NotNull
    @NotBlank
    private String password;
}

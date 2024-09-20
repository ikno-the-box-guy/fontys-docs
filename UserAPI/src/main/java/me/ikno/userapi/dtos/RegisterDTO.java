package me.ikno.userapi.dtos;

import lombok.Getter;

@Getter
public class RegisterDTO {
    private String displayName;
    private String email;
    private String password;
}

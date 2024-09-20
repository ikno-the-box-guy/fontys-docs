package me.ikno.userapi.models;

import lombok.Getter;

@Getter
public class LoginResult {
    private UserModel user;
    private String token;

    public LoginResult(UserModel user, String token) {
        this.user = user;
        this.token = token;
    }
}

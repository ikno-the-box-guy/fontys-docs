package me.ikno.userapi.models;

import lombok.Getter;

@Getter
public class RegisterResult {
    private final UserModel userModel;

    public RegisterResult(UserModel userModel) {
        this.userModel = userModel;
    }
}

package me.ikno.userapi.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResultDTO {
    String displayName;
    String email;
    String rootDirectoryId;
}

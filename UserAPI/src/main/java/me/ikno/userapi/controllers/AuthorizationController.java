package me.ikno.userapi.controllers;

import jakarta.validation.Valid;
import me.ikno.userapi.dtos.LoginDTO;
import me.ikno.userapi.dtos.LoginResultDTO;
import me.ikno.userapi.dtos.RegisterDTO;
import me.ikno.userapi.models.LoginResult;
import me.ikno.userapi.models.RegisterResult;
import me.ikno.userapi.models.UserModel;
import me.ikno.userapi.services.AuthorizationService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
@Validated
public class AuthorizationController {
    private final AuthorizationService authorizationService;

    public AuthorizationController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @PostMapping(value="/auth/login", consumes = "application/json")
    public ResponseEntity<Object> login(@RequestBody @Valid LoginDTO loginRequest) {
        LoginResult loginResult = authorizationService.login(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );

        if(loginResult == null) {
            return ResponseEntity.badRequest().body("Invalid credentials");
        }

        UserModel user = loginResult.getUser();

        LoginResultDTO loginResultDTO = new LoginResultDTO();
        loginResultDTO.setToken(loginResult.getToken());
        loginResultDTO.setDisplayName(user.getDisplayName());
        loginResultDTO.setEmail(user.getEmail());
        loginResultDTO.setRootDirectoryId(user.getRootDirectoryId());

        return ResponseEntity.ok(loginResultDTO);
    }

    @PostMapping(value="/auth/register", consumes = "application/json")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterDTO registerRequest) {
        RegisterResult registerResult = authorizationService.register(
                registerRequest.getDisplayName(),
                registerRequest.getEmail(),
                registerRequest.getPassword()
        );

        return ResponseEntity.ok("User registered successfully");
    }
}

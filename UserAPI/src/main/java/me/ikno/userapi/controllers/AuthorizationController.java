package me.ikno.userapi.controllers;

import me.ikno.userapi.dtos.LoginDTO;
import me.ikno.userapi.dtos.RegisterDTO;
import me.ikno.userapi.models.LoginResult;
import me.ikno.userapi.models.RegisterResult;
import me.ikno.userapi.services.AuthorizationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class AuthorizationController {
    private final AuthorizationService authorizationService;

    public AuthorizationController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @PostMapping(value="/auth/login", consumes = "application/json")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginRequest) {
        LoginResult loginResult = authorizationService.login(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );

        if(loginResult == null) {
            return ResponseEntity.badRequest().body("Invalid credentials");
        }

        return ResponseEntity.ok(loginResult.getToken());
    }

    @PostMapping(value="/auth/register", consumes = "application/json")
    public ResponseEntity<String> register(@RequestBody RegisterDTO registerRequest) {
        RegisterResult registerResult = authorizationService.register(
                registerRequest.getDisplayName(),
                registerRequest.getEmail(),
                registerRequest.getPassword()
        );

        return ResponseEntity.ok("User registered successfully");
    }
}

package me.ikno.documentapi.controllers;

import me.ikno.documentapi.services.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("api/v1")
public class UserController {
    final JwtService jwtService;

    public UserController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @GetMapping("/login")
    public ResponseEntity<String> createUser() {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("role", "user");

        String jwt = jwtService.generateToken("erwtje013@gmail.com", claims);
        return ResponseEntity.ok(jwt);
    }
}

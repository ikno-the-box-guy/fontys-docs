package me.ikno.userapi.services;

import me.ikno.userapi.exceptions.EmailTakenException;
import me.ikno.userapi.exceptions.InvalidCredentialsException;
import me.ikno.userapi.models.LoginResult;
import me.ikno.userapi.models.RegisterResult;
import me.ikno.userapi.models.UserModel;
import me.ikno.userapi.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashMap;
import java.util.Optional;

@Service
public class AuthorizationService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public AuthorizationService(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        encoder = new BCryptPasswordEncoder(10);
    }

    public LoginResult login(String email, String password) {
        Optional<UserModel> userModel = userRepository.findByEmail(email);

        if(userModel.isEmpty()) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        UserModel user = userModel.get();
        if(encoder.matches(password, user.getPassword())) {
            HashMap<String, Object> claims = new HashMap<>();
            claims.put("jti", user.getId().toString());

            return new LoginResult(
                user,
                jwtService.generateToken(email, claims)
            );
        }

        throw new InvalidCredentialsException("Invalid credentials");
    }

    public RegisterResult register(String displayName, String email, String password) {
        UserModel userModel = new UserModel();
        userModel.setDisplayName(displayName);
        userModel.setEmail(email);
        userModel.setPassword(encoder.encode(password));
        userModel.setRootDirectoryId(1);

        try {
            userRepository.save(userModel);
        } catch (Exception e) {
            throw new EmailTakenException("Email " + email + " is already taken");
        }

        return new RegisterResult(userModel);
    }
}

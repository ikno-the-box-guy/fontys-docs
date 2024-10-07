package me.ikno.userapi.services;

import me.ikno.userapi.clients.DocumentClient;
import me.ikno.userapi.exceptions.EmailTakenException;
import me.ikno.userapi.exceptions.InvalidCredentialsException;
import me.ikno.userapi.models.LoginResult;
import me.ikno.userapi.models.RegisterResult;
import me.ikno.userapi.models.UserModel;
import me.ikno.userapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashMap;
import java.util.Optional;

@Service
public class AuthorizationService {
    private final DocumentClient documentClient;
    @Value("${password.pepper}")
    private String pepper;

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public AuthorizationService(JwtService jwtService, UserRepository userRepository, DocumentClient documentClient) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;

        encoder = new BCryptPasswordEncoder(10);
        this.documentClient = documentClient;
    }

    public LoginResult login(String email, String password) {
        Optional<UserModel> userModel = userRepository.findByEmail(email);

        if(userModel.isEmpty()) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        UserModel user = userModel.get();
        if(encoder.matches(pepper + password, user.getPassword())) {
            HashMap<String, Object> claims = new HashMap<>();
            claims.put("userId", user.getId().toString());
            claims.put("rootDirectoryId", user.getRootDirectoryId());

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
        userModel.setPassword(encoder.encode(pepper + password));
        userModel.setRootDirectoryId("0");

        UserModel newUser;
        try {
            newUser = userRepository.save(userModel);
        } catch (Exception e) {
            throw new EmailTakenException("Email " + email + " has already been taken");
        }

        Optional<String> rootDirectoryId = documentClient.createRootDirectory(newUser.getId());
        if(rootDirectoryId.isEmpty()) {
            userRepository.delete(newUser);
            throw new RuntimeException("Failed to create root directory");
        }

        newUser.setRootDirectoryId(rootDirectoryId.get());
        userRepository.save(newUser);

        return new RegisterResult(newUser);
    }
}

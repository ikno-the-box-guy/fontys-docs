package me.ikno.userapi.services;

import me.ikno.userapi.exceptions.EmailTakenException;
import me.ikno.userapi.exceptions.InvalidCredentialsException;
import me.ikno.userapi.models.LoginResult;
import me.ikno.userapi.models.RegisterResult;
import me.ikno.userapi.models.UserModel;
import me.ikno.userapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Optional;

@Service
public class AuthorizationService {
    @Value("${password.pepper}")
    private String pepper;

    @Value("${api.secret}")
    private String apiSecret;

    @Value("${api.doc-url}")
    private String docApiUrl;

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

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "token=" + apiSecret);

        ResponseEntity<String> rootResponse = restTemplate.exchange(
                docApiUrl + "/directories/root/" + newUser.getId(),
                HttpMethod.POST,
                new HttpEntity<>(headers),
                String.class
        );

        String rootDirectoryId = rootResponse.getBody();
        if(rootDirectoryId == null) {
            userRepository.delete(newUser);
            throw new RuntimeException("Failed to create root directory");
        }

        newUser.setRootDirectoryId(rootDirectoryId);
        userRepository.save(newUser);

        return new RegisterResult(newUser);
    }
}

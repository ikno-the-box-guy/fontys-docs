package me.ikno.userapi.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class DocumentClient {
    @Value("${api.secret}")
    private String apiSecret;

    @Value("${api.doc-url}")
    private String docApiUrl;

    private final RestTemplate restTemplate;

    public DocumentClient() {
        this.restTemplate = new RestTemplate();
    }

    public Optional<String> createRootDirectory(int userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "token=" + apiSecret);

        try {
            ResponseEntity<String> rootResponse = restTemplate.exchange(
                    docApiUrl + "/directories/root/" + userId,
                    HttpMethod.POST,
                    new HttpEntity<>(headers),
                    String.class
            );

            return Optional.ofNullable(rootResponse.getBody());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}

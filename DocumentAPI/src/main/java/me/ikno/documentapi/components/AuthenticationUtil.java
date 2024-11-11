package me.ikno.documentapi.components;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationUtil {
    public int getUserId() {
        var context = SecurityContextHolder.getContext();
        var authentication = context.getAuthentication();
        var credentials = authentication.getCredentials();
        Claims claims = (Claims) credentials;
        return Integer.parseInt(claims.get("userId", String.class));
    }

    public int getRootDirectoryId() {
        Claims claims = (Claims) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        return Integer.parseInt(claims.get("rootDirectoryId", String.class));
    }

    public String getEmail() {
        Claims claims = (Claims) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        return claims.getSubject();
    }
}

package me.ikno.documentapi.components;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationUtil {
    public int getUserId() {
        Claims claims = (Claims) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        return Integer.parseInt(claims.get("jti", String.class));
    }
}

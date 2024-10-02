package me.ikno.documentapi.middleware;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.ikno.documentapi.exceptions.InvalidTokenException;
import me.ikno.documentapi.services.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws
            ServletException,
            IOException
    {
        try {
            String token = getTokenFromCookie(request);

            if(token == null) {
                throw new InvalidTokenException("No token provided");
            }

            if(token.isBlank()) {
                throw new InvalidTokenException("No token provided");
            }

            // TODO: Check if token has expired
            if(!jwtService.isValidToken(token)) {
                throw new InvalidTokenException("Invalid token");
            }

            String email = jwtService.extractEmail(token);
            Claims claims = jwtService.extractClaims(token);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    email,
                    claims,
                    List.of()
            );

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);

            filterChain.doFilter(request, response);
        } catch (InvalidTokenException ex) {
            String jsonResponse =
                    "{\"timestamp\":\"" + LocalDateTime.now() + "\"," +
                    "\"status\":" + HttpStatus.UNAUTHORIZED.value() + "," +
                    "\"error\":\"Unauthorized\"," +
                    "\"message\":\"" + ex.getMessage() + "\"," +
                    "\"path\":\"" + request.getRequestURI() + "\"}";

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write(jsonResponse);
        }
    }

    private String getTokenFromCookie(HttpServletRequest request) {
        for(Cookie cookie : request.getCookies()) {
            if(cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }

        return null;
    }
}

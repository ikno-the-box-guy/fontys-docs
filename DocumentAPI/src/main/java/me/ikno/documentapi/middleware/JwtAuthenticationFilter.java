package me.ikno.documentapi.middleware;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.ikno.documentapi.exceptions.InvalidTokenException;
import me.ikno.documentapi.services.JwtService;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${api.secret}")
    private String apiSecret;

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
                filterChain.doFilter(request, response);
                return;
//                throw new InvalidTokenException("No token provided");
            }

            if(token.isBlank()) {
                filterChain.doFilter(request, response);
                return;
//                throw new InvalidTokenException("No token provided");
            }

            if(token.equals(apiSecret)) {
                UsernamePasswordAuthenticationToken apiAuthToken = new UsernamePasswordAuthenticationToken(
                        null,
                        null,
                        List.of(() -> "SCOPE_api")
                );

                apiAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(apiAuthToken);

                filterChain.doFilter(request, response);
                return;
            }

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
        if(request == null || request.getCookies() == null) {
            return null;
        }

        for(Cookie cookie : request.getCookies()) {
            if(cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }

        return null;
    }
}

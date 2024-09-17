package me.ikno.documentapi.middleware;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.ikno.documentapi.exceptions.InvalidTokenException;
import me.ikno.documentapi.exceptions.MissingTokenException;
import me.ikno.documentapi.services.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
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
            String token = request.getHeader("Authorization");

            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);

                if(!jwtService.isValidToken(token)) {
                    throw new InvalidTokenException("Invalid token");
                }

                String email = jwtService.extractEmail(token);
                Claims claims = jwtService.extractClaims(token);

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        List.of(new SimpleGrantedAuthority((String) claims.get("role")))
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

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
}

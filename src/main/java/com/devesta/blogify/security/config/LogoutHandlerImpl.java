package com.devesta.blogify.security.config;

import com.devesta.blogify.security.jwt.Token;
import com.devesta.blogify.security.jwt.TokenRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@RequiredArgsConstructor
public class LogoutHandlerImpl implements LogoutHandler {

    private final TokenRepository tokenRepository;


    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        final String authHeader = request.getHeader(AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        final String jwtToken = authHeader.substring(7);
        Token token = tokenRepository.findByToken(jwtToken).orElse(null);

        if (token != null) {
            token.setExpired(true);
            token.setRevoked(true);
            tokenRepository.save(token);
        }

        inValidateCookie(response);

    }

    private static void inValidateCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}

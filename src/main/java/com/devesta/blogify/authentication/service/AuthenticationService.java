package com.devesta.blogify.authentication.service;

import com.devesta.blogify.authentication.payload.AuthenticationResponse;
import com.devesta.blogify.authentication.payload.LoginRequest;
import com.devesta.blogify.authentication.payload.RegisterRequest;
import com.devesta.blogify.exception.exceptions.EmailAlreadyExistsException;
import com.devesta.blogify.exception.exceptions.UserAlreadyExistsException;
import com.devesta.blogify.security.jwt.JwtService;
import com.devesta.blogify.security.jwt.Token;
import com.devesta.blogify.security.jwt.TokenRepository;
import com.devesta.blogify.user.UserRepository;
import com.devesta.blogify.user.domain.Role;
import com.devesta.blogify.user.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;

    public AuthenticationResponse register(@NotNull RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new UserAlreadyExistsException(registerRequest.getUsername());
        }
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new EmailAlreadyExistsException(registerRequest.getEmail());
        }

        User user = User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .email(registerRequest.getEmail())
                .role(Role.USER)
                .build();

        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public AuthenticationResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword())
        );
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));

        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user.getUserId());
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void revokeAllUserTokens(Long uid) {
        List<Token> tokens = tokenRepository.findAllValidTokenByUserId(uid);
        if (tokens.isEmpty())
            return;
        tokens.forEach(token -> {
            token.setRevoked(true);
            token.setExpired(true);
        });

        tokenRepository.saveAll(tokens);

    }

    private void saveUserToken(User user, String token) {
        Token userToken = Token.builder()
                .user(user)
                .token(token)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(userToken);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authHeader = request.getHeader(AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return;

        final String refreshToken = authHeader.substring(7);
        final String userName = jwtService.extractUsername(refreshToken);
        if (userName != null) {
            User userDetails = userRepository.findByUsername(userName).orElseThrow();

            if (jwtService.isValidToken(refreshToken, userDetails)) {
                String jwtToken = jwtService.generateToken(userDetails);
                revokeAllUserTokens(userDetails.getUserId());
                saveUserToken(userDetails, jwtToken);
                AuthenticationResponse authResponse = AuthenticationResponse
                        .builder()
                        .accessToken(jwtToken)
                        .refreshToken(refreshToken)
                        .build();

                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}

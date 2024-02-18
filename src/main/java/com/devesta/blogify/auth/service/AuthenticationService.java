package com.devesta.blogify.auth.service;

import com.devesta.blogify.auth.payload.AuthenticationResponse;
import com.devesta.blogify.auth.payload.LoginRequest;
import com.devesta.blogify.auth.payload.RegisterRequest;
import com.devesta.blogify.domain.entity.enumerated.Role;
import com.devesta.blogify.domain.entity.User;
import com.devesta.blogify.exception.exceptions.EmailAlreadyExistsException;
import com.devesta.blogify.exception.exceptions.UserAlreadyExistsException;
import com.devesta.blogify.repository.UserRepository;
import com.devesta.blogify.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest registerRequest) {
        if(userRepository.existsByUsername(registerRequest.getUsername())){
            throw new UserAlreadyExistsException(registerRequest.getUsername());
        }
        if(userRepository.existsByEmail(registerRequest.getEmail())){
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
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword())
        );
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();

    }
}

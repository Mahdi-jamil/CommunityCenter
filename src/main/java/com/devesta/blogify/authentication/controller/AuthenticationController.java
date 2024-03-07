package com.devesta.blogify.authentication.controller;

import com.devesta.blogify.authentication.payload.AuthenticationResponse;
import com.devesta.blogify.authentication.service.AuthenticationService;
import com.devesta.blogify.authentication.payload.LoginRequest;
import com.devesta.blogify.authentication.payload.RegisterRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authentication")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody @Valid RegisterRequest registerRequest,
            HttpServletResponse response) {
        AuthenticationResponse authenticationResponse =
                authenticationService.register(registerRequest);
        setToken(response, authenticationResponse);

        return ResponseEntity.ok(authenticationResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody @Valid LoginRequest loginRequest,
            HttpServletResponse response) {
        AuthenticationResponse authenticationResponse =
                authenticationService.login(loginRequest);
        setToken(response, authenticationResponse);

        return ResponseEntity.ok(authenticationResponse);
    }

    private static void setToken(HttpServletResponse response, AuthenticationResponse authenticationResponse) {
        Cookie cookie = new Cookie("token", authenticationResponse.getToken());
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int) (System.currentTimeMillis() + 60 * 1000 * 60 * 24));
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response){
        inValidateToken(response);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private static void inValidateToken(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}

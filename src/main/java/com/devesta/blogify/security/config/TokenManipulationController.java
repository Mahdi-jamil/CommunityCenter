package com.devesta.blogify.security.config;

import com.devesta.blogify.security.jwt.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenManipulationController {

    private final TokenRepository tokenRepository;

    @PostMapping("/api/v1/revoke_tokens")
    public ResponseEntity<Void> expireAllTokens(){
        tokenRepository.revokeAllToken();
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

package com.devesta.blogify.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
public class JwtService {

    private final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();
    private final long tokenExpiration = 60 * 1000 * 60; // hour
    private final Logger logger = LoggerFactory.getLogger(JwtService.class);

    public String extractUsername(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, tokenExpiration);
    }

    public String generateRefreshToken( UserDetails userDetails) {
        long refreshTokenExpiration = tokenExpiration * 24; // day
        return buildToken(new HashMap<>(), userDetails, refreshTokenExpiration);
    }

    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    public boolean isValidToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && isTokenNotExpired(token);
    }

    private boolean isTokenNotExpired(String token) {
        return getClaim(token, Claims::getExpiration).after(new Date());
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {

        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException ex) {
            logger.error("Expired token: {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            logger.error("Malformed JWT: {}", ex.getMessage());
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            logger.error("Invalid JWT token: {}", ex.getMessage());
        }
        throw new AuthenticationException("Error parsing JWT -> Access Denied ") {
        };
    }

    private SecretKey getSigningKey() {
        return SECRET_KEY;
//        byte[] keyBytes = "khjvbakafldn487052nksvfojbhwndhkrgosvif".getBytes(StandardCharsets.UTF_8);
//        return Keys.hmacShaKeyFor(keyBytes);
    }
}

package com.devesta.blogify.authentication;

import com.devesta.blogify.authentication.payload.AuthenticationResponse;
import com.devesta.blogify.authentication.payload.LoginRequest;
import com.devesta.blogify.authentication.payload.RegisterRequest;
import com.devesta.blogify.authentication.service.AuthenticationService;
import com.devesta.blogify.exception.exceptions.EmailAlreadyExistsException;
import com.devesta.blogify.exception.exceptions.UserAlreadyExistsException;
import com.devesta.blogify.security.jwt.JwtService;
import com.devesta.blogify.security.jwt.TokenRepository;
import com.devesta.blogify.user.UserRepository;
import com.devesta.blogify.user.domain.Role;
import com.devesta.blogify.user.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    public void testThatRegisterReturnsJwt() {
        when(userRepository.existsByUsername(any())).thenReturn(false);
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(new User());
        when(jwtService.generateToken(any())).thenReturn("mockedJWTToken");
        when(jwtService.generateRefreshToken(any())).thenReturn("mockedRefreshToken");
        RegisterRequest registerRequest = new RegisterRequest("testUser", "testPassword", "test@example.com");

        AuthenticationResponse authenticationResponse = authenticationService.register(registerRequest);

        Assertions.assertThat(authenticationResponse).isNotNull();
        Assertions.assertThat(authenticationResponse.getAccessToken()).isEqualTo("mockedJWTToken");
    }

    @Test
    public void testThatRegisterWithExistingUsernameThrowException() {
        when(userRepository.existsByUsername(any())).thenReturn(true);
        RegisterRequest registerRequest = new RegisterRequest("ExistingUser", "testPassword", "test@example.com");

        assertThatThrownBy(() -> authenticationService.register(registerRequest))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining("User with username: ExistingUser already exists!");
    }

    @Test
    public void testThatRegisterWithExistingEmailThrowException() {
        when(userRepository.existsByUsername(any())).thenReturn(false);
        when(userRepository.existsByEmail(any())).thenReturn(true);
        RegisterRequest registerRequest = new RegisterRequest("ExistingUser", "testPassword", "ExistingEmail@example.com");

        assertThatThrownBy(() -> authenticationService.register(registerRequest))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessageContaining("User with email: ExistingEmail@example.com already exists!");
    }

    @Test
    public void testLogin_Successful() {
        LoginRequest loginRequest = new LoginRequest("testUser", "testPassword");
        User user = User.builder()
                .userId(1L)
                .username("testUser")
                .password(passwordEncoder.encode("testPassword"))
                .email("test@example.com")
                .role(Role.USER)
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any())).thenReturn("mockedJWTToken");
        when(jwtService.generateRefreshToken(any())).thenReturn("mockedRefreshToken");

        AuthenticationResponse authenticationResponse = authenticationService.login(loginRequest);

        Assertions.assertThat(authenticationResponse).isNotNull();
        Assertions.assertThat(authenticationResponse.getAccessToken()).isEqualTo("mockedJWTToken");
    }

    @Test
    public void testThatUserNotAuthenticatedUserCannotLogin(){
        LoginRequest loginRequest = new LoginRequest("testUser", "testPassword");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("user not found"));

        assertThatThrownBy(() -> authenticationService.login(loginRequest))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessageContaining("user not found");
    }

    @Test
    public void testThatNotRegisteredUserCannotLogin(){
        LoginRequest loginRequest = new LoginRequest("testUser", "testPassword");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userRepository.findByUsername("testUser")).thenThrow(new UsernameNotFoundException("user not found"));

        assertThatThrownBy(() -> authenticationService.login(loginRequest))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("user not found");

    }


}

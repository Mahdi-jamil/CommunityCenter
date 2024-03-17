package com.devesta.blogify.authentication;

import com.devesta.blogify.authentication.payload.AuthenticationResponse;
import com.devesta.blogify.authentication.payload.LoginRequest;
import com.devesta.blogify.authentication.payload.RegisterRequest;
import com.devesta.blogify.authentication.service.AuthenticationService;
import com.devesta.blogify.exception.exceptions.EmailAlreadyExistsException;
import com.devesta.blogify.exception.exceptions.UserAlreadyExistsException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@WithMockUser
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class AuthenticationControllerIntegrationTests {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthenticationService authenticationService;

    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void testThatMemberCanRegisterAndTakeJwt() throws Exception {
        RegisterRequest request =
                new RegisterRequest("mahdi", "root123", "jamilmahdi77@gmail.com");
        String jsonRequest = objectMapper.writeValueAsString(request);
        AuthenticationResponse authenticationResponse =
                new AuthenticationResponse("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJleGFtcGxlVXNlciIsImlhdCI6MTY0NjQyOTcxOCwiZXhwIjoxNjQ2NDMzMzE4fQ.I4tvcDgS1rz8Gqo3UH00mjRxjCp8ncpYi3HX-BriWaQ");
        Mockito.when(authenticationService.register(request)).thenReturn(authenticationResponse);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/authentication/register")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.token").value("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJleGFtcGxlVXNlciIsImlhdCI6MTY0NjQyOTcxOCwiZXhwIjoxNjQ2NDMzMzE4fQ.I4tvcDgS1rz8Gqo3UH00mjRxjCp8ncpYi3HX-BriWaQ")
        );
    }

    @Test
    public void testThatMemberCannotRegisterWithSameUsername() throws Exception {
        RegisterRequest request =
                new RegisterRequest("mahdi", "root1234", "jamilmahdi789@gmail.com");
        Mockito.when(authenticationService.register(request)).thenThrow(new UserAlreadyExistsException("user already exists"));
        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/authentication/register")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isConflict()
        );
    }

    @Test
    public void testThatMemberCannotRegisterWithSameEmail() throws Exception {
        RegisterRequest request =
                new RegisterRequest("mahdi jamil", "root1234", "jamilmahdi77@gmail.com");
        Mockito.when(authenticationService.register(request)).thenThrow(new EmailAlreadyExistsException("email already exists"));
        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/authentication/register")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isConflict()
        );
    }

    @Test
    public void testThatUserCanLoginAndTakeJwt() throws Exception {
        LoginRequest request = new LoginRequest("mahdi", "root123");
        String jsonRequest = objectMapper.writeValueAsString(request);
        AuthenticationResponse authenticationResponse =
                new AuthenticationResponse("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJleGFtcGxlVXNlciIsImlhdCI6MTY0NjQyOTcxOCwiZXhwIjoxNjQ2NDMzMzE4fQ.I4tvcDgS1rz8Gqo3UH00mjRxjCp8ncpYi3HX-BriWaQ");
        Mockito.when(authenticationService.login(request)).thenReturn(authenticationResponse);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/authentication/login")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.token").isString()
        );
    }

    @Test
    public void testThatNotAuthenticateRequestGet401() throws Exception {
        LoginRequest request = new LoginRequest("notAuthenticated", "noFound");
        String jsonRequest = objectMapper.writeValueAsString(request);
        Mockito.when(authenticationService.login(request)).thenThrow(new BadCredentialsException("not authenticated "));

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/authentication/login")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isUnauthorized()
        );
    }
}

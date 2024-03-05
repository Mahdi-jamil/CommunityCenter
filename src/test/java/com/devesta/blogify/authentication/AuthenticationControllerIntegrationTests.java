package com.devesta.blogify.authentication;

import com.devesta.blogify.authentication.payload.LoginRequest;
import com.devesta.blogify.authentication.payload.RegisterRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@AutoConfigureMockMvc
@WithMockUser
public class AuthenticationControllerIntegrationTests {

    private final ObjectMapper objectMapper;
    private final MockMvc mockMvc;

    @Autowired
    public AuthenticationControllerIntegrationTests(ObjectMapper objectMapper, MockMvc mockMvc) {
        this.objectMapper = objectMapper;
        this.mockMvc = mockMvc;
    }

    @Test
    public void testThatMemberCanRegisterAndTakeJwt() throws Exception {
        RegisterRequest request =
                new RegisterRequest("mahdi", "root123", "jamilmahdi77@gmail.com");

        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/authentication/register")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.token").isString()
        );
    }

    @Test
    public void testThatMemberCannotRegisterWithSameUsername() throws Exception {
        RegisterRequest request =
                new RegisterRequest("mahdi", "root1234", "jamilmahdi78@gmail.com");

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

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/authentication/login")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isUnauthorized()
        );
    }
}

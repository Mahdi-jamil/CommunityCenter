package com.devesta.blogify.controller;

import com.devesta.blogify.authentication.payload.RegisterRequest;
import com.devesta.blogify.user.domain.UpdatePayLoad;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@WithMockUser
@Profile("test")
public class UserControllerIntegrationTests {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    LocalDate today = LocalDate.now();
    int year = today.getYear();
    int month = today.getMonthValue();
    int day = today.getDayOfMonth();

    @Autowired
    public UserControllerIntegrationTests(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @BeforeEach
    public void setUp() throws Exception {
        RegisterRequest request =
                new RegisterRequest("mahdi", "root123", "jamilmahdi77@gmail.com");
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/authentication/register")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }

    @Test
    public void testThatGetReturnUser() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/users/{username}", "mahdi")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.username").value("mahdi")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value("jamilmahdi77@gmail.com")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.role").value("USER")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.createdDate[0]").value(year)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.createdDate[1]").value(month)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.createdDate[2]").value(day)
        );
    }

    @Test
    public void testThatGetAllReturnUsers() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/users")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].username").value("mahdi")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].email").value("jamilmahdi77@gmail.com")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].role").value("USER")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].createdDate[0]").value(year)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].createdDate[1]").value(month)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].createdDate[2]").value(day)
        );
    }

    @Test
    public void testThatGetReturn404IfNotFound() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/users/{username}", "userNotFound")
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatGetReturn200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/users/{username}", "mahdi")
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatGetAllReturn200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/users")
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Disabled
    public void testThatUserCanBeUpdated() throws Exception {
        UpdatePayLoad updatePayLoad = UpdatePayLoad.builder()
                .email("jamilmahdi78@gmail.com")
                .build();
        String value = objectMapper.writeValueAsString(updatePayLoad);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/v1/users/{uid}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(value)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value("jamilmahdi78@gmail.com")
        );

    }

}

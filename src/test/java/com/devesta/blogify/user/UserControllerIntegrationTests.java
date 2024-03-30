package com.devesta.blogify.user;

import com.devesta.blogify.exception.exceptions.notfound.UserNotFoundException;
import com.devesta.blogify.user.domain.UpdatePayLoad;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WithMockUser
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UserControllerIntegrationTests {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final UserDataUtils utils = new UserDataUtils();
    @MockBean
    private final UserService userService;

    @Autowired
    public UserControllerIntegrationTests(MockMvc mockMvc, ObjectMapper objectMapper, UserService userService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.userService = userService;
    }


    @Test
    public void testThatGetReturnUser() throws Exception {
        when(userService.getDetailedUser(1L)).thenReturn(utils.fullDetailUser);

        mockMvc.perform(
                get("/api/v1/users/{uid}", 1L)
        ).andExpect(
                jsonPath("$.username").value("mahdi")
        ).andExpect(
                jsonPath("$.email").value("jamilmahdi77@gmail.com")
        ).andExpect(
                jsonPath("$.role").value("USER")
        ).andExpect(
                jsonPath("$.createdDate[0]").value(utils.year)
        ).andExpect(
                jsonPath("$.createdDate[1]").value(utils.month)
        ).andExpect(
                jsonPath("$.createdDate[2]").value(utils.day)
        );

        verify(userService).getDetailedUser(1L);
    }

    @Test
    public void testThatGetAllReturnUsers() throws Exception {
        // Arrange
        when(userService.getAllUser()).thenReturn(Collections.singletonList(utils.userDto1));

        // Act
        mockMvc.perform(
                get("/api/v1/users")
        ).andExpect(
                jsonPath("$[0].username").value("mahdi")
        ).andExpect(
                jsonPath("$[0].image_url").value("image_url1")
        ).andExpect(
                jsonPath("$[0].createdDate[0]").value(utils.year)
        ).andExpect(
                jsonPath("$[0].createdDate[1]").value(utils.month)
        ).andExpect(
                jsonPath("$[0].createdDate[2]").value(utils.day)
        );

        // Assert
        verify(userService).getAllUser();
    }

    @Test
    public void testThatGetReturn404IfNotFound() throws Exception {
        when(userService.getDetailedUser(2L)).thenThrow(new UserNotFoundException());
        mockMvc.perform(
                get("/api/v1/users/{uid}", 2L)
        ).andExpect(status().isNotFound());
    }

    @Test
    public void testThatGetReturn200() throws Exception {
        when(userService.getDetailedUser(1L)).thenReturn(utils.fullDetailUser);
        mockMvc.perform(
                get("/api/v1/users/{uid}",1L)
        ).andExpect(status().isOk());
    }

    @Test
    public void testThatGetAllReturn200() throws Exception {
        when(userService.getAllUser()).thenReturn(Collections.singletonList(utils.userDto1));
        mockMvc.perform(
                get("/api/v1/users")
        ).andExpect(status().isOk());
    }

    @Test
    @Disabled
    public void testThatUserCanBeUpdated() throws Exception {
        UpdatePayLoad updatePayLoad = UpdatePayLoad.builder()
                .email("jamilmahdi78@gmail.com")
                .password(null)
                .build();
        when(userService.partialUpdate(updatePayLoad, 1L)).thenReturn(utils.userDto1Updated);

        mockMvc.perform(
                patch("/api/v1/users/{uid}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePayLoad))
                        .characterEncoding("utf-8")
        ).andExpect(
                status().isOk()
        ).andExpect(
                jsonPath("$.email").value("jamilmahdi78@gmail.com")
        );

        Mockito.verify(userService, times(1)).partialUpdate(updatePayLoad, 1L);
    }

}

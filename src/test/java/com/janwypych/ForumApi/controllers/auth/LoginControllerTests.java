package com.janwypych.ForumApi.controllers.auth;

import com.janwypych.ForumApi.TestDataUtil;
import com.janwypych.ForumApi.dtos.auth.AuthResponse;
import com.janwypych.ForumApi.dtos.auth.LoginRequest;
import com.janwypych.ForumApi.exceptions.InvalidCredentialsException;
import com.janwypych.ForumApi.services.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AccountService accountService;

    @Test
    public void testThatLoginReturnsHttp400WhenEmailIsBlank() throws Exception {
        LoginRequest loginRequest = TestDataUtil.createLoginRequest();
        loginRequest.setEmail(" ");
        String loginJson = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    @Test
    public void testThatLoginReturnsHttp400WhenEmailIsTooShort() throws Exception {
        LoginRequest loginRequest = TestDataUtil.createLoginRequest();
        loginRequest.setEmail("a");
        String loginJson = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    @Test
    public void testThatLoginReturnsHttp400WhenEmailIsTooLong() throws Exception {
        LoginRequest loginRequest = TestDataUtil.createLoginRequest();
        loginRequest.setEmail("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@testaaaaaaaaaaaaaaaaaaaaaaaaaaaa.com");
        String loginJson = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    @Test
    public void testThatLoginReturnsHttp400WhenEmailIsBadFormat() throws Exception {
        LoginRequest loginRequest = TestDataUtil.createLoginRequest();
        loginRequest.setEmail("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@test.com");
        String loginJson = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    @Test
    public void testThatLoginReturnsHttp400WhenPasswordIsBlank() throws Exception {
        LoginRequest loginRequest = TestDataUtil.createLoginRequest();
        loginRequest.setPassword("");
        String loginJson = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    @Test
    public void testThatLoginReturnsHttp400WhenPasswordIsTooShort() throws Exception {
        LoginRequest loginRequest = TestDataUtil.createLoginRequest();
        loginRequest.setPassword("aaa");
        String loginJson = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    @Test
    public void testThatLoginReturnsHttp400WhenPasswordIsTooLong() throws Exception {
        LoginRequest loginRequest = TestDataUtil.createLoginRequest();
        loginRequest.setPassword("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        String loginJson = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    @Test
    public void testThatLoginReturnsHttp401WhenCredentialsAreInvalid() throws Exception {
        LoginRequest loginRequest = TestDataUtil.createLoginRequest();
        String loginJson = objectMapper.writeValueAsString(loginRequest);

        when(accountService.login(any(LoginRequest.class)))
                .thenThrow(new InvalidCredentialsException("Invalid credentials"));

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson)
        ).andExpect(
                MockMvcResultMatchers.status().isUnauthorized()
        );
    }

    @Test
    public void testThatLoginReturnsHttp200WhenCredentialsAreValid() throws Exception {
        LoginRequest loginRequest = TestDataUtil.createLoginRequest();
        String loginJson = objectMapper.writeValueAsString(loginRequest);

        when(accountService.login(any(LoginRequest.class)))
                .thenReturn(new AuthResponse("jwt-token"));

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatLoginReturnsTokenWhenCredentialsAreValid() throws Exception {
        LoginRequest loginRequest = TestDataUtil.createLoginRequest();
        String loginJson = objectMapper.writeValueAsString(loginRequest);

        when(accountService.login(any(LoginRequest.class)))
                .thenReturn(new AuthResponse("jwt-token"));

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.token").isNotEmpty()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.token").isString()
        );
    }
}
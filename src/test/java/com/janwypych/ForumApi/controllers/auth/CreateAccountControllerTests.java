package com.janwypych.ForumApi.controllers.auth;


import com.janwypych.ForumApi.TestDataUtil;
import com.janwypych.ForumApi.dtos.auth.AuthResponse;
import com.janwypych.ForumApi.dtos.auth.CreateAccountRequest;
import com.janwypych.ForumApi.exceptions.AccountAlreadyExistsException;
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
public class CreateAccountControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AccountService accountService;

    @Test
    public void testThatCreateAccountReturnsHttp400WhenUsernameIsBlank() throws Exception {
        CreateAccountRequest createAccountRequest = TestDataUtil.createAccountRequest();
        createAccountRequest.setUsername(" ");
        String createAccountJson = objectMapper.writeValueAsString(createAccountRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createAccountJson)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    @Test
    public void testThatCreateAccountReturnsHttp400WhenUsernameIsTooShort() throws Exception {
        CreateAccountRequest createAccountRequest = TestDataUtil.createAccountRequest();
        createAccountRequest.setUsername("a");
        String createAccountJson = objectMapper.writeValueAsString(createAccountRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createAccountJson)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    @Test
    public void testThatCreateAccountReturnsHttp400WhenUsernameIsTooLong() throws Exception {
        CreateAccountRequest createAccountRequest = TestDataUtil.createAccountRequest();
        createAccountRequest.setUsername("aaaaaaaaaaaaaaaaaaaaa");
        String createAccountJson = objectMapper.writeValueAsString(createAccountRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createAccountJson)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    @Test
    public void testThatCreateAccountReturnsHttp400WhenUsernameHasInvalidBeginning() throws Exception {
        CreateAccountRequest createAccountRequest = TestDataUtil.createAccountRequest();
        createAccountRequest.setUsername("_jan");
        String createAccountJson = objectMapper.writeValueAsString(createAccountRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createAccountJson)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    @Test
    public void testThatCreateAccountReturnsHttp400WhenUsernameHasInvalidSpecialCharacter() throws Exception {
        CreateAccountRequest createAccountRequest = TestDataUtil.createAccountRequest();
        createAccountRequest.setUsername("jan ");
        String createAccountJson = objectMapper.writeValueAsString(createAccountRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createAccountJson)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    @Test
    public void testThatCreateAccountReturnsHttp400WhenEmailIsBlank() throws Exception {
        CreateAccountRequest createAccountRequest = TestDataUtil.createAccountRequest();
        createAccountRequest.setEmail(" ");
        String createAccountJson = objectMapper.writeValueAsString(createAccountRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createAccountJson)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    @Test
    public void testThatCreateAccountReturnsHttp400WhenEmailIsTooShort() throws Exception {
        CreateAccountRequest createAccountRequest = TestDataUtil.createAccountRequest();
        createAccountRequest.setEmail("a");
        String createAccountJson = objectMapper.writeValueAsString(createAccountRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createAccountJson)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    @Test
    public void testThatCreateAccountReturnsHttp400WhenEmailIsTooLong() throws Exception {
        CreateAccountRequest createAccountRequest = TestDataUtil.createAccountRequest();
        createAccountRequest.setEmail("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@testaaaaaaaaaaaaaaaaaaaaaaaaaaaa.com");
        String createAccountJson = objectMapper.writeValueAsString(createAccountRequest);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(createAccountJson))
                .andExpect(
                        MockMvcResultMatchers.status().isBadRequest()
                );
    }

    @Test
    public void testThatCreateAccountReturnsHttp400WhenEmailIsBadFormat() throws Exception {
        CreateAccountRequest createAccountRequest = TestDataUtil.createAccountRequest();
        createAccountRequest.setEmail("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@test.com");
        String createAccountJson = objectMapper.writeValueAsString(createAccountRequest);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(createAccountJson))
                .andExpect(
                        MockMvcResultMatchers.status().isBadRequest()
                );
    }

    @Test
    public void testThatCreateAccountReturnsHttp400WhenPasswordIsBlank() throws Exception {
        CreateAccountRequest createAccountRequest = TestDataUtil.createAccountRequest();
        createAccountRequest.setPassword("");
        String createAccountJson = objectMapper.writeValueAsString(createAccountRequest);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(createAccountJson))
                .andExpect(
                        MockMvcResultMatchers.status().isBadRequest()
                );
    }

    @Test
    public void testThatCreateAccountReturnsHttp400WhenPasswordIsTooShort() throws Exception {
        CreateAccountRequest createAccountRequest = TestDataUtil.createAccountRequest();
        createAccountRequest.setPassword("aaaa");
        String createAccountJson = objectMapper.writeValueAsString(createAccountRequest);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(createAccountJson))
                .andExpect(
                        MockMvcResultMatchers.status().isBadRequest()
                );
    }

    @Test
    public void testThatCreateAccountReturnsHttp400WhenPasswordIsTooLong() throws Exception {
        CreateAccountRequest createAccountRequest = TestDataUtil.createAccountRequest();
        createAccountRequest.setPassword("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        String createAccountJson = objectMapper.writeValueAsString(createAccountRequest);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(createAccountJson))
                .andExpect(
                        MockMvcResultMatchers.status().isBadRequest()
                );
    }

    @Test
    public void testThatCreateAccountReturnsHttp409WhenAccountAlreadyExists() throws Exception {
        CreateAccountRequest createAccountRequest = TestDataUtil.createAccountRequest();

        String createAccountJson =
                objectMapper.writeValueAsString(createAccountRequest);

        when(accountService.register(any(CreateAccountRequest.class)))
                .thenThrow(new AccountAlreadyExistsException("account already exists"));

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createAccountJson)
        ).andExpect(
                MockMvcResultMatchers.status().isConflict()
        );
    }

    @Test
    public void testThatCreateAccountReturnsHttp201WhenAccountIsValid() throws Exception {
        CreateAccountRequest createAccountRequest = TestDataUtil.createAccountRequest();
        String createAccountJson = objectMapper.writeValueAsString(createAccountRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createAccountJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateAccountReturnsTokenWhenAccountIsValid() throws Exception {
        CreateAccountRequest createAccountRequest = TestDataUtil.createAccountRequest();
        String createAccountJson = objectMapper.writeValueAsString(createAccountRequest);

        when(accountService.register(any(CreateAccountRequest.class)))
                .thenReturn(new AuthResponse("jwt-token"));

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createAccountJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.token").isNotEmpty()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.token").isString()
        );
    }
}

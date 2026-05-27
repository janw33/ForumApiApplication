package com.janwypych.ForumApi;

import com.janwypych.ForumApi.dtos.CreateAccountRequest;
import com.janwypych.ForumApi.dtos.LoginRequest;
import com.janwypych.ForumApi.entities.Account;

public final class TestDataUtil {
    public static CreateAccountRequest createAccountRequest() {
        return CreateAccountRequest.builder()
                .username("test")
                .email("test@test.com")
                .password("password123")
                .build();
    }

    public static LoginRequest createLoginRequest() {
        return LoginRequest.builder()
                .email("test@test.com")
                .password("password123")
                .build();
    }
    public static Account createAccount() {
         return Account.builder()
                .id(1L)
                .username("test")
                .email("test@test.com")
                .password("hashedPassword")
                .build();
    }
}

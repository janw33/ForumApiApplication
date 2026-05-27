package com.janwypych.ForumApi;

import com.janwypych.ForumApi.dtos.CreateAccountRequest;

public final class TestDataUtil {
    public static CreateAccountRequest createAccountRequest() {
        return CreateAccountRequest.builder()
                .username("test")
                .email("test@test.com")
                .password("password123")
                .build();
    }
}

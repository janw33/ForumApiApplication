package com.janwypych.ForumApi;

import com.janwypych.ForumApi.dtos.auth.CreateAccountRequest;
import com.janwypych.ForumApi.dtos.post.CreatePostRequest;
import com.janwypych.ForumApi.dtos.post.EditPostRequest;
import com.janwypych.ForumApi.dtos.auth.LoginRequest;
import com.janwypych.ForumApi.entities.Account;
import com.janwypych.ForumApi.entities.Post;

import java.time.LocalDateTime;

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

    public static CreatePostRequest createPostRequest() {
        return CreatePostRequest.builder()
                .title("test")
                .content("testContent")
                .build();
    }

    public static Post createPost(Account author) {
        return Post.builder()
                .id(1L)
                .title("test")
                .content("testContent")
                .author(author)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static EditPostRequest createEditPostRequest() {
        return EditPostRequest.builder()
                .title("test")
                .content("testContent")
                .build();
    }
}

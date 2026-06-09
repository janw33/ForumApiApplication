package com.janwypych.ForumApi;

import com.janwypych.ForumApi.dtos.auth.CreateAccountRequest;
import com.janwypych.ForumApi.dtos.post.CreatePostRequest;
import com.janwypych.ForumApi.dtos.post.EditPostRequest;
import com.janwypych.ForumApi.dtos.auth.LoginRequest;
import com.janwypych.ForumApi.entities.Account;
import com.janwypych.ForumApi.entities.Comment;
import com.janwypych.ForumApi.entities.Post;
import com.janwypych.ForumApi.entities.enums.Role;

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
                 .role(Role.USER)
                .build();
    }
    public static Account createAccount2() {
        return Account.builder()
                .id(2L)
                .username("test1")
                .email("test1@test.com")
                .password("hashedPassword1")
                .role(Role.USER)
                .build();
    }
    public static Account createAccount3() {
        return Account.builder()
                .id(3L)
                .username("test2")
                .email("test2@test.com")
                .password("hashedPassword2")
                .role(Role.USER)
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

    public static Comment createComment(Account author, Post post) {
        return Comment.builder()
                .id(1L)
                .createdAt(LocalDateTime.now())
                .content("test")
                .author(author)
                .post(post)
                .build();
    }
}

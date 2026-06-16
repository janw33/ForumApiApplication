package com.janwypych.ForumApi.controllers.like;

import com.janwypych.ForumApi.TestDataUtil;
import com.janwypych.ForumApi.dtos.like.LikeResponse;
import com.janwypych.ForumApi.entities.Account;
import com.janwypych.ForumApi.exceptions.LikeAlreadyExistsException;
import com.janwypych.ForumApi.exceptions.PostNotFoundException;
import com.janwypych.ForumApi.services.LikeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LikePostControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LikeService likeService;

    private Authentication createAuthentication() {
        Account account = TestDataUtil.createAccount();

        return new UsernamePasswordAuthenticationToken(
                account,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    private RequestPostProcessor authenticatedUser() {
        return authentication(createAuthentication());
    }

    @Test
    public void testThatLikePostReturnsHttp404WhenPostIsNotFound() throws Exception {
        when(likeService.likePost(any(Account.class), anyLong()))
                .thenThrow(new PostNotFoundException("Post not found"));

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/posts/1/likes")
                        .with(authenticatedUser())
        ).andExpect(
                status().isNotFound()
        );
    }

    @Test
    public void testThatLikePostReturnsHttp409WhenUserAlreadyLikedPost() throws Exception {
        when(likeService.likePost(any(Account.class), anyLong())).
                thenThrow(new LikeAlreadyExistsException("Post already liked"));

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/posts/1/likes")
                        .with(authenticatedUser())
        ).andExpect(
                status().isConflict()
        );
    }

    @Test
    public void testThatLikePostReturnsLikeResponse() throws Exception {
        LikeResponse likeResponse = LikeResponse.builder()
                .id(1L)
                .createdAt(LocalDateTime.now())
                .accountId(1L)
                .username("test")
                .build();

        when(likeService.likePost(any(Account.class), anyLong())).
                thenReturn(likeResponse);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/posts/1/likes")
                        .with(authenticatedUser())
        ).andExpect(
                jsonPath("$.id").value(likeResponse.getId())
        ).andExpect(
                jsonPath("$.accountId").value(likeResponse.getAccountId())
        ).andExpect(
                jsonPath("$.username").value(likeResponse.getUsername())
        ).andExpect(
                jsonPath("$.createdAt").isNotEmpty()
        ).andExpect(
                status().isCreated()
        );
    }
}

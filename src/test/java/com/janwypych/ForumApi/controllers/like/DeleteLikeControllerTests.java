package com.janwypych.ForumApi.controllers.like;

import com.janwypych.ForumApi.TestDataUtil;
import com.janwypych.ForumApi.entities.Account;
import com.janwypych.ForumApi.exceptions.LikeNotFoundException;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DeleteLikeControllerTests {
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
    public void testThatDeleteLikeReturnsHttp404WhenPostIsNotFound() throws Exception {
        doThrow(new PostNotFoundException("Post not found"))
                .when(likeService)
                .deleteLike(any(Account.class), anyLong());

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/posts/1/likes")
                        .with(authenticatedUser())
        ).andExpect(
                status().isNotFound()
        );
    }

    @Test
    public void testThatDeleteLikeReturnsHttp404WhenLikeIsNotFound() throws Exception {
        doThrow(new LikeNotFoundException("Like not found"))
                .when(likeService)
                .deleteLike(any(Account.class), anyLong());

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/posts/1/likes")
                        .with(authenticatedUser())
        ).andExpect(
                status().isNotFound()
        );
    }

    @Test
    public void testThatLDeleteLikeReturnsHttp204() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/posts/1/likes")
                        .with(authenticatedUser())
        ).andExpect(
                status().isNoContent()
        );
    }
}

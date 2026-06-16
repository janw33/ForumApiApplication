package com.janwypych.ForumApi.security.like;

import com.janwypych.ForumApi.TestDataUtil;
import com.janwypych.ForumApi.dtos.like.LikeResponse;
import com.janwypych.ForumApi.entities.Account;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LikePostSecurityTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LikeService likeService;

    public Authentication createAuthentication(String role) {
        Account account = TestDataUtil.createAccount();

        return new UsernamePasswordAuthenticationToken(
                account,
                null,
                List.of(new SimpleGrantedAuthority(role))
        );
    }

    private RequestPostProcessor authenticatedUser(String role) {
        return authentication(createAuthentication(role));
    }

    @Test
    public void testThatLikePostReturnsHttp401WhenUserIsUnauthenticated() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/posts/1/likes")
        ).andExpect(
                status().isUnauthorized()
        );
    }
    @Test
    public void testThatLikePostReturnsHttp401WhenTokenIsInvalid() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/posts/1/likes")
                        .header("Authorization", "Bearer invalidtoken")
        ).andExpect(
                status().isUnauthorized()
        );
    }

    @Test
    public void testThatLikePostReturnsHttp201WhenUserIsAuthenticated() throws Exception {
        LikeResponse likeResponse = LikeResponse.builder()
                .id(1L)
                .createdAt(LocalDateTime.now())
                .accountId(1L)
                .username("test")
                .build();

        when(likeService.likePost(any(Account.class) ,anyLong()))
                .thenReturn(likeResponse);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/posts/1/likes")
                        .with(authenticatedUser("ROLE_USER"))
        ).andExpect(
                status().isCreated()
        );
    }

    @Test
    public void testThatLikePostReturnsHttp201WhenAdminIsAuthenticated() throws Exception {
        LikeResponse likeResponse = LikeResponse.builder()
                .id(1L)
                .createdAt(LocalDateTime.now())
                .accountId(1L)
                .username("test")
                .build();

        when(likeService.likePost(any(Account.class) ,anyLong()))
                .thenReturn(likeResponse);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/posts/1/likes")
                        .with(authenticatedUser("ROLE_ADMIN"))
        ).andExpect(
                status().isCreated()
        );
    }
}

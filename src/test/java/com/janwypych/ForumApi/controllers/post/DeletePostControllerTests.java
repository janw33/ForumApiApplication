package com.janwypych.ForumApi.controllers.post;

import com.janwypych.ForumApi.TestDataUtil;
import com.janwypych.ForumApi.entities.Account;
import com.janwypych.ForumApi.exceptions.AccountHasNoPermissionException;
import com.janwypych.ForumApi.exceptions.AccountNotFoundException;
import com.janwypych.ForumApi.exceptions.PostNotFoundException;
import com.janwypych.ForumApi.exceptions.UserNotAuthorException;
import com.janwypych.ForumApi.services.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class DeletePostControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostService postService;

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
    public void  testThatDeletePostReturnsHttp404WhenPostIsNotFound() throws Exception {
        Long postId = 1L;

        doThrow(new PostNotFoundException("Post not found"))
                .when(postService)
                .deletePost(any(Account.class), eq(postId));

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/posts/" + postId.toString())
                        .with(authenticatedUser())
        ).andExpect(
                status().isNotFound()
        );
    }

    @Test
    public void  testThatDeletePostReturnsHttp403WhenUserIsNotAuthorAndNotAdmin() throws Exception {
        Long postId = 1L;

        doThrow(new AccountHasNoPermissionException("Account has no permission"))
                .when(postService)
                .deletePost(any(Account.class), eq(postId));

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/posts/" + postId.toString())
                        .with(authenticatedUser())
        ).andExpect(
                status().isForbidden()
        );
    }

    @Test
    public void  testThatDeletePostReturnsHttp204WhenUserIsAuthorOrAdminAndPostIsValid() throws Exception {
        Account account = TestDataUtil.createAccount();
        Long postId = 1L;

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/posts/" + postId.toString())
                        .with(authenticatedUser())
        ).andExpect(
                status().isNoContent()
        );

        verify(postService).deletePost(any(Account.class), eq(1L));
    }
}
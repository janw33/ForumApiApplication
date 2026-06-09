package com.janwypych.ForumApi.security.post;

import com.janwypych.ForumApi.TestDataUtil;
import com.janwypych.ForumApi.dtos.post.EditPostRequest;
import com.janwypych.ForumApi.dtos.post.PostResponse;
import com.janwypych.ForumApi.entities.Account;
import com.janwypych.ForumApi.services.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EditPostSecurityTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PostService postService;

    private Authentication createAuthentication(String role) {
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
    public void testThatEditPostReturnsHttp401WhenUserIsUnauthenticated() throws Exception {
        EditPostRequest editPostRequest = TestDataUtil.createEditPostRequest();
        String editPostJson = objectMapper.writeValueAsString(editPostRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(editPostJson)
        ).andExpect(
                status().isUnauthorized()
        );
    }

    @Test
    public void testThatEditPostReturnsHttp401WhenUserTokenIsInvalid() throws Exception {
        EditPostRequest editPostRequest = TestDataUtil.createEditPostRequest();
        String editPostJson = objectMapper.writeValueAsString(editPostRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/v1/posts/1")
                        .header("Authorization", "Bearer invalidtoken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(editPostJson)
        ).andExpect(
                status().isUnauthorized()
        );
    }

    @Test
    public void testThatEditPostReturnsHttp403WhenUserIsAdmin() throws Exception {
        EditPostRequest editPostRequest = TestDataUtil.createEditPostRequest();
        String editPostJson = objectMapper.writeValueAsString(editPostRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/v1/posts/1")
                        .with(authenticatedUser("ROLE_ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(editPostJson)
        ).andExpect(
                status().isForbidden()
        );
    }

    @Test
    public void testThatEditPostReturnsHttp200WhenUserIsAuthenticated() throws Exception {
        EditPostRequest editPostRequest = TestDataUtil.createEditPostRequest();
        String editPostJson = objectMapper.writeValueAsString(editPostRequest);

        PostResponse postResponse = PostResponse.builder()
                .id(1L)
                .title(editPostRequest.getTitle())
                .content(editPostRequest.getContent())
                .authorId(1L)
                .createdAt(LocalDateTime.now())
                .authorUsername("test")
                .build();

        when(postService.updatePost(any(Account.class), anyLong(), any(EditPostRequest.class)))
                .thenReturn(postResponse);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/v1/posts/1")
                        .with(authenticatedUser("ROLE_USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(editPostJson)
        ).andExpect(
                status().isOk()
        );
    }
}


package com.janwypych.ForumApi.controllers.post;

import com.janwypych.ForumApi.TestDataUtil;
import com.janwypych.ForumApi.dtos.post.EditPostRequest;
import com.janwypych.ForumApi.dtos.post.PostResponse;
import com.janwypych.ForumApi.entities.Account;
import com.janwypych.ForumApi.exceptions.PostNotFoundException;
import com.janwypych.ForumApi.exceptions.UserNotAuthorException;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EditPostControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
    public void testThatEditPostReturnsHttp400WhenTitleIsTooShort() throws Exception {
        EditPostRequest editPostRequest = TestDataUtil.createEditPostRequest();
        editPostRequest.setTitle("a");
        String editPostJson = objectMapper.writeValueAsString(editPostRequest);
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/v1/posts/1")
                        .with(authenticatedUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(editPostJson)
        ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    public void testThatEditPostReturnsHttp400WhenTitleIsTooLong() throws Exception {
        EditPostRequest editPostRequest = TestDataUtil.createEditPostRequest();
        editPostRequest.setTitle("a".repeat(51));
        String editPostJson = objectMapper.writeValueAsString(editPostRequest);
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/v1/posts/1")
                        .with(authenticatedUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(editPostJson)
        ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    public void testThatEditPostReturnsHttp400WhenContentIsTooShort() throws Exception {
        EditPostRequest editPostRequest = TestDataUtil.createEditPostRequest();
        editPostRequest.setContent("a");
        String editPostJson = objectMapper.writeValueAsString(editPostRequest);
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/v1/posts/1")
                        .with(authenticatedUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(editPostJson)
        ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    public void testThatEditPostReturnsHttp400WhenContentIsTooLong() throws Exception {
        EditPostRequest editPostRequest = TestDataUtil.createEditPostRequest();
        editPostRequest.setContent("a".repeat(5001));
        String editPostJson = objectMapper.writeValueAsString(editPostRequest);
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/v1/posts/1")
                        .with(authenticatedUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(editPostJson)
        ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    public void testThatEditPostReturnsHttp404WhenPostDoesNotExist() throws Exception {
        EditPostRequest editPostRequest = TestDataUtil.createEditPostRequest();
        String editPostJson = objectMapper.writeValueAsString(editPostRequest);

        when(postService.updatePost(any(Account.class), anyLong(), eq(editPostRequest)))
                .thenThrow(new PostNotFoundException("Post not found"));

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/v1/posts/1")
                        .with(authenticatedUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(editPostJson)
        ).andExpect(
                status().isNotFound()
        );
    }

    @Test
    public void testThatEditPostReturnsHttp403WhenUserIsNotAuthor() throws Exception {
        EditPostRequest editPostRequest = TestDataUtil.createEditPostRequest();
        String editPostJson = objectMapper.writeValueAsString(editPostRequest);

        when(postService.updatePost(any(Account.class), anyLong(), eq(editPostRequest)))
                .thenThrow(new UserNotAuthorException("User not author"));

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/v1/posts/1")
                        .with(authenticatedUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(editPostJson)
        ).andExpect(
                status().isForbidden()
        );
    }

    @Test
    public void testThatEditPostReturnsPostResponseWhenAuthorAndPostExists() throws Exception {
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

        when(postService.updatePost(any(Account.class), anyLong(), eq(editPostRequest)))
                .thenReturn(postResponse);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/v1/posts/1")
                        .with(authenticatedUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(editPostJson)
        ).andExpect(
                jsonPath("$.id").value(postResponse.getId())
        ).andExpect(
                jsonPath("$.title").value(editPostRequest.getTitle())
        ).andExpect(
                jsonPath("$.content").value(editPostRequest.getContent())
        ).andExpect(
                jsonPath("$.authorId").value(postResponse.getAuthorId())
        ).andExpect(
                jsonPath("$.authorUsername").value(postResponse.getAuthorUsername())
        ).andExpect(
                status().isOk()
        );
    }
}
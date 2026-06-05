package com.janwypych.ForumApi.security;

import com.janwypych.ForumApi.TestDataUtil;
import com.janwypych.ForumApi.dtos.CreatePostRequest;
import com.janwypych.ForumApi.dtos.EditPostRequest;
import com.janwypych.ForumApi.dtos.PostResponse;
import com.janwypych.ForumApi.entities.Post;
import com.janwypych.ForumApi.services.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
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
    public void testThatEditPostReturnsHttp201WhenUserIsAuthenticated() throws Exception {
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

        when(postService.updatePost(anyLong(), anyLong(), any(EditPostRequest.class)))
                .thenReturn(postResponse);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/v1/posts/1")
                        .with(user("1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(editPostJson)
        ).andExpect(
                status().isOk()
        );
    }
}


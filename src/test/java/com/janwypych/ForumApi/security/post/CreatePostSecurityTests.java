package com.janwypych.ForumApi.security.post;

import com.janwypych.ForumApi.TestDataUtil;
import com.janwypych.ForumApi.dtos.post.CreatePostRequest;
import com.janwypych.ForumApi.dtos.post.PostResponse;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CreatePostSecurityTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PostService postService;

    @Test
    public void testThatCreatePostReturnsHttp401WhenUserIsUnauthenticated() throws Exception {
        CreatePostRequest createPostRequest = TestDataUtil.createPostRequest();
        String createPostJson = objectMapper.writeValueAsString(createPostRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPostJson)
        ).andExpect(
                status().isUnauthorized()
        );
    }

    @Test
    public void testThatCreatePostReturnsHttp401WhenUserTokenIsInvalid() throws Exception {
        CreatePostRequest createPostRequest = TestDataUtil.createPostRequest();
        String createPostJson = objectMapper.writeValueAsString(createPostRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/posts")
                        .header("Authorization", "Bearer invalidtoken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPostJson)
        ).andExpect(
                status().isUnauthorized()
        );
    }

    @Test
    public void testThatCreatePostReturnsHttp201WhenUserIsAuthenticated() throws Exception {
        CreatePostRequest request = TestDataUtil.createPostRequest();

        PostResponse response = PostResponse.builder()
                .id(1L)
                .title(request.getTitle())
                .content(request.getContent())
                .authorId(1L)
                .authorUsername("test")
                .build();

        when(postService.create(anyLong(), any(CreatePostRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/posts")
                        .with(user("1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(
                status().isCreated()
        );
    }
}

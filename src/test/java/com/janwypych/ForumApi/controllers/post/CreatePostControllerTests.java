package com.janwypych.ForumApi.controllers.post;

import com.janwypych.ForumApi.TestDataUtil;
import com.janwypych.ForumApi.dtos.post.CreatePostRequest;
import com.janwypych.ForumApi.dtos.post.PostResponse;
import com.janwypych.ForumApi.entities.Account;
import com.janwypych.ForumApi.exceptions.AccountNotFoundException;
import com.janwypych.ForumApi.services.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
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
public class CreatePostControllerTests {
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
    public void testThatCreatePostReturnsHttp400WhenTitleIsBlank() throws Exception {
        CreatePostRequest createPostRequest = TestDataUtil.createPostRequest();
        createPostRequest.setTitle("");

        String createPostJson = objectMapper.writeValueAsString(createPostRequest);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/posts")
                        .with(authenticatedUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPostJson)
        ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    public void testThatCreatePostReturnsHttp400WhenTitleTooShort() throws Exception {
        CreatePostRequest createPostRequest = TestDataUtil.createPostRequest();
        createPostRequest.setTitle("a");
        String createPostJson = objectMapper.writeValueAsString(createPostRequest);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/posts")
                        .with(authenticatedUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPostJson)
        ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    public void testThatCreatePostReturnsHttp400WhenTitleTooLong() throws Exception {
        CreatePostRequest createPostRequest = TestDataUtil.createPostRequest();
        createPostRequest.setTitle("a".repeat(51));
        String createPostJson = objectMapper.writeValueAsString(createPostRequest);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/posts")
                        .with(authenticatedUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPostJson)
        ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    public void testThatCreatePostReturnsHttp400WhenContentIsBlank() throws Exception {
        CreatePostRequest createPostRequest = TestDataUtil.createPostRequest();
        createPostRequest.setContent("");
        String createPostJson = objectMapper.writeValueAsString(createPostRequest);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/posts")
                        .with(authenticatedUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPostJson)
        ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    public void testThatCreatePostReturnsHttp400WhenContentIsTooShort() throws Exception {
        CreatePostRequest createPostRequest = TestDataUtil.createPostRequest();
        createPostRequest.setContent("a");
        String createPostJson = objectMapper.writeValueAsString(createPostRequest);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/posts")
                        .with(authenticatedUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPostJson)
        ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    public void testThatCreatePostReturnsHttp400WhenContentIsTooLong() throws Exception {
        CreatePostRequest createPostRequest = TestDataUtil.createPostRequest();
        createPostRequest.setContent("a".repeat(5001));
        String createPostJson = objectMapper.writeValueAsString(createPostRequest);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/posts")
                        .with(authenticatedUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPostJson)
        ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    public void testThatCreatePostReturnsPostResponseWhenRequestIsValid() throws Exception {
        CreatePostRequest createPostRequest = TestDataUtil.createPostRequest();
        String createPostJson = objectMapper.writeValueAsString(createPostRequest);

        PostResponse postResponse = PostResponse.builder()
                .id(1L)
                .title(createPostRequest.getTitle())
                .content(createPostRequest.getContent())
                .authorId(1L)
                .createdAt(LocalDateTime.now())
                .authorUsername("test")
                .build();

        when(postService.create(any(Account.class), any(CreatePostRequest.class)))
                .thenReturn(postResponse);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/posts")
                        .with(authenticatedUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPostJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(1L)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(createPostRequest.getTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content").value(createPostRequest.getContent())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.authorId").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.authorUsername").isString()
        ).andExpect(
                status().isCreated());
    }
}
package com.janwypych.ForumApi.controllers.comment;

import com.janwypych.ForumApi.dtos.comment.CommentResponse;
import com.janwypych.ForumApi.dtos.comment.CreateCommentRequest;
import com.janwypych.ForumApi.dtos.post.PostResponse;
import com.janwypych.ForumApi.exceptions.AccountNotFoundException;
import com.janwypych.ForumApi.exceptions.PostNotFoundException;
import com.janwypych.ForumApi.services.CommentService;
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

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CreateCommentControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CommentService commentService;


    @Test
    public void testThatCreateCommentReturnsHttp400WhenContentIsNull() throws Exception {
        CreateCommentRequest createCommentRequest = new CreateCommentRequest(null);
        String createCommentJson = objectMapper.writeValueAsString(createCommentRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/posts/1/comments")
                        .with(user("1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createCommentJson)
        ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    public void testThatCreateCommentReturnsHttp400WhenContentIsBlank() throws Exception {
        CreateCommentRequest createCommentRequest = new CreateCommentRequest("");
        String createCommentJson = objectMapper.writeValueAsString(createCommentRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/posts/1/comments")
                        .with(user("1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createCommentJson)
                ).andExpect(
                        status().isBadRequest()
                );
    }

    @Test
    public void testThatCreateCommentReturnsHttp400WhenContentIsTooShort() throws Exception {
        CreateCommentRequest createCommentRequest = new CreateCommentRequest("a");
        String createCommentJson = objectMapper.writeValueAsString(createCommentRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/posts/1/comments")
                        .with(user("1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createCommentJson)
        ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    public void testThatCreateCommentReturnsHttp400WhenContentIsTooLong() throws Exception {
        CreateCommentRequest createCommentRequest = new CreateCommentRequest("a".repeat(201));
        String createCommentJson = objectMapper.writeValueAsString(createCommentRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/posts/1/comments")
                        .with(user("1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createCommentJson)
        ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    public void testThatCreateCommentReturnsHttp404WhenAccountDoesNotExist() throws Exception {
        CreateCommentRequest createCommentRequest = new CreateCommentRequest("test");
        String createCommentJson = objectMapper.writeValueAsString(createCommentRequest);

        when(commentService.createComment(1L, 1L, createCommentRequest))
                .thenThrow(AccountNotFoundException.class);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/posts/1/comments")
                        .with(user("1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createCommentJson)
        ).andExpect(
                status().isNotFound()
        );
    }

    @Test
    public void testThatCreateCommentReturnsHttp404WhenPostDoesNotExist() throws Exception {
        CreateCommentRequest createCommentRequest = new CreateCommentRequest("test");
        String createCommentJson = objectMapper.writeValueAsString(createCommentRequest);

        when(commentService.createComment(1L, 1L, createCommentRequest))
                .thenThrow(PostNotFoundException.class);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/posts/1/comments")
                        .with(user("1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createCommentJson)
        ).andExpect(
                status().isNotFound()
        );
    }

    @Test
    public void testThatCreateCommentReturnsCommentResponseWhenPostExistsAndAccountExists() throws Exception {
        CreateCommentRequest createCommentRequest = new CreateCommentRequest("test");
        String createCommentJson = objectMapper.writeValueAsString(createCommentRequest);

        CommentResponse commentResponse = CommentResponse.builder()
                .id(1L)
                .content(createCommentRequest.getContent())
                .createdAt(LocalDateTime.now())
                .authorUsername("test")
                .authorId(1L)
                .build();

        when(commentService.createComment(1L, 1L, createCommentRequest))
                .thenReturn(commentResponse);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/posts/1/comments")
                        .with(user("1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createCommentJson)
        ).andExpect(
                jsonPath("$.id").value(commentResponse.getId())
        ).andExpect(
                jsonPath("$.content").value(createCommentRequest.getContent())
        ).andExpect(
                jsonPath("$.createdAt").value(commentResponse.getCreatedAt().toString())
        ).andExpect(
                jsonPath("$.authorId").value(commentResponse.getAuthorId())
        ).andExpect(
                jsonPath("$.authorUsername").value(commentResponse.getAuthorUsername())
        ).andExpect(
                status().isCreated()
        );
    }

}
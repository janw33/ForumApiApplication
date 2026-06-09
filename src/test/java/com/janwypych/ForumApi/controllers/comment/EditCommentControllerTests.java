package com.janwypych.ForumApi.controllers.comment;

import com.janwypych.ForumApi.TestDataUtil;
import com.janwypych.ForumApi.dtos.comment.CommentResponse;
import com.janwypych.ForumApi.dtos.comment.EditCommentRequest;
import com.janwypych.ForumApi.entities.Account;
import com.janwypych.ForumApi.exceptions.CommentNotFoundException;
import com.janwypych.ForumApi.exceptions.PostNotFoundException;
import com.janwypych.ForumApi.exceptions.UserNotAuthorException;
import com.janwypych.ForumApi.services.CommentService;
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
public class EditCommentControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CommentService commentService;

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
    public void testThatEditCommentReturnsHttp400WhenEditCommentRequestIsTooShort() throws Exception {
        EditCommentRequest editCommentRequest = new EditCommentRequest("a");
        String editCommentJson = objectMapper.writeValueAsString(editCommentRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/v1/posts/1/comments/1")
                        .with(authenticatedUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(editCommentJson)
        ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    public void testThatEditCommentReturnsHttp400WhenEditCommentRequestIsTooLong() throws Exception {
        EditCommentRequest editCommentRequest = new EditCommentRequest("a".repeat(201));
        String editCommentJson = objectMapper.writeValueAsString(editCommentRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/v1/posts/1/comments/1")
                        .with(authenticatedUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(editCommentJson)
        ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    public void testThatEditCommentReturnsHttp404WhenPostIsNotFound() throws Exception {
        EditCommentRequest editCommentRequest = new EditCommentRequest("test");
        String editCommentJson = objectMapper.writeValueAsString(editCommentRequest);

        when(commentService.editComment(any(Account.class), anyLong(), anyLong(), eq(editCommentRequest)))
                .thenThrow(new PostNotFoundException("Post not found"));

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/v1/posts/1/comments/1")
                        .with(authenticatedUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(editCommentJson)
        ).andExpect(
                status().isNotFound()
        );
    }

    @Test
    public void testThatEditCommentReturnsHttp404WhenCommentIsNotFound() throws Exception {
        EditCommentRequest editCommentRequest = new EditCommentRequest("test");
        String editCommentJson = objectMapper.writeValueAsString(editCommentRequest);

        when(commentService.editComment(any(Account.class), anyLong(), anyLong(), eq(editCommentRequest)))
                .thenThrow(new CommentNotFoundException("Comment not found"));

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/v1/posts/1/comments/1")
                        .with(authenticatedUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(editCommentJson)
        ).andExpect(
                status().isNotFound()
        );
    }

    @Test
    public void testThatEditCommentReturnsHttp403WhenUserIsNotAuthor() throws Exception {
        EditCommentRequest editCommentRequest = new EditCommentRequest("test");
        String editCommentJson = objectMapper.writeValueAsString(editCommentRequest);

        when(commentService.editComment(any(Account.class), anyLong(), anyLong(), eq(editCommentRequest)))
                .thenThrow(new UserNotAuthorException("User not author"));

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/v1/posts/1/comments/1")
                        .with(authenticatedUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(editCommentJson)
        ).andExpect(
                status().isForbidden()
        );
    }

    @Test
    public void testThatEditCommentReturnsCommentResponse() throws Exception {
        EditCommentRequest editCommentRequest = new EditCommentRequest("test");
        String editCommentJson = objectMapper.writeValueAsString(editCommentRequest);

        CommentResponse commentResponse = CommentResponse.builder()
                .id(1L)
                .content(editCommentRequest.getContent())
                .createdAt(LocalDateTime.now())
                .authorUsername("test")
                .authorId(1L)
                .build();

        when(commentService.editComment(any(Account.class), anyLong(), anyLong(), eq(editCommentRequest)))
                .thenReturn(commentResponse);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/v1/posts/1/comments/1")
                        .with(authenticatedUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(editCommentJson)
        ).andExpect(
                jsonPath("$.id").value(commentResponse.getId())
        ).andExpect(
                jsonPath("$.content").value(editCommentRequest.getContent())
        ).andExpect(
                jsonPath("$.authorId").value(commentResponse.getAuthorId())
        ).andExpect(
                jsonPath("$.authorUsername").value(commentResponse.getAuthorUsername())
        ).andExpect(
                status().isOk()
        );
    }
}

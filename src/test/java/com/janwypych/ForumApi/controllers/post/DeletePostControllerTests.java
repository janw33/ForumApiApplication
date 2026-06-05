package com.janwypych.ForumApi.controllers.post;

import com.janwypych.ForumApi.exceptions.AccountNotFoundException;
import com.janwypych.ForumApi.exceptions.PostNotFoundException;
import com.janwypych.ForumApi.exceptions.UserNotAuthorException;
import com.janwypych.ForumApi.services.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class DeletePostControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostService postService;

    @Test
    public void  testThatDeletePostReturnsHttp404WhenPostIsNotFound() throws Exception {
        Long userId = 1L;
        Long postId = 1L;

        doThrow(new PostNotFoundException("Post not found"))
                .when(postService)
                .deletePost(userId, postId);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/posts/" + postId.toString())
                        .with(user("1"))
        ).andExpect(
                status().isNotFound()
        );
    }

    @Test
    public void  testThatDeletePostReturnsHttp403WhenUserIsNotAuthor() throws Exception {
        Long userId = 1L;
        Long postId = 1L;

        doThrow(new UserNotAuthorException("User not author"))
                .when(postService)
                .deletePost(userId, postId);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/posts/" + postId.toString())
                        .with(user("1"))
        ).andExpect(
                status().isForbidden()
        );
    }

    @Test
    public void  testThatDeletePostReturnsHttp204WhenUserIsAuthorAndPostIsValid() throws Exception {
        Long postId = 1L;

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/posts/" + postId.toString())
                        .with(user("1"))
        ).andExpect(
                status().isNoContent()
        );

        verify(postService).deletePost(1L, 1L);
    }
}
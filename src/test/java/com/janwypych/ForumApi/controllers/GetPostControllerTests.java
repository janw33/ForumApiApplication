package com.janwypych.ForumApi.controllers;

import com.janwypych.ForumApi.dtos.PostResponse;
import com.janwypych.ForumApi.exceptions.PostNotFoundException;
import com.janwypych.ForumApi.services.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GetPostControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostService postService;

    @Test
    public void testThatGetPostReturnsHttp400WhenIdIsBadFormat() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/posts/abc")
        ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    public void testThatGetPostReturnsHttp404WhenPostDoesntExist() throws Exception {
        when(postService.getPost(1L))
                .thenThrow(new PostNotFoundException("Post not found"));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/posts/1")
        ).andExpect(
                status().isNotFound()
        );
    }

    @Test
    public void testThatGetPostReturnsHttp200WhenPostExists() throws Exception {
        when(postService.getPost(1L))
                .thenReturn(new PostResponse());

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/posts/1")
        ).andExpect(
                status().isOk()
        );
    }
}

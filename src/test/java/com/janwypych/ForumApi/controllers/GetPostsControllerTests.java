package com.janwypych.ForumApi.controllers;

import com.janwypych.ForumApi.dtos.PostResponse;
import com.janwypych.ForumApi.services.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import org.springframework.data.domain.Pageable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GetPostsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostService postService;

    @Test
    public void testThatGetPostReturnsHttp200WhenPostExists() throws Exception {
        when(postService.getPosts(any(Pageable.class)))
                .thenReturn(Page.empty());

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/posts")
        ).andExpect(
                status().isOk()
        );
    }
}
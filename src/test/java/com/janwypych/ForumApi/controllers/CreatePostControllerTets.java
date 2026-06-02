package com.janwypych.ForumApi.controllers;

import com.janwypych.ForumApi.TestDataUtil;
import com.janwypych.ForumApi.dtos.CreatePostRequest;
import com.janwypych.ForumApi.services.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.web.servlet.function.RequestPredicates.contentType;

@SpringBootTest
@AutoConfigureMockMvc
public class CreatePostControllerTets {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PostService postService;

    @Test
    public void testThatCreateAccountReturnsHttp400WhenTitleIsBlank() throws Exception {
        CreatePostRequest createPostRequest = TestDataUtil.createPostRequest();
        createPostRequest.setTitle("");
        String createPostJson = objectMapper.writeValueAsString(createPostRequest);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPostJson)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }
}
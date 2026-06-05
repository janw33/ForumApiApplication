package com.janwypych.ForumApi.controllers.comment;

import com.janwypych.ForumApi.exceptions.PostNotFoundException;
import com.janwypych.ForumApi.services.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GetCommentsControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CommentService commentService;

    @Test
    public void testThatGetCommentsReturnsHttp404WhenPostIsNotFound() throws Exception {
        when(commentService.getComments(anyLong(), any(Pageable.class)))
                .thenThrow(new PostNotFoundException("Post not found"));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/posts/1/comments")
        ).andExpect(
                status().isNotFound()
        );
    }

    @Test
    public void testThatGetCommentsReturnsHttp200WhenPostExists() throws Exception {
        when(commentService.getComments(anyLong(), any(Pageable.class)))
                .thenReturn(Page.empty());

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/posts/1/comments")
        ).andExpect(
                status().isOk()
        );
    }
}

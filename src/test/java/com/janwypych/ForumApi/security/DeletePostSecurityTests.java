package com.janwypych.ForumApi.security;

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

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DeletePostSecurityTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PostService postService;

    @Test
    public void testThatDeletePostReturnsHttp401WhenUserIsUnauthenticated() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/posts/1")
        ).andExpect(
                status().isUnauthorized()
        );
    }

    @Test
    public void testThatDeletePostReturnsHttp401WhenTokenIsInvalid() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/posts/1")
                        .header("Authorization", "Bearer invalidtoken")
        ).andExpect(
                status().isUnauthorized()
        );
    }

    @Test
    public void testThatDeletePostReturnsHttp204WhenUserIsAuthenticated() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/posts/1")
                        .with(user("1"))
        ).andExpect(
                status().isNoContent()
        );
    }
}

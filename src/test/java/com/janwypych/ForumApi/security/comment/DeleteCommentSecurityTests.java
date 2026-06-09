//package com.janwypych.ForumApi.security.comment;
//
//import com.janwypych.ForumApi.exceptions.AccountNotFoundException;
//import com.janwypych.ForumApi.services.CommentService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.doThrow;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class DeleteCommentSecurityTests {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private CommentService commentService;
//
//    @Test
//    public void testThatDeleteCommentReturnsHttp401WhenUserIsUnauthenticated() throws Exception {
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.delete("/api/v1/posts/1/comments/1")
//        ).andExpect(
//                status().isUnauthorized()
//        );
//    }
//
//    @Test
//    public void testThatDeleteCommentReturnsHttp401WhenTokenIsInvalid() throws Exception {
//        mockMvc.perform(
//                MockMvcRequestBuilders.delete("/api/v1/posts/1/comments/1")
//                        .header("Authorization", "Bearer invalidtoken")
//        ).andExpect(
//                status().isUnauthorized()
//        );
//    }
//
//    @Test
//    public void testThatDeleteCommentReturnsHttp204WhenUserIsAuthenticated() throws Exception {
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.delete("/api/v1/posts/1/comments/1")
//                        .with(user("1"))
//        ).andExpect(
//                status().isNoContent()
//        );
//    }
//}
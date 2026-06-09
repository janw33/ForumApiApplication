//package com.janwypych.ForumApi.controllers.comment;
//
//import com.janwypych.ForumApi.exceptions.AccountNotFoundException;
//import com.janwypych.ForumApi.exceptions.CommentNotFoundException;
//import com.janwypych.ForumApi.exceptions.PostNotFoundException;
//import com.janwypych.ForumApi.exceptions.UserNotAuthorException;
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
//public class DeleteCommentControllerTests {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private CommentService commentService;
//
//    @Test
//    public void testThatDeleteCommentReturnsHttp404WhenAccountIsNotFound() throws Exception {
//        doThrow(new AccountNotFoundException("Account not fount"))
//                .when(commentService)
//                .deleteComment(anyLong(), anyLong(), anyLong());
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.delete("/api/v1/posts/1/comments/1")
//                        .with(user("1"))
//        ).andExpect(
//                status().isNotFound()
//        );
//    }
//
//    @Test
//    public void testThatDeleteCommentReturnsHttp404WhenPostIsNotFound() throws Exception {
//        doThrow(new PostNotFoundException("Post not fount"))
//                .when(commentService)
//                .deleteComment(anyLong(), anyLong(), anyLong());
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.delete("/api/v1/posts/1/comments/1")
//                        .with(user("1"))
//        ).andExpect(
//                status().isNotFound()
//        );
//    }
//
//    @Test
//    public void testThatDeleteCommentReturnsHttp404WhenCommentIsNotFound() throws Exception {
//        doThrow(new CommentNotFoundException("Comment not fount"))
//                .when(commentService)
//                .deleteComment(anyLong(), anyLong(), anyLong());
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.delete("/api/v1/posts/1/comments/1")
//                        .with(user("1"))
//        ).andExpect(
//                status().isNotFound()
//        );
//    }
//
//
//    @Test
//    public void testThatDeleteCommentReturnsHttp403WhenUserIsNotAuthor() throws Exception {
//        doThrow(new UserNotAuthorException("Comment not fount"))
//                .when(commentService)
//                .deleteComment(anyLong(), anyLong(), anyLong());
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.delete("/api/v1/posts/1/comments/1")
//                        .with(user("1"))
//        ).andExpect(
//                status().isForbidden()
//        );
//    }
//
//    @Test
//    public void testThatDeleteCommentReturnsHttp204() throws Exception {
//        mockMvc.perform(
//                MockMvcRequestBuilders.delete("/api/v1/posts/1/comments/1")
//                        .with(user("1"))
//        ).andExpect(
//                status().isNoContent()
//        );
//    }
//}
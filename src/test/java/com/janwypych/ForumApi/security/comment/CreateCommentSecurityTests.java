//package com.janwypych.ForumApi.security.comment;
//
//import com.janwypych.ForumApi.TestDataUtil;
//import com.janwypych.ForumApi.dtos.comment.CommentResponse;
//import com.janwypych.ForumApi.dtos.comment.CreateCommentRequest;
//import com.janwypych.ForumApi.dtos.post.CreatePostRequest;
//import com.janwypych.ForumApi.entities.Account;
//import com.janwypych.ForumApi.services.CommentService;
//import com.janwypych.ForumApi.services.PostService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.request.RequestPostProcessor;
//import tools.jackson.databind.ObjectMapper;
//
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.when;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class CreateCommentSecurityTests {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockitoBean
//    private CommentService commentService;
//
//    public Authentication createAuthentication() {
//        Account account = TestDataUtil.createAccount();
//
//        return new UsernamePasswordAuthenticationToken(
//                account,
//                null,
//                List.of(new SimpleGrantedAuthority("ROLE_USER"))
//        );
//    }
//
//    private RequestPostProcessor authenticatedUser() {
//        return authentication(createAuthentication());
//    }
//
//    @Test
//    public void testThatCreateCommentReturnsHttp401WhenUserIsUnauthenticated() throws Exception {
//        CreateCommentRequest createCommentRequest = new CreateCommentRequest("test");
//        String createCommentJson = objectMapper.writeValueAsString(createCommentRequest);
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.post("/api/v1/posts/1/comments")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(createCommentJson)
//        ).andExpect(
//                status().isUnauthorized()
//        );
//    }
//
//    @Test
//    public void testThatCreateCommentReturnsHttp401WhenTokenIsInvalid() throws Exception {
//        CreateCommentRequest createCommentRequest = new CreateCommentRequest("test");
//        String createCommentJson = objectMapper.writeValueAsString(createCommentRequest);
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.post("/api/v1/posts/1/comments")
//                        .header("Authorization", "Bearer invalidtoken")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(createCommentJson)
//        ).andExpect(
//                status().isUnauthorized()
//        );
//    }
//
//    @Test
//    public void testThatCreateCommentReturnsHttp201WhenUserIsAuthenticated() throws Exception {
//        CreateCommentRequest createCommentRequest = new CreateCommentRequest("test");
//        String createCommentJson = objectMapper.writeValueAsString(createCommentRequest);
//
//        CommentResponse commentResponse = CommentResponse.builder()
//                .id(1L)
//                .content(createCommentRequest.getContent())
//                .authorId(1L)
//                .authorUsername("test")
//                .build();
//
//        when(commentService.createComment(anyLong(), anyLong(), any(CreateCommentRequest.class)))
//                .thenReturn(commentResponse);
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.post("/api/v1/posts/1/comments")
//                        .with(authenticatedUser())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(createCommentJson)
//        ).andExpect(
//                status().isCreated()
//        );
//    }
//}
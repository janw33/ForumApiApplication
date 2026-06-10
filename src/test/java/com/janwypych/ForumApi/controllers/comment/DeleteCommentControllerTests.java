package com.janwypych.ForumApi.controllers.comment;

import com.janwypych.ForumApi.TestDataUtil;
import com.janwypych.ForumApi.entities.Account;
import com.janwypych.ForumApi.exceptions.AccountHasNoPermissionException;
import com.janwypych.ForumApi.exceptions.CommentNotFoundException;
import com.janwypych.ForumApi.exceptions.PostNotFoundException;
import com.janwypych.ForumApi.exceptions.UserNotAuthorException;
import com.janwypych.ForumApi.services.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DeleteCommentControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CommentService commentService;

    private Authentication createAuthentication( ) {
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
    public void testThatDeleteCommentReturnsHttp404WhenPostIsNotFound() throws Exception {
        doThrow(new PostNotFoundException("Post not fount"))
                .when(commentService)
                .deleteComment(any(Account.class), anyLong(), anyLong());

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/posts/1/comments/1")
                        .with(authenticatedUser())
        ).andExpect(
                status().isNotFound()
        );
    }

    @Test
    public void testThatDeleteCommentReturnsHttp404WhenCommentIsNotFound() throws Exception {
        doThrow(new CommentNotFoundException("Comment not fount"))
                .when(commentService)
                .deleteComment(any(Account.class), anyLong(), anyLong());

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/posts/1/comments/1")
                        .with(authenticatedUser())
        ).andExpect(
                status().isNotFound()
        );
    }


    @Test
    public void testThatDeleteCommentReturnsHttp403WhenUserHasNoPermission() throws Exception {
        doThrow(new AccountHasNoPermissionException("Account has no permission"))
                .when(commentService)
                .deleteComment(any(Account.class), anyLong(), anyLong());

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/posts/1/comments/1")
                        .with(authenticatedUser())
        ).andExpect(
                status().isForbidden()
        );
    }

    @Test
    public void testThatDeleteCommentReturnsHttp204() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/posts/1/comments/1")
                        .with(authenticatedUser())
        ).andExpect(
                status().isNoContent()
        );
    }
}
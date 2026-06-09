package com.janwypych.ForumApi.services.comment;

import com.janwypych.ForumApi.TestDataUtil;
import com.janwypych.ForumApi.entities.Account;
import com.janwypych.ForumApi.entities.Comment;
import com.janwypych.ForumApi.entities.Post;
import com.janwypych.ForumApi.entities.enums.Role;
import com.janwypych.ForumApi.exceptions.*;
import com.janwypych.ForumApi.repositories.CommentRepository;
import com.janwypych.ForumApi.repositories.PostRepository;
import com.janwypych.ForumApi.services.CommentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteCommentServiceTests {
    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;


    @Test
    public void testThatDeleteCommentThrowsPostNotFoundExceptionWhenPostDoesntExist() {
        Account account = TestDataUtil.createAccount();
        Long postId = 1L;
        Long commentId = 1L;

        when(postRepository.findById(postId))
                .thenReturn(Optional.empty());

        assertThrows(
                PostNotFoundException.class,
                () -> commentService.deleteComment(account, postId, commentId)
        );

        verify(commentRepository, never()).deleteById(any());
    }

    @Test
    public void testThatDeleteCommentThrowsCommentNotFoundExceptionWhenCommentDoesntExist() {
        Account account = TestDataUtil.createAccount();
        Long postId = 1L;
        Long commentId = 1L;

        Post post = TestDataUtil.createPost(account);

        when(postRepository.findById(postId))
                .thenReturn(Optional.of(post));

        when(commentRepository.findById(commentId))
                .thenReturn(Optional.empty());

        assertThrows(
                CommentNotFoundException.class,
                () -> commentService.deleteComment(account, postId, commentId)
        );

        verify(commentRepository, never()).deleteById(any());
    }

    @Test
    public void testThatDeleteCommentThrowsCommentNotFoundExceptionWhenCommentIsNotInPost() {
        Account account = TestDataUtil.createAccount();
        Long postId = 1L;
        Long commentId = 1L;

        Post post = TestDataUtil.createPost(account);
        post.setId(2L);
        Comment comment = TestDataUtil.createComment(account, post);

        when(postRepository.findById(postId))
                .thenReturn(Optional.of(post));

        when(commentRepository.findById(commentId))
                .thenReturn(Optional.of(comment));

        assertThrows(
                CommentNotFoundException.class,
                () -> commentService.deleteComment(account, postId, commentId)
        );

        verify(commentRepository, never()).deleteById(any());
    }

    @Test
    public void testThatDeleteCommentThrowsAccountHasNoPermissionExceptionWhenUserIsNotAuthorOfCommentAndPostAndIsNotAdmin() {
        Account currentUser = TestDataUtil.createAccount();
        Long postId = 1L;
        Long commentId = 1L;

        currentUser.setId(1L);

        Account commentAuthor = TestDataUtil.createAccount2();

        Account postAuthor = TestDataUtil.createAccount3();

        Post post = TestDataUtil.createPost(postAuthor);

        Comment comment = TestDataUtil.createComment(commentAuthor, post);

        when(postRepository.findById(postId))
                .thenReturn(Optional.of(post));

        when(commentRepository.findById(commentId))
                .thenReturn(Optional.of(comment));

        assertThrows(
                AccountHasNoPermissionException.class,
                () -> commentService.deleteComment(currentUser, postId, commentId)
        );

        verify(commentRepository, never()).deleteById(any());
    }

    @Test
    public void testThatDeleteCommentDeletesCommentWhenUserIsAuthorOfComment() {
        Account currentUser = TestDataUtil.createAccount();
        Long postId = 1L;
        Long commentId = 1L;

        Account postAuthor = TestDataUtil.createAccount2();
        Post post = TestDataUtil.createPost(postAuthor);
        Comment comment = TestDataUtil.createComment(currentUser, post);

        when(postRepository.findById(postId))
                .thenReturn(Optional.of(post));

        when(commentRepository.findById(commentId))
                .thenReturn(Optional.of(comment));

        commentService.deleteComment(currentUser, postId, commentId);

        verify(commentRepository).delete(comment);
    }

    @Test
    public void testThatDeleteCommentDeletesCommentWhenUserIsAuthorOfPost() {
        Account currentUser = TestDataUtil.createAccount();
        Long postId = 1L;
        Long commentId = 1L;

        Account commentAuthor = TestDataUtil.createAccount2();
        Post post = TestDataUtil.createPost(currentUser);
        Comment comment = TestDataUtil.createComment(commentAuthor, post);

        when(postRepository.findById(postId))
                .thenReturn(Optional.of(post));

        when(commentRepository.findById(commentId))
                .thenReturn(Optional.of(comment));

        commentService.deleteComment(currentUser, postId, commentId);

        verify(commentRepository).delete(comment);
    }

    @Test
    public void testThatDeleteCommentDeletesCommentWhenUserIsAdmin() {
        Account currentUser = TestDataUtil.createAccount();
        currentUser.setRole(Role.ADMIN);
        Long postId = 1L;
        Long commentId = 1L;

        Account postAuthor = TestDataUtil.createAccount2();
        Account commentAuthor = TestDataUtil.createAccount3();

        Post post = TestDataUtil.createPost(postAuthor);
        Comment comment = TestDataUtil.createComment(commentAuthor, post);

        when(postRepository.findById(postId))
                .thenReturn(Optional.of(post));

        when(commentRepository.findById(commentId))
                .thenReturn(Optional.of(comment));

        commentService.deleteComment(currentUser, postId, commentId);

        verify(commentRepository).delete(comment);
    }
}
package com.janwypych.ForumApi.services.comment;

import com.janwypych.ForumApi.TestDataUtil;
import com.janwypych.ForumApi.entities.Account;
import com.janwypych.ForumApi.entities.Comment;
import com.janwypych.ForumApi.entities.Post;
import com.janwypych.ForumApi.exceptions.AccountNotFoundException;
import com.janwypych.ForumApi.exceptions.CommentNotFoundException;
import com.janwypych.ForumApi.exceptions.PostNotFoundException;
import com.janwypych.ForumApi.exceptions.UserNotAuthorException;
import com.janwypych.ForumApi.mappers.CommentMapper;
import com.janwypych.ForumApi.repositories.AccountRepository;
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
    private CommentMapper commentMapper;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    @Test
    public void testThatDeleteCommentThrowsAccountNotFoundExceptionWhenAccountDoesntExist() {
        Long userId = 1L;
        Long postId = 1L;
        Long commentId = 1L;

        when(accountRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(
                AccountNotFoundException.class,
                () -> commentService.deleteComment(userId, postId, commentId)
        );

        verify(commentRepository, never()).deleteById(any());
    }

    @Test
    public void testThatDeleteCommentThrowsPostNotFoundExceptionWhenPostDoesntExist() {
        Long userId = 1L;
        Long postId = 1L;
        Long commentId = 1L;

        Account author = TestDataUtil.createAccount();

        when(accountRepository.findById(userId))
                .thenReturn(Optional.of(author));

        when(postRepository.findById(postId))
                .thenReturn(Optional.empty());

        assertThrows(
                PostNotFoundException.class,
                () -> commentService.deleteComment(userId, postId, commentId)
        );

        verify(commentRepository, never()).deleteById(any());
    }

    @Test
    public void testThatDeleteCommentThrowsCommentNotFoundExceptionWhenCommentDoesntExist() {
        Long userId = 1L;
        Long postId = 1L;
        Long commentId = 1L;

        Account author = TestDataUtil.createAccount();
        Post post = TestDataUtil.createPost(author);

        when(accountRepository.findById(userId))
                .thenReturn(Optional.of(author));

        when(postRepository.findById(postId))
                .thenReturn(Optional.of(post));

        when(commentRepository.findById(commentId))
                .thenReturn(Optional.empty());

        assertThrows(
                CommentNotFoundException.class,
                () -> commentService.deleteComment(userId, postId, commentId)
        );

        verify(commentRepository, never()).deleteById(any());
    }

    @Test
    public void testThatDeleteCommentThrowsCommentNotFoundExceptionWhenCommentIsNotInPost() {
        Long userId = 1L;
        Long postId = 1L;
        Long commentId = 1L;

        Account author = TestDataUtil.createAccount();
        Post post = TestDataUtil.createPost(author);
        post.setId(2L);
        Comment comment = TestDataUtil.createComment(author, post);

        when(accountRepository.findById(userId))
                .thenReturn(Optional.of(author));

        when(postRepository.findById(postId))
                .thenReturn(Optional.of(post));

        when(commentRepository.findById(commentId))
                .thenReturn(Optional.of(comment));

        assertThrows(
                CommentNotFoundException.class,
                () -> commentService.deleteComment(userId, postId, commentId)
        );

        verify(commentRepository, never()).deleteById(any());
    }

    @Test
    public void testThatDeleteCommentThrowsUserNotAuthorExceptionWhenUserIsNotAuthorOfCommentAndPost() {
        Long userId = 1L;
        Long postId = 1L;
        Long commentId = 1L;

        Account currentUser = TestDataUtil.createAccount();
        currentUser.setId(1L);

        Account commentAuthor = TestDataUtil.createAccount2();
        commentAuthor.setId(10L);

        Account postAuthor = TestDataUtil.createAccount3();
        postAuthor.setId(20L);

        Post post = TestDataUtil.createPost(postAuthor);

        Comment comment = TestDataUtil.createComment(commentAuthor, post);

        when(accountRepository.findById(userId))
                .thenReturn(Optional.of(currentUser));

        when(postRepository.findById(postId))
                .thenReturn(Optional.of(post));

        when(commentRepository.findById(commentId))
                .thenReturn(Optional.of(comment));

        assertThrows(
                UserNotAuthorException.class,
                () -> commentService.deleteComment(userId, postId, commentId)
        );

        verify(commentRepository, never()).deleteById(any());
    }

    @Test
    public void testThatDeleteCommentDeletesCommentWhenUserIsAuthorOfComment() {
        Long userId = 1L;
        Long postId = 1L;
        Long commentId = 1L;

        Account commentAuthor = TestDataUtil.createAccount();
        Account postAuthor = TestDataUtil.createAccount2();

        Post post = TestDataUtil.createPost(postAuthor);
        Comment comment = TestDataUtil.createComment(commentAuthor, post);

        when(accountRepository.findById(userId))
                .thenReturn(Optional.of(commentAuthor));

        when(postRepository.findById(postId))
                .thenReturn(Optional.of(post));

        when(commentRepository.findById(commentId))
                .thenReturn(Optional.of(comment));

        commentService.deleteComment(userId, postId, commentId);

        verify(commentRepository).deleteById(commentId);
    }

    @Test
    public void testThatDeleteCommentDeletesCommentWhenUserIsAuthorOfPost() {
        Long userId = 2L;
        Long postId = 1L;
        Long commentId = 1L;

        Account commentAuthor = TestDataUtil.createAccount();
        Account postAuthor = TestDataUtil.createAccount2();

        Post post = TestDataUtil.createPost(postAuthor);
        Comment comment = TestDataUtil.createComment(commentAuthor, post);

        when(accountRepository.findById(userId))
                .thenReturn(Optional.of(postAuthor));

        when(postRepository.findById(postId))
                .thenReturn(Optional.of(post));

        when(commentRepository.findById(commentId))
                .thenReturn(Optional.of(comment));

        commentService.deleteComment(userId, postId, commentId);

        verify(commentRepository).deleteById(commentId);
    }
}
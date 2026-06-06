package com.janwypych.ForumApi.services.comment;

import com.janwypych.ForumApi.TestDataUtil;
import com.janwypych.ForumApi.dtos.comment.CommentResponse;
import com.janwypych.ForumApi.dtos.comment.EditCommentRequest;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EditCommentServiceTests {
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
    public void testThatEditCommentThrowsAccountNotFoundExceptionWhenAccountIDoesntExist() {
        Long userId = 1L;
        Long postId = 1L;
        Long commentId = 1L;
        EditCommentRequest editCommentRequest = new EditCommentRequest("test");

        when(accountRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(
                AccountNotFoundException.class,
                () -> commentService.editComment(userId, postId, commentId, editCommentRequest)
        );

        verify(commentRepository, never()).save(any());
    }

    @Test
    public void testThatEditCommentThrowsPostNotFoundExceptionWhenPostIsDoesntExist() {
        Long userId = 1L;
        Long postId = 1L;
        Long commentId = 1L;
        EditCommentRequest editCommentRequest = new EditCommentRequest("test");

        Account author = TestDataUtil.createAccount();

        when(accountRepository.findById(userId))
                .thenReturn(Optional.of(author));

        when(postRepository.findById(postId))
                .thenReturn(Optional.empty());

        assertThrows(
                PostNotFoundException.class,
                () -> commentService.editComment(userId, postId, commentId, editCommentRequest)
        );

        verify(commentRepository, never()).save(any());
    }

    @Test
    public void testThatEditCommentThrowsCommentNotFoundExceptionWhenCommentDoesntExist() {
        Long userId = 1L;
        Long postId = 1L;
        Long commentId = 1L;
        EditCommentRequest editCommentRequest = new EditCommentRequest("test");

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
                () -> commentService.editComment(userId, postId, commentId, editCommentRequest)
        );

        verify(commentRepository, never()).save(any());
    }

    @Test
    public void testThatEditCommentThrowsCommentNotFoundExceptionWhenCommentIsNotInPost() {
        Long userId = 1L;
        Long postId = 1L;
        Long commentId = 1L;
        EditCommentRequest editCommentRequest = new EditCommentRequest("test");

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
                () -> commentService.editComment(userId, postId, commentId, editCommentRequest)
        );

        verify(commentRepository, never()).save(any());
    }

    @Test
    public void testThatEditCommentThrowsUserNotAuthorExceptionWhenUserIsNotAuthor() {
        Long userId = 1L;
        Long postId = 1L;
        Long commentId = 1L;
        EditCommentRequest editCommentRequest = new EditCommentRequest("test");

        Account author = TestDataUtil.createAccount();
        author.setId(2L);
        Post post = TestDataUtil.createPost(author);
        Comment comment = TestDataUtil.createComment(author, post);

        when(accountRepository.findById(userId))
                .thenReturn(Optional.of(author));

        when(postRepository.findById(postId))
                .thenReturn(Optional.of(post));

        when(commentRepository.findById(commentId))
                .thenReturn(Optional.of(comment));

        assertThrows(
                UserNotAuthorException.class,
                () -> commentService.editComment(userId, postId, commentId, editCommentRequest)
        );

        verify(commentRepository, never()).save(any());
    }

    @Test
    public void testThatEditCommentReturnsCommentResponse() {
        Long userId = 1L;
        Long postId = 1L;
        Long commentId = 1L;
        EditCommentRequest editCommentRequest = new EditCommentRequest("test");

        Account author = TestDataUtil.createAccount();
        Post post = TestDataUtil.createPost(author);
        Comment comment = TestDataUtil.createComment(author, post);
        CommentResponse commentResponse = CommentResponse.builder()
                .id(comment.getId())
                .createdAt(comment.getCreatedAt())
                .authorId(comment.getAuthor().getId())
                .authorUsername(comment.getAuthor().getUsername())
                .content(editCommentRequest.getContent())
                .build();

        when(accountRepository.findById(userId))
                .thenReturn(Optional.of(author));

        when(postRepository.findById(postId))
                .thenReturn(Optional.of(post));

        when(commentRepository.findById(commentId))
                .thenReturn(Optional.of(comment));

        comment.setContent(editCommentRequest.getContent());

        when(commentRepository.save(comment))
                .thenReturn(comment);

        when(commentMapper.mapFromCommentToCommentResponse(comment))
                .thenReturn(commentResponse);

        CommentResponse result = commentService.editComment(userId, postId, commentId, editCommentRequest);

        assertEquals(commentResponse, result);

        verify(commentRepository).save(comment);
    }
}
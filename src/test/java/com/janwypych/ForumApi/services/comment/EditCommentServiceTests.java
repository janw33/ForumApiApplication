package com.janwypych.ForumApi.services.comment;

import com.janwypych.ForumApi.TestDataUtil;
import com.janwypych.ForumApi.dtos.comment.CommentResponse;
import com.janwypych.ForumApi.dtos.comment.EditCommentRequest;
import com.janwypych.ForumApi.entities.Account;
import com.janwypych.ForumApi.entities.Comment;
import com.janwypych.ForumApi.entities.Post;
import com.janwypych.ForumApi.exceptions.CommentNotFoundException;
import com.janwypych.ForumApi.exceptions.PostNotFoundException;
import com.janwypych.ForumApi.exceptions.UserNotAuthorException;
import com.janwypych.ForumApi.mappers.CommentMapper;
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
    private PostRepository postRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;


    @Test
    public void testThatEditCommentThrowsPostNotFoundExceptionWhenPostIsDoesntExist() {
        Account account = TestDataUtil.createAccount();
        Long postId = 1L;
        Long commentId = 1L;
        EditCommentRequest editCommentRequest = new EditCommentRequest("test");

        when(postRepository.findById(postId))
                .thenReturn(Optional.empty());

        assertThrows(
                PostNotFoundException.class,
                () -> commentService.editComment(account, postId, commentId, editCommentRequest)
        );

        verify(commentRepository, never()).save(any());
    }

    @Test
    public void testThatEditCommentThrowsCommentNotFoundExceptionWhenCommentDoesntExist() {
        Account account = TestDataUtil.createAccount();
        Long postId = 1L;
        Long commentId = 1L;
        EditCommentRequest editCommentRequest = new EditCommentRequest("test");
        ;
        Post post = TestDataUtil.createPost(account);

        when(postRepository.findById(postId))
                .thenReturn(Optional.of(post));

        when(commentRepository.findById(commentId))
                .thenReturn(Optional.empty());

        assertThrows(
                CommentNotFoundException.class,
                () -> commentService.editComment(account, postId, commentId, editCommentRequest)
        );

        verify(commentRepository, never()).save(any());
    }

    @Test
    public void testThatEditCommentThrowsCommentNotFoundExceptionWhenCommentIsNotInPost() {
        Account account = TestDataUtil.createAccount();
        Long postId = 1L;
        Long commentId = 1L;
        EditCommentRequest editCommentRequest = new EditCommentRequest("test");

        Post post = TestDataUtil.createPost(account);
        post.setId(2L);
        Comment comment = TestDataUtil.createComment(account, post);

        when(postRepository.findById(postId))
                .thenReturn(Optional.of(post));

        when(commentRepository.findById(commentId))
                .thenReturn(Optional.of(comment));

        assertThrows(
                CommentNotFoundException.class,
                () -> commentService.editComment(account, postId, commentId, editCommentRequest)
        );

        verify(commentRepository, never()).save(any());
    }

    @Test
    public void testThatEditCommentThrowsUserNotAuthorExceptionWhenUserIsNotAuthor() {
        Account account = TestDataUtil.createAccount();
        Long postId = 1L;
        Long commentId = 1L;
        EditCommentRequest editCommentRequest = new EditCommentRequest("test");

        Account author = TestDataUtil.createAccount2();
        Post post = TestDataUtil.createPost(author);
        Comment comment = TestDataUtil.createComment(author, post);

        when(postRepository.findById(postId))
                .thenReturn(Optional.of(post));

        when(commentRepository.findById(commentId))
                .thenReturn(Optional.of(comment));

        assertThrows(
                UserNotAuthorException.class,
                () -> commentService.editComment(account, postId, commentId, editCommentRequest)
        );

        verify(commentRepository, never()).save(any());
    }

    @Test
    public void testThatEditCommentReturnsCommentResponse() {
        Account account = TestDataUtil.createAccount();
        Long postId = 1L;
        Long commentId = 1L;
        EditCommentRequest editCommentRequest = new EditCommentRequest("test");

        Post post = TestDataUtil.createPost(account);
        Comment comment = TestDataUtil.createComment(account, post);

        CommentResponse commentResponse = CommentResponse.builder()
                .id(comment.getId())
                .createdAt(comment.getCreatedAt())
                .authorId(comment.getAuthor().getId())
                .authorUsername(comment.getAuthor().getUsername())
                .content(editCommentRequest.getContent())
                .build();

        when(postRepository.findById(postId))
                .thenReturn(Optional.of(post));

        when(commentRepository.findById(commentId))
                .thenReturn(Optional.of(comment));

        comment.setContent(editCommentRequest.getContent());

        when(commentRepository.save(comment))
                .thenReturn(comment);

        when(commentMapper.mapFromCommentToCommentResponse(comment))
                .thenReturn(commentResponse);

        CommentResponse result = commentService.editComment(account, postId, commentId, editCommentRequest);

        assertEquals(commentResponse, result);

        verify(commentRepository).save(comment);
    }
}
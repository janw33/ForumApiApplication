package com.janwypych.ForumApi.services.comment;

import com.janwypych.ForumApi.TestDataUtil;
import com.janwypych.ForumApi.dtos.comment.CommentResponse;
import com.janwypych.ForumApi.dtos.comment.CreateCommentRequest;
import com.janwypych.ForumApi.entities.Account;
import com.janwypych.ForumApi.entities.Comment;
import com.janwypych.ForumApi.entities.Post;
import com.janwypych.ForumApi.exceptions.PostNotFoundException;
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
public class CreateCommentServiceTests {
    @Mock
    private CommentMapper commentMapper;

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    @Test
    public void testThatCreateCommentThrowsPostNotFoundExceptionWhenPostIsNotFound() {
        Account account = TestDataUtil.createAccount();
        Long postId = 1L;
        CreateCommentRequest createCommentRequest = new CreateCommentRequest("test");

        when(postRepository.findById(postId))
                .thenReturn(Optional.empty());

        assertThrows(
                PostNotFoundException.class,
                () -> commentService.createComment(account, postId, createCommentRequest)
        );

        verify(commentRepository, never()).save(any());
    }

    @Test
    public void testThatCreateCommentReturnCommentResponseWhenAccountAndPostAreValid() {
        Account account = TestDataUtil.createAccount();
        Long postId = 1L;
        CreateCommentRequest createCommentRequest = new CreateCommentRequest("test");

        Post post = TestDataUtil.createPost(account);
        Comment comment = TestDataUtil.createComment(account, post);

        CommentResponse commentResponse = CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .authorId(comment.getAuthor().getId())
                .authorUsername(comment.getAuthor().getUsername())
                .build();

        when(postRepository.findById(postId))
                .thenReturn(Optional.of(post));

        when(commentMapper.mapFromCreateCommentRequestToComment(createCommentRequest))
                .thenReturn(comment);

        when(commentRepository.save(comment))
                .thenReturn(comment);

        when(commentMapper.mapFromCommentToCommentResponse(comment))
                .thenReturn(commentResponse);

        CommentResponse result = commentService.createComment(account, postId, createCommentRequest);

        assertEquals(commentResponse, result);

        verify(commentRepository).save(comment);
    }
}

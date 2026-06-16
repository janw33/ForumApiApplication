package com.janwypych.ForumApi.services.comment;

import com.janwypych.ForumApi.TestDataUtil;
import com.janwypych.ForumApi.dtos.comment.CommentResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetCommentsServiceTests {
    @Mock
    private CommentMapper commentMapper;

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    @Test
    public void testThatGetCommentsThrowsPostNotFoundExceptionWhenPostIsNotFound() {
        Long postId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        when(postRepository.findById(postId))
                .thenReturn(Optional.empty());

        assertThrows(
                PostNotFoundException.class,
                () -> commentService.getComments(postId, pageable)
        );

        verify(commentRepository, never()).findAllByPostId(anyLong(), any());
    }

    @Test
    public void testThatGetCommentsReturnsCommentsWhenPostExists() {
        Long postId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        Account account = TestDataUtil.createAccount();
        Post post = TestDataUtil.createPost(account);
        Comment comment = TestDataUtil.createComment(account, post);

        CommentResponse commentResponse = CommentResponse.builder()
                .id(comment.getId())
                .createdAt(comment.getCreatedAt())
                .authorUsername(comment.getAuthor().getUsername())
                .authorId(comment.getAuthor().getId())
                .content(comment.getContent())
                .build();

        Page<Comment> comments = new PageImpl<>(List.of(comment));

        when(postRepository.findById(postId))
                .thenReturn(Optional.of(post));

        when(commentRepository.findAllByPostId(postId, pageable))
                .thenReturn(comments);

        when(commentMapper.mapFromCommentToCommentResponse(comment))
                .thenReturn(commentResponse);

        Page<CommentResponse> result = commentService.getComments(postId, pageable);

        assertEquals(1, result.getContent().size());
        assertEquals(commentResponse, result.getContent().getFirst());

        verify(commentRepository).findAllByPostId(postId, pageable);
        verify(commentMapper).mapFromCommentToCommentResponse(comment);
    }
}
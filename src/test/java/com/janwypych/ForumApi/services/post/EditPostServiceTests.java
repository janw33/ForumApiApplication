package com.janwypych.ForumApi.services.post;

import com.janwypych.ForumApi.TestDataUtil;
import com.janwypych.ForumApi.dtos.EditPostRequest;
import com.janwypych.ForumApi.dtos.PostResponse;
import com.janwypych.ForumApi.entities.Account;
import com.janwypych.ForumApi.entities.Post;
import com.janwypych.ForumApi.exceptions.PostNotFoundException;
import com.janwypych.ForumApi.exceptions.UserNotAuthorException;
import com.janwypych.ForumApi.mappers.PostMapper;
import com.janwypych.ForumApi.repositories.AccountRepository;
import com.janwypych.ForumApi.repositories.PostRepository;
import com.janwypych.ForumApi.services.PostService;
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
public class EditPostServiceTests {
    @Mock
    private PostMapper postMapper;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @Test
    public void testThatEditPostThrowsPostNotFoundExceptionWhenPostDoesNotExist() {
        Long userId = 1L;
        Long postId = 1L;
        EditPostRequest editPostRequest = TestDataUtil.createEditPostRequest();

        when(postRepository.findById(postId))
                .thenReturn(Optional.empty());

        assertThrows(
                PostNotFoundException.class,
                () -> postService.updatePost(userId, postId, editPostRequest)
        );

        verify(postRepository).findById(postId);
        verify(postRepository, never()).save(any());
    }

    @Test
    public void testThatEditPostThrowsUserNotAuthorExceptionWhenUserIsNotAuthor() {
        Long userId = 1L;
        Long postId = 1L;
        EditPostRequest editPostRequest = TestDataUtil.createEditPostRequest();

        Account author = TestDataUtil.createAccount();
        author.setId(2L);

        Post post = TestDataUtil.createPost(author);

        when(postRepository.findById(postId))
                .thenReturn(Optional.of(post));

        assertThrows(
                UserNotAuthorException.class,
                () -> postService.updatePost(userId, postId, editPostRequest)
        );

        verify(postRepository).findById(postId);
        verify(postRepository, never()).save(any());
    }

    @Test
    public void testThatEditPostReturnsPostResponseWhenUserIsAuthorAndPostIsValid() {
        Long userId = 1L;
        Long postId = 1L;
        EditPostRequest editPostRequest = TestDataUtil.createEditPostRequest();

        Account author = TestDataUtil.createAccount();

        Post post = TestDataUtil.createPost(author);

        PostResponse postResponse = PostResponse.builder()
                .id(post.getId())
                .title(editPostRequest.getTitle())
                .content(editPostRequest.getContent())
                .authorId(post.getAuthor().getId())
                .createdAt(post.getCreatedAt())
                .authorUsername(post.getAuthor().getUsername())
                .build();

        when(postRepository.findById(postId))
                .thenReturn(Optional.of(post));

        when(postRepository.save(post))
                .thenReturn(post);

        when(postMapper.mapFromPostToPostResponse(post))
                .thenReturn(postResponse);

        PostResponse result = postService.updatePost(userId, postId, editPostRequest);

        assertEquals(editPostRequest.getTitle(), result.getTitle());
        assertEquals(editPostRequest.getContent(), result.getContent());
        assertEquals(postResponse, result);

        verify(postRepository).findById(postId);
        verify(postRepository).save(post);
        verify(postMapper).mapFromPostToPostResponse(post);
    }
}

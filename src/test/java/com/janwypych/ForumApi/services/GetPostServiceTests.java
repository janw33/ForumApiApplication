package com.janwypych.ForumApi.services;

import com.janwypych.ForumApi.TestDataUtil;
import com.janwypych.ForumApi.dtos.PostResponse;
import com.janwypych.ForumApi.entities.Account;
import com.janwypych.ForumApi.entities.Post;
import com.janwypych.ForumApi.exceptions.PostNotFoundException;
import com.janwypych.ForumApi.mappers.PostMapper;
import com.janwypych.ForumApi.repositories.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetPostServiceTests {
    @Mock
    private PostMapper postMapper;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @Test
    public void testThatGetPostThrowsPostNotFoundExceptionWhenFindPostByIdReturnsEmptyOptional() {
        when(postRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                PostNotFoundException.class,
                () -> postService.getPost(1L)
        );
    }
    @Test
    public void testThatGetPostReturnPostResponseWhenIdIsValid() {
        Account account = TestDataUtil.createAccount();
        Post post = TestDataUtil.createPost(account);

        PostResponse postResponse = PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .authorId(post.getAuthor().getId())
                .createdAt(post.getCreatedAt())
                .authorUsername(post.getAuthor().getUsername())
                .build();

        when(postRepository.findById(1L))
                .thenReturn(Optional.of(post));

        when(postMapper.mapFromPostToPostResponse(post))
                .thenReturn(postResponse);

        PostResponse result = postService.getPost(1L);

        assertEquals(postResponse, result);

        verify(postRepository).findById(1L);
        verify(postMapper).mapFromPostToPostResponse(post);
    }
}
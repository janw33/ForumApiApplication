package com.janwypych.ForumApi.services;

import com.janwypych.ForumApi.TestDataUtil;
import com.janwypych.ForumApi.dtos.CreatePostRequest;
import com.janwypych.ForumApi.dtos.PostResponse;
import com.janwypych.ForumApi.entities.Account;
import com.janwypych.ForumApi.entities.Post;
import com.janwypych.ForumApi.exceptions.AccountNotFoundException;
import com.janwypych.ForumApi.mappers.PostMapper;
import com.janwypych.ForumApi.repositories.AccountRepository;
import com.janwypych.ForumApi.repositories.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreatePostServiceTests {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PostMapper postMapper;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @Test
    public void testThatCreatePostThrowsAccountNotFoundExceptionWhenAccountDoesNotExist() {
        Long accountId = 1L;
        CreatePostRequest createPostRequest = TestDataUtil.createPostRequest();

        when(accountRepository.findById(accountId))
                .thenReturn(Optional.empty());

        assertThrows(
                AccountNotFoundException.class,
                () -> postService.create(accountId, createPostRequest)
        );

        verify(postRepository, never()).save(any(Post.class));
    }

    @Test
    public void testThatCreatePostReturnPostResponseWhenAccountExists() {
        Account author = TestDataUtil.createAccount();
        CreatePostRequest createPostRequest = TestDataUtil.createPostRequest();

        Post post = Post.builder()
                .id(1L)
                .title(createPostRequest.getTitle())
                .content(createPostRequest.getContent())
                .createdAt(LocalDateTime.now())
                .author(author)
                .build();

        PostResponse postResponse = PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .authorId(post.getAuthor().getId())
                .createdAt(post.getCreatedAt())
                .authorUsername(post.getAuthor().getUsername())
                .build();

        when(accountRepository.findById(author.getId()))
                .thenReturn(Optional.of(author));

        when(postMapper.mapFromCreatePostRequestToPost(createPostRequest))
                .thenReturn(post);

        when(postRepository.save(post))
                .thenReturn(post);

        when(postMapper.mapFromPostToPostResponse(post))
                .thenReturn(postResponse);

        PostResponse result = postService.create(author.getId(), createPostRequest);

        assertEquals(postResponse, result);

        verify(postRepository).save(post);
    }
}

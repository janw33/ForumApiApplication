package com.janwypych.ForumApi.services.post;

import com.janwypych.ForumApi.TestDataUtil;
import com.janwypych.ForumApi.dtos.post.CreatePostRequest;
import com.janwypych.ForumApi.dtos.post.PostResponse;
import com.janwypych.ForumApi.entities.Account;
import com.janwypych.ForumApi.entities.Post;
import com.janwypych.ForumApi.exceptions.AccountNotFoundException;
import com.janwypych.ForumApi.mappers.PostMapper;
import com.janwypych.ForumApi.repositories.AccountRepository;
import com.janwypych.ForumApi.repositories.PostRepository;
import com.janwypych.ForumApi.services.PostService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.parameters.P;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
    public void testThatCreatePostReturnPostResponseWhenAccountExists() {
        Account author = TestDataUtil.createAccount();
        CreatePostRequest createPostRequest = TestDataUtil.createPostRequest();

        Post post = Post.builder()
                .title(createPostRequest.getTitle())
                .content(createPostRequest.getContent())
                .build();

        Post savedPost = Post.builder()
                .id(1L)
                .title(post.getTitle())
                .content(post.getContent())
                .author(author)
                .createdAt(LocalDateTime.now())
                .build();

        PostResponse postResponse = PostResponse.builder()
                .id(1L)
                .title(savedPost.getTitle())
                .content(savedPost.getContent())
                .authorId(savedPost.getAuthor().getId())
                .authorUsername(savedPost.getAuthor().getUsername())
                .createdAt(savedPost.getCreatedAt())
                .build();

        when(postMapper.mapFromCreatePostRequestToPost(createPostRequest))
                .thenReturn(post);

        when(postRepository.save(post))
                .thenReturn(savedPost);

        when(postMapper.mapFromPostToPostResponse(savedPost))
                .thenReturn(postResponse);

        PostResponse result = postService.create(author, createPostRequest);

        assertEquals(postResponse, result);

        verify(postRepository).save(post);
    }
}

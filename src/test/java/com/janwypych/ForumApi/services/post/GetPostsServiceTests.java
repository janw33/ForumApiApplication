package com.janwypych.ForumApi.services.post;

import com.janwypych.ForumApi.TestDataUtil;
import com.janwypych.ForumApi.dtos.post.PostResponse;
import com.janwypych.ForumApi.entities.Account;
import com.janwypych.ForumApi.entities.Post;
import com.janwypych.ForumApi.mappers.PostMapper;
import com.janwypych.ForumApi.repositories.PostRepository;
import com.janwypych.ForumApi.services.PostService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetPostsServiceTests {
    @Mock
    private PostMapper postMapper;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @Test
    public void testThatGetPostsReturnsPageOfPostResponses() {
        Pageable pageable = PageRequest.of(0, 10);

        Account account = TestDataUtil.createAccount();
        Post post = TestDataUtil.createPost(account);

        PostResponse postResponse = PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .authorId(post.getAuthor().getId())
                .authorUsername(post.getAuthor().getUsername())
                .createdAt(post.getCreatedAt())
                .build();

        Page<Post> posts = new PageImpl<>(List.of(post));

        when(postRepository.findAll(pageable))
                .thenReturn(posts);

        when(postMapper.mapFromPostToPostResponse(post))
                .thenReturn(postResponse);

        Page<PostResponse> result = postService.getPosts(pageable);

        assertEquals(1, result.getContent().size());
        assertEquals(postResponse, result.getContent().getFirst());

        verify(postRepository).findAll(pageable);
        verify(postMapper).mapFromPostToPostResponse(post);
    }
}
package com.janwypych.ForumApi.services.post;

import com.janwypych.ForumApi.TestDataUtil;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeletePostServiceTests {
    @Mock
    private PostMapper postMapper;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @Test
    public void testThatDeletePostThrowsPostNotFoundExceptionWhenPostIsNotFound() {
        Long userId = 1L;
        Long postId = 1L;

        when(postRepository.findById(postId))
                .thenReturn(Optional.empty());

        assertThrows(
                PostNotFoundException.class,
                () -> postService.deletePost(userId, postId)
        );

        verify(postRepository, never()).delete(any());
    }

    @Test
    public void testThatDeletePostThrowsUserNotAuthorExceptionWhenUserIsNotAuthor() {
        Long userId = 1L;
        Long postId = 1L;

        Account author = TestDataUtil.createAccount();
        author.setId(2L);
        Post post = TestDataUtil.createPost(author);

        when(postRepository.findById(postId))
                .thenReturn(Optional.of(post));

        assertThrows(
                UserNotAuthorException.class,
                () -> postService.deletePost(userId, postId)
        );

        verify(postRepository, never()).delete(any());
    }

    @Test
    public void testThatDeletePostDeletesPostWhenUserIsAuthorAndPostIsValid() {
        Long userId = 1L;
        Long postId = 1L;

        Account author = TestDataUtil.createAccount();
        Post post = TestDataUtil.createPost(author);

        when(postRepository.findById(postId))
                .thenReturn(Optional.of(post));

        postService.deletePost(userId, postId);

        verify(postRepository).delete(post);
    }
}
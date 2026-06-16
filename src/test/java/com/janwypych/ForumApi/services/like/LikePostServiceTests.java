package com.janwypych.ForumApi.services.like;

import com.janwypych.ForumApi.TestDataUtil;
import com.janwypych.ForumApi.dtos.like.LikeResponse;
import com.janwypych.ForumApi.entities.Account;
import com.janwypych.ForumApi.entities.Like;
import com.janwypych.ForumApi.entities.Post;
import com.janwypych.ForumApi.exceptions.LikeAlreadyExistsException;
import com.janwypych.ForumApi.exceptions.PostNotFoundException;
import com.janwypych.ForumApi.mappers.LikeMapper;
import com.janwypych.ForumApi.repositories.LikeRepository;
import com.janwypych.ForumApi.repositories.PostRepository;
import com.janwypych.ForumApi.services.LikeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LikePostServiceTests {
    @Mock
    private PostRepository postRepository;

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private LikeMapper likeMapper;

    @InjectMocks
    private LikeService likeService;

    @Test
    public void testThatLikePostThrowsPostNotFoundExceptionWhenPostIsNotFound() {
        Account currentUser = TestDataUtil.createAccount();
        Account postAuthor = TestDataUtil.createAccount2();
        Post post = TestDataUtil.createPost(postAuthor);

        when(postRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(
                PostNotFoundException.class,
                () -> likeService.likePost(currentUser, post.getId())
        );

        verify(likeRepository, never()).save(any());
    }

    @Test
    public void testThatLikePostThrowsLikeAlreadyExistsExceptionWhenLikeAlreadyExists() {
        Account currentUser = TestDataUtil.createAccount();
        Account postAuthor = TestDataUtil.createAccount2();
        Post post = TestDataUtil.createPost(postAuthor);

        when(postRepository.findById(post.getId()))
                .thenReturn(Optional.of(post));

        when(likeRepository.existsByAccountAndPost(currentUser, post))
                .thenReturn(true);

        assertThrows(
                LikeAlreadyExistsException.class,
                () -> likeService.likePost(currentUser, post.getId())
        );

        verify(likeRepository, never()).save(any());
    }

    @Test
    public void testThatLikePostReturnsLikeResponse() {
        Account currentUser = TestDataUtil.createAccount();
        Account postAuthor = TestDataUtil.createAccount2();
        Post post = TestDataUtil.createPost(postAuthor);

        when(postRepository.findById(post.getId()))
                .thenReturn(Optional.of(post));

        when(likeRepository.existsByAccountAndPost(currentUser, post))
                .thenReturn(false);

        Like likeEntity = Like.builder()
                .createdAt(LocalDateTime.now())
                .account(currentUser)
                .post(post)
                .build();

        Like savedLike = Like.builder()
                .id(1L)
                .createdAt(likeEntity.getCreatedAt())
                .account(likeEntity.getAccount())
                .post(likeEntity.getPost())
                .build();

        when(likeRepository.save(any(Like.class)))
                .thenReturn(savedLike);

        LikeResponse likeResponse = LikeResponse.builder()
                .id(savedLike.getId())
                .createdAt(savedLike.getCreatedAt())
                .build();

        when(likeMapper.mapFromLikeToLikeResponse(savedLike))
                .thenReturn(likeResponse);

        likeResponse.setAccountId(savedLike.getAccount().getId());
        likeResponse.setUsername(savedLike.getAccount().getUsername());

        LikeResponse response = likeService.likePost(currentUser, post.getId());

        assertEquals(likeResponse, response);

        verify(likeRepository).save(any(Like.class));
    }
}

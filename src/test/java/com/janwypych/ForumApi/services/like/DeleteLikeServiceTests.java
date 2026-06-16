package com.janwypych.ForumApi.services.like;

import com.janwypych.ForumApi.TestDataUtil;
import com.janwypych.ForumApi.entities.Account;
import com.janwypych.ForumApi.entities.Like;
import com.janwypych.ForumApi.entities.Post;
import com.janwypych.ForumApi.exceptions.LikeNotFoundException;
import com.janwypych.ForumApi.exceptions.PostNotFoundException;
import com.janwypych.ForumApi.repositories.LikeRepository;
import com.janwypych.ForumApi.repositories.PostRepository;
import com.janwypych.ForumApi.services.LikeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteLikeServiceTests {
    @Mock
    private PostRepository postRepository;

    @Mock
    private LikeRepository likeRepository;

    @InjectMocks
    private LikeService likeService;

    @Test
    public void testThatDeleteLikeThrowsPostNotFoundExceptionWhenPostIsNotFound() {
        Account currentUser = TestDataUtil.createAccount();
        Post post = TestDataUtil.createPost(currentUser);

        when(postRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(
                PostNotFoundException.class,
                () -> likeService.deleteLike(currentUser, post.getId())
        );

        verify(likeRepository, never()).delete(any());
    }

    @Test
    public void testThatDeleteLikeThrowsLikeNotFoundExceptionWhenLikeDoesNotExist() {
        Account currentUser = TestDataUtil.createAccount();
        Post post = TestDataUtil.createPost(currentUser);

        when(postRepository.findById(anyLong()))
                .thenReturn(Optional.of(post));

        when(likeRepository.findByAccountAndPost(any(Account.class), any(Post.class)))
                .thenReturn(Optional.empty());

        assertThrows(
                LikeNotFoundException.class,
                () -> likeService.deleteLike(currentUser, post.getId())
        );

        verify(likeRepository, never()).delete(any());
    }

    @Test
    public void testThatDeleteLikeDeletesLike() {
        Account currentUser = TestDataUtil.createAccount();
        Post post = TestDataUtil.createPost(currentUser);
        Like like = TestDataUtil.createLike(currentUser, post);

        when(postRepository.findById(anyLong()))
                .thenReturn(Optional.of(post));

        when(likeRepository.findByAccountAndPost(any(Account.class), any(Post.class)))
                .thenReturn(Optional.of(like));

        likeService.deleteLike(currentUser, post.getId());

        verify(likeRepository).delete(any(Like.class));
    }
}

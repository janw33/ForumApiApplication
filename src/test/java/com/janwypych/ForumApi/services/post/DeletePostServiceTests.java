package com.janwypych.ForumApi.services.post;

import com.janwypych.ForumApi.TestDataUtil;
import com.janwypych.ForumApi.entities.Account;
import com.janwypych.ForumApi.entities.Post;
import com.janwypych.ForumApi.entities.enums.Role;
import com.janwypych.ForumApi.exceptions.AccountHasNoPermissionException;
import com.janwypych.ForumApi.exceptions.PostNotFoundException;
import com.janwypych.ForumApi.repositories.PostRepository;
import com.janwypych.ForumApi.services.PostService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeletePostServiceTests {
    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @Test
    public void testThatDeletePostThrowsPostNotFoundExceptionWhenPostIsNotFound() {
        Account account = TestDataUtil.createAccount();
        Long postId = 1L;

        when(postRepository.findById(postId))
                .thenReturn(Optional.empty());

        assertThrows(
                PostNotFoundException.class,
                () -> postService.deletePost(account, postId)
        );

        verify(postRepository, never()).delete(any());
    }

    @Test
    public void testThatDeletePostThrowsAccountHasNoPermissionExceptionWhenUserIsNotAuthorAndNotAdmin() {
        Account account = TestDataUtil.createAccount();
        Long postId = 1L;

        Account author = TestDataUtil.createAccount2();
        Post post = TestDataUtil.createPost(author);

        when(postRepository.findById(postId))
                .thenReturn(Optional.of(post));

        assertThrows(
                AccountHasNoPermissionException.class,
                () -> postService.deletePost(account, postId)
        );

        verify(postRepository, never()).delete(any());
    }

    @Test
    public void testThatDeletePostDeletesPostWhenUserIsAuthorAndPostIsValid() {
        Account account = TestDataUtil.createAccount();
        Long postId = 1L;

        Post post = TestDataUtil.createPost(account);

        when(postRepository.findById(postId))
                .thenReturn(Optional.of(post));

        postService.deletePost(account, postId);

        verify(postRepository).delete(post);
    }

    @Test
    public void testThatDeletePostDeletesPostWhenUserIsAdminAndPostIsValid() {
        Account account = TestDataUtil.createAccount();
        account.setRole(Role.ADMIN);
        Long postId = 1L;

        Account author = TestDataUtil.createAccount2();
        Post post = TestDataUtil.createPost(author);

        when(postRepository.findById(postId))
                .thenReturn(Optional.of(post));

        postService.deletePost(account, postId);

        verify(postRepository).delete(post);
    }
}
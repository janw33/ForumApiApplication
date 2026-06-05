package com.janwypych.ForumApi.services;

import com.janwypych.ForumApi.dtos.CreatePostRequest;
import com.janwypych.ForumApi.dtos.EditPostRequest;
import com.janwypych.ForumApi.dtos.PostResponse;
import com.janwypych.ForumApi.entities.Account;
import com.janwypych.ForumApi.entities.Post;
import com.janwypych.ForumApi.exceptions.AccountNotFoundException;
import com.janwypych.ForumApi.exceptions.PostNotFoundException;
import com.janwypych.ForumApi.exceptions.UserNotAuthorException;
import com.janwypych.ForumApi.mappers.PostMapper;
import com.janwypych.ForumApi.repositories.AccountRepository;
import com.janwypych.ForumApi.repositories.PostRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PostService {
    private final PostMapper postMapper;
    private final PostRepository postRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public PostService(PostMapper postMapper, PostRepository postRepository, AccountRepository accountRepository) {
        this.postMapper = postMapper;
        this.postRepository = postRepository;
        this.accountRepository = accountRepository;
    }

    public PostResponse create(Long userId, CreatePostRequest createPostRequest) {
        Account author = accountRepository.findById(userId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        Post post = postMapper.mapFromCreatePostRequestToPost(createPostRequest);
        post.setAuthor(author);
        post.setCreatedAt(LocalDateTime.now());
        Post savedPost = postRepository.save(post);

        PostResponse postResponse = postMapper.mapFromPostToPostResponse(savedPost);
        postResponse.setAuthorId(savedPost.getAuthor().getId());
        postResponse.setAuthorUsername(savedPost.getAuthor().getUsername());
        return postResponse;
    }

    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        return postMapper.mapFromPostToPostResponse(post);
    }

    public Page <PostResponse> getPosts(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        return posts.map(postMapper::mapFromPostToPostResponse);
    }

    public PostResponse updatePost(Long userId, Long postId, EditPostRequest editPostRequest) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        if(!post.getAuthor().getId().equals(userId))
            throw new UserNotAuthorException("User not author");

        if (editPostRequest.getTitle() != null) {
            post.setTitle(editPostRequest.getTitle());
        }

        if (editPostRequest.getContent() != null) {
            post.setContent(editPostRequest.getContent());
        }

        return postMapper.mapFromPostToPostResponse(postRepository.save(post));
    }

    public void deletePost(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        if(!post.getAuthor().getId().equals(userId))
            throw new UserNotAuthorException("User not author");

        postRepository.delete(post);
    }
}

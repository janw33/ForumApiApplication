package com.janwypych.ForumApi.services;

import com.janwypych.ForumApi.dtos.CreatePostRequest;
import com.janwypych.ForumApi.dtos.PostResponse;
import com.janwypych.ForumApi.entities.Account;
import com.janwypych.ForumApi.entities.Post;
import com.janwypych.ForumApi.exceptions.AccountNotFoundException;
import com.janwypych.ForumApi.mappers.PostMapper;
import com.janwypych.ForumApi.repositories.AccountRepository;
import com.janwypych.ForumApi.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;

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
        return postMapper.mapFromPostToPostResponse(savedPost);
    }
}

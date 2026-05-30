package com.janwypych.ForumApi.controllers;

import com.janwypych.ForumApi.dtos.CreatePostRequest;
import com.janwypych.ForumApi.dtos.PostResponse;
import com.janwypych.ForumApi.services.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            @Valid @RequestBody CreatePostRequest createPostRequest
            ) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        Long userId = (Long) authentication.getPrincipal();

        return ResponseEntity.status(HttpStatus.CREATED).body(postService.create(userId, createPostRequest));
    }
}

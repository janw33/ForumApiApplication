package com.janwypych.ForumApi.controllers;

import com.janwypych.ForumApi.dtos.post.CreatePostRequest;
import com.janwypych.ForumApi.dtos.post.EditPostRequest;
import com.janwypych.ForumApi.dtos.post.PostResponse;
import com.janwypych.ForumApi.services.PostService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

        Long userId =  Long.parseLong(authentication.getName());

        return ResponseEntity.status(HttpStatus.CREATED).body(postService.create(userId, createPostRequest));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<PostResponse> getPost(
            @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(postService.getPost(id));
    }

    @GetMapping
    public Page <PostResponse> getPosts(Pageable pageable) {
        return postService.getPosts(pageable);
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<PostResponse> editPost(
            @Valid @RequestBody EditPostRequest editPostRequest,
            @PathVariable("id") Long postId
    ) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        Long userId =  Long.parseLong(authentication.getName());

        return ResponseEntity.ok(postService.updatePost(userId, postId, editPostRequest));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable("id") Long postId
    ) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        Long userId =  Long.parseLong(authentication.getName());

        postService.deletePost(userId, postId);

        return ResponseEntity.noContent().build();
    }
}

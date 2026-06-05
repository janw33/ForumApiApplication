package com.janwypych.ForumApi.controllers;

import com.janwypych.ForumApi.dtos.comment.CommentResponse;
import com.janwypych.ForumApi.dtos.comment.CreateCommentRequest;
import com.janwypych.ForumApi.services.CommentService;
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
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping(path = "/{postId}/comments")
    public ResponseEntity<CommentResponse> createComment (
            @Valid @RequestBody CreateCommentRequest createCommentRequest,
            @PathVariable("postId") Long postId
    ) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        Long userId =  Long.parseLong(authentication.getName());

        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(postId, userId, createCommentRequest));
    }

    @GetMapping(path = "/{postId}/comments")
    public Page<CommentResponse> getComments(
            @PathVariable("postId") Long postId,
            Pageable pageable
    ) {
        return commentService.getComments(postId, pageable);
    }
}

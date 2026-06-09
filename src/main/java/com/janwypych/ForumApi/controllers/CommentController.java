package com.janwypych.ForumApi.controllers;

import com.janwypych.ForumApi.dtos.comment.CommentResponse;
import com.janwypych.ForumApi.dtos.comment.CreateCommentRequest;
import com.janwypych.ForumApi.dtos.comment.EditCommentRequest;
import com.janwypych.ForumApi.entities.Account;
import com.janwypych.ForumApi.services.CommentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CommentResponse> createComment (
            @AuthenticationPrincipal Account currentUser,
            @Valid @RequestBody CreateCommentRequest createCommentRequest,
            @PathVariable("postId") Long postId
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(currentUser, postId, createCommentRequest));
    }

    @GetMapping(path = "/{postId}/comments")
    public Page<CommentResponse> getComments(
            @PathVariable("postId") Long postId,
            Pageable pageable
    ) {
        return commentService.getComments(postId, pageable);
    }

    @PatchMapping(path = "/{postId}/comments/{commentId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CommentResponse> editComment(
            @AuthenticationPrincipal Account currentUser,
            @PathVariable("postId") Long postId,
            @PathVariable("commentId") Long commentId,
            @Valid @RequestBody EditCommentRequest editCommentRequest
    ) {
        return ResponseEntity.ok(commentService.editComment(currentUser, postId, commentId, editCommentRequest));
    }

    @DeleteMapping(path = "/{postId}/comments/{commentId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> deleteComment(
            @AuthenticationPrincipal Account currentUser,
            @PathVariable("postId") Long postId,
            @PathVariable("commentId") Long commentId
    ) {
        commentService.deleteComment(currentUser, postId, commentId);

        return ResponseEntity.noContent().build();
    }
}

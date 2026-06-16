package com.janwypych.ForumApi.controllers;

import com.janwypych.ForumApi.dtos.like.LikeResponse;
import com.janwypych.ForumApi.entities.Account;
import com.janwypych.ForumApi.services.LikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/posts")
public class LikeController {
    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping(path = "/{postId}/likes")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<LikeResponse> likePost(
            @AuthenticationPrincipal Account currentUser,
            @PathVariable("postId") Long postId
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(likeService.likePost(currentUser, postId));
    }
}

package com.janwypych.ForumApi.services;

import com.janwypych.ForumApi.dtos.comment.CommentResponse;
import com.janwypych.ForumApi.dtos.comment.CreateCommentRequest;
import com.janwypych.ForumApi.entities.Account;
import com.janwypych.ForumApi.entities.Comment;
import com.janwypych.ForumApi.entities.Post;
import com.janwypych.ForumApi.exceptions.AccountNotFoundException;
import com.janwypych.ForumApi.exceptions.PostNotFoundException;
import com.janwypych.ForumApi.mappers.CommentMapper;
import com.janwypych.ForumApi.repositories.AccountRepository;
import com.janwypych.ForumApi.repositories.CommentRepository;
import com.janwypych.ForumApi.repositories.PostRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentService {
    private final CommentMapper commentMapper;
    private final AccountRepository accountRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository, CommentMapper commentMapper, AccountRepository accountRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.accountRepository = accountRepository;
        this.postRepository = postRepository;
    }

    public CommentResponse createComment(Long postId, Long userId, CreateCommentRequest createCommentRequest) {
        Account author = accountRepository.findById(userId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        Comment comment = commentMapper.mapFromCreateCommentRequestToComment(createCommentRequest);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setAuthor(author);
        comment.setPost(post);

        Comment savedComment = commentRepository.save(comment);

        return commentMapper.mapFromCommentToCommentResponse(savedComment);
    }
}

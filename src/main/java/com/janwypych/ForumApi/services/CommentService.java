package com.janwypych.ForumApi.services;

import com.janwypych.ForumApi.dtos.comment.CommentResponse;
import com.janwypych.ForumApi.dtos.comment.CreateCommentRequest;
import com.janwypych.ForumApi.dtos.comment.EditCommentRequest;
import com.janwypych.ForumApi.entities.Account;
import com.janwypych.ForumApi.entities.Comment;
import com.janwypych.ForumApi.entities.Post;
import com.janwypych.ForumApi.exceptions.AccountNotFoundException;
import com.janwypych.ForumApi.exceptions.CommentNotFoundException;
import com.janwypych.ForumApi.exceptions.PostNotFoundException;
import com.janwypych.ForumApi.exceptions.UserNotAuthorException;
import com.janwypych.ForumApi.mappers.CommentMapper;
import com.janwypych.ForumApi.repositories.AccountRepository;
import com.janwypych.ForumApi.repositories.CommentRepository;
import com.janwypych.ForumApi.repositories.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public CommentResponse createComment(Account currentUser, Long postId, CreateCommentRequest createCommentRequest) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        Comment comment = commentMapper.mapFromCreateCommentRequestToComment(createCommentRequest);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setAuthor(currentUser);
        comment.setPost(post);

        Comment savedComment = commentRepository.save(comment);

        CommentResponse commentResponse = commentMapper.mapFromCommentToCommentResponse(savedComment);
        commentResponse.setAuthorId(comment.getAuthor().getId());
        commentResponse.setAuthorUsername(comment.getAuthor().getUsername());

        return commentResponse;
    }

    public Page<CommentResponse> getComments(Long postId, Pageable pageable) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        Page<Comment> comments = commentRepository.findAllByPostId(postId, pageable);

        return comments.map(commentMapper::mapFromCommentToCommentResponse);
    }

    public CommentResponse editComment(Long userId, Long postId, Long commentId, EditCommentRequest editCommentRequest) {
        accountRepository.findById(userId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found"));

        if (!comment.getPost().getId().equals(postId))
            throw new CommentNotFoundException("comment not found");

        if (!comment.getAuthor().getId().equals(userId))
            throw new UserNotAuthorException("User is not author of this comment");

        if (editCommentRequest.getContent() != null)
            comment.setContent(editCommentRequest.getContent());

        return commentMapper.mapFromCommentToCommentResponse(commentRepository.save(comment));
    }

    public void deleteComment(Long userId, Long postId, Long commentId) {
        accountRepository.findById(userId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found"));

        if (!comment.getPost().getId().equals(postId))
            throw new CommentNotFoundException("comment not found");

        if (!comment.getAuthor().getId().equals(userId) && !post.getAuthor().getId().equals(userId))
            throw new UserNotAuthorException("User is not author");

        commentRepository.deleteById(commentId);
    }
}

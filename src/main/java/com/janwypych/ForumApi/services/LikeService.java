package com.janwypych.ForumApi.services;

import com.janwypych.ForumApi.dtos.like.LikeResponse;
import com.janwypych.ForumApi.entities.Account;
import com.janwypych.ForumApi.entities.Like;
import com.janwypych.ForumApi.entities.Post;
import com.janwypych.ForumApi.exceptions.LikeAlreadyExistsException;
import com.janwypych.ForumApi.exceptions.PostNotFoundException;
import com.janwypych.ForumApi.mappers.LikeMapper;
import com.janwypych.ForumApi.repositories.CommentRepository;
import com.janwypych.ForumApi.repositories.LikeRepository;
import com.janwypych.ForumApi.repositories.PostRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LikeService {
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final LikeMapper likeMapper ;

    public LikeService(PostRepository postRepository, LikeRepository likeRepository, LikeMapper likeMapper) {
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.likeMapper = likeMapper;
    }

    public LikeResponse likePost(Account currentUser, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        if(likeRepository.existsByAccountAndPost(currentUser, post)) {
            throw new LikeAlreadyExistsException("Like already exists");
        }

        Like likeEntity = Like.builder()
                .createdAt(LocalDateTime.now())
                .account(currentUser)
                .post(post)
                .build();

        Like savedLike = likeRepository.save(likeEntity);

        LikeResponse likeResponse = likeMapper.mapFromLikeToLikeResponse(savedLike);
        likeResponse.setAccountId(savedLike.getAccount().getId());
        likeResponse.setUsername(savedLike.getAccount().getUsername());

        return likeResponse;
    }
}

package com.janwypych.ForumApi.repositories;

import com.janwypych.ForumApi.entities.Account;
import com.janwypych.ForumApi.entities.Like;
import com.janwypych.ForumApi.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByAccountAndPost(Account account, Post post);
}

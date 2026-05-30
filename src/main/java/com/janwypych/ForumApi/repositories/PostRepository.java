package com.janwypych.ForumApi.repositories;

import com.janwypych.ForumApi.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}

package com.janwypych.ForumApi.repositories;

import com.janwypych.ForumApi.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}

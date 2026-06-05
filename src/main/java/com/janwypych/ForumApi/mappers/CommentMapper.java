package com.janwypych.ForumApi.mappers;

import com.janwypych.ForumApi.dtos.comment.CommentResponse;
import com.janwypych.ForumApi.dtos.comment.CreateCommentRequest;
import com.janwypych.ForumApi.entities.Comment;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {
    private final ModelMapper modelMapper;

    public CommentMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Comment mapFromCreateCommentRequestToComment(CreateCommentRequest createCommentRequest) {
        return modelMapper.map(createCommentRequest, Comment.class);
    }

    public CommentResponse mapFromCommentToCommentResponse(Comment comment) {
        return modelMapper.map(comment, CommentResponse.class);
    }
}

package com.janwypych.ForumApi.mappers;

import com.janwypych.ForumApi.dtos.post.CreatePostRequest;
import com.janwypych.ForumApi.dtos.post.PostResponse;
import com.janwypych.ForumApi.entities.Post;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {
    private final ModelMapper modelMapper;

    public PostMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Post mapFromCreatePostRequestToPost(CreatePostRequest createPostRequest) {
        return modelMapper.map(createPostRequest, Post.class);
    }

    public PostResponse mapFromPostToPostResponse(Post post) {
        return modelMapper.map(post, PostResponse.class);
    }
}

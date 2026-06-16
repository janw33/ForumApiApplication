package com.janwypych.ForumApi.mappers;

import com.janwypych.ForumApi.dtos.like.LikeResponse;
import com.janwypych.ForumApi.entities.Like;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class LikeMapper {
    private final ModelMapper modelMapper;

    public LikeMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public LikeResponse mapFromLikeToLikeResponse(Like like) {
        return modelMapper.map(like, LikeResponse.class);
    }
}

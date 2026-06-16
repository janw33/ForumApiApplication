package com.janwypych.ForumApi.dtos.like;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikeResponse {
    private Long id;
    private LocalDateTime createdAt;
    private Long accountId;
    private String username;
}

package com.janwypych.ForumApi.dtos.comment;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EditCommentRequest {
    @Size(min = 2, max = 200)
    private String content;
}
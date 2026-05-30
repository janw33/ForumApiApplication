package com.janwypych.ForumApi.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePostRequest {
    @NotBlank
    @Size(min = 2, max = 50)
    private String title;

    @NotBlank
    private String content;
}

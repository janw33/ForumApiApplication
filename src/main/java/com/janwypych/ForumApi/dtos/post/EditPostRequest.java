package com.janwypych.ForumApi.dtos.post;


import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EditPostRequest {
    @Size(min = 2, max = 50)
    private String title;

    @Size(min = 2, max = 5000)
    private String content;

}

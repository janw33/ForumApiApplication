package com.janwypych.ForumApi.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAccountRequest {
    @NotBlank
    @Size(min = 2, max = 20)
    @Pattern(
            regexp = "^[a-zA-Z0-9](?:[a-zA-Z0-9_]{1,18}[a-zA-Z0-9])?$",
            message = "Username must contain only letters, numbers and underscores")
    private String username;

    @NotBlank
    @Size(min = 2, max = 100)
    @Email
    private String email;

    @NotBlank
    @Size(min = 8, max = 100)
    private String password;
}

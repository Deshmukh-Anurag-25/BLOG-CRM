package com.blogsphere.blogsphere.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {

    @NotBlank(message = "content is required")
    private String content;
    private Long postId;
}

package com.blogsphere.blogsphere.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageRequest {

    @NotBlank(message = "title is required")
    private String title;

    @NotBlank(message = "slug is required")
    private String slug;

    private String content;
}

package com.blogsphere.blogsphere.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequest {

    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "slug is required")
    private String slug;
}

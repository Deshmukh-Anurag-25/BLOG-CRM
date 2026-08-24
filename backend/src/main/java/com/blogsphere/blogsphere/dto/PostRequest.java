package com.blogsphere.blogsphere.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class PostRequest {
    private String title;
    private String slug;
    private String content;
    private String excerpt;
    private Long categoryId;
    private Set<Long> tagIds;
}

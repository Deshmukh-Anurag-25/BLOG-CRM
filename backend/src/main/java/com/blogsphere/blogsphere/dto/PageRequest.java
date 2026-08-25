package com.blogsphere.blogsphere.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageRequest {
    private String title;
    private String slug;
    private String content;
}
